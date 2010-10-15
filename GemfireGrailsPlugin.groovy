import groovy.lang.Closure;

import org.grails.plugins.gemfire.*
import org.grails.datastore.gorm.gemfire.*
import org.grails.datastore.gorm.support.DatastorePersistenceContextInterceptor
import org.springframework.datastore.mapping.web.support.OpenSessionInViewInterceptor
import org.springframework.datastore.mapping.transactions.DatastoreTransactionManager
import org.grails.datastore.gorm.GormInstanceApi
import org.codehaus.groovy.grails.commons.GrailsDomainClassProperty
import org.springframework.datastore.mapping.reflect.ClassPropertyFetcher
import org.springframework.context.ApplicationContext
import org.springframework.datastore.mapping.core.Datastore
import org.springframework.transaction.PlatformTransactionManager
import org.grails.datastore.gorm.utils.InstanceProxy


class GemfireGrailsPlugin {
    // the plugin version
    def version = "0.1-SNAPSHOT"

        // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.4 > *"

        // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp",
            "grails-app/controllers/**",
            "grails-app/views/simpleTemplate/*",
            "**/.gitignore"
    ]

    def author = "Jeff Brown"
    def authorEmail = "jeff.brown@springsource.com"
    def title = "Grails Gemfire Plugin"
    def description = '''\\
The Gemfire plugin for Grails provides integration with the GemFire in-memory distributed
data management platform.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/gemfire"

    def doWithSpring = {
		def servers = application.config.grails?.gemfire?.servers
		if(servers instanceof Closure) {
			def serverConfig = new CacheServerBuilder().evaluate(servers)
			for(entry in serverConfig) {
				def poolFactory = entry.value.remove('pool')
				"${entry.key}"(org.springframework.data.gemfire.CacheFactoryBean) {
					properties = entry.value
				}
				if(poolFactory) {
					"${entry.key}Pool"(org.grails.plugins.gemfire.PoolFactoryBean) {
						poolFactory = poolFactory
					}					
				}
			}
		}
		else {
        	defaultGemfireCache(org.springframework.data.gemfire.CacheFactoryBean){}			
		}


        def regions = application.config.grails?.gemfire?.regions
        if(regions instanceof Closure) {
            def builder = new RegionMetadataBuilder()
            regions.resolveStrategy = Closure.DELEGATE_FIRST
            regions.delegate = builder
            regions()
            
            def regionMetada = builder.regions
            regionMetada.each { metadata ->
                def regionName = metadata.name
                def regionAttributes = metadata.attributes
                "${regionName}GemfireRegion"(org.springframework.data.gemfire.RegionFactoryBean) { bean ->
					cache = ref("defaultGemfireCache")
                    name = regionName
                    if(regionAttributes) {
                        attributes = regionAttributes
                    }
                }
                "${regionName}GemfireTemplate"(org.springframework.data.gemfire.GemfireTemplate) {
                    region = ref("${regionName}GemfireRegion")
                }
                "${regionName}"(org.grails.plugins.gemfire.GemfireHelper) {
                    template = ref("${regionName}GemfireTemplate")
                }
            }
        }
        def gemfireConfig = application.config?.grails?.gemfire?.config

        def existingTransactionManager = manager?.hasGrailsPlugin("hibernate") || getSpringConfig().containsBean("transactionManager")
        def txManagerName = existingTransactionManager ? 'datastoreTransactionManager' : 'transactionManager'

        "$txManagerName"(DatastoreTransactionManager) {
          datastore = ref("springDatastore")
        }

        datastoreMappingContext(GemfireMappingContextFactoryBean) {
          grailsApplication = ref('grailsApplication')
          pluginManager = ref('pluginManager')
        }
        springDatastore(GemfireDatastoreFactoryBean) { bean ->
		  bean.autowire = true
          config = gemfireConfig
          mappingContext = ref("datastoreMappingContext")
          pluginManager = ref('pluginManager')
        }
        datastorePersistenceInterceptor(DatastorePersistenceContextInterceptor, ref("springDatastore"))

        if (manager?.hasGrailsPlugin("controllers")) {
            datastoreOpenSessionInViewInterceptor(OpenSessionInViewInterceptor) {
                datastore = ref("springDatastore")
            }
            if (getSpringConfig().containsBean("controllerHandlerMappings")) {
                controllerHandlerMappings.interceptors << datastoreOpenSessionInViewInterceptor
            }
            if (getSpringConfig().containsBean("annotationHandlerMapping")) {
                if (annotationHandlerMapping.interceptors) {
                    annotationHandlerMapping.interceptors << datastoreOpenSessionInViewInterceptor
                }
                else {
                    annotationHandlerMapping.interceptors = [datastoreOpenSessionInViewInterceptor]
                }
            }
        }
    }

	def doWithDynamicMethods = { ApplicationContext ctx ->
      Datastore datastore = ctx.getBean(Datastore)
      PlatformTransactionManager transactionManager = ctx.getBean(DatastoreTransactionManager)
      def enhancer = transactionManager ?
                          new GemfireGormEnhancer(datastore, transactionManager) :
                          new GemfireGormEnhancer(datastore)

      def isHibernateInstalled = manager.hasGrailsPlugin("hibernate")
      for(entity in datastore.mappingContext.persistentEntities) {
        if(isHibernateInstalled) {
          def cls = entity.javaClass
          def cpf = ClassPropertyFetcher.forClass(cls)
          def mappedWith = cpf.getStaticPropertyValue(GrailsDomainClassProperty.MAPPING_STRATEGY, String)
          if(mappedWith == 'gemfire') {
            enhancer.enhance(entity)
          }
          else {
            def staticApi = new GemfireStaticApi(cls, datastore)
            def instanceApi = new GormInstanceApi(cls, datastore)
            cls.metaClass.static.getGemfire = {-> staticApi }
            cls.metaClass.getGemfire = {-> new InstanceProxy(instance:delegate, target:instanceApi) }
          }
        }
        else {
          enhancer.enhance(entity)
        }
      }		
	}
}
