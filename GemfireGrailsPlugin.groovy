import groovy.lang.Closure;

import org.grails.plugins.gemfire.*


class GemfireGrailsPlugin {
    // the plugin version
    def version = "1.0.0.BUILD-SNAPSHOT"

        // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.0 > *"

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
    def description = '''\
The Gemfire plugin for Grails provides integration with the \
[GemFire|http://www.vmware.com/products/application-platform/vfabric-gemfire/overview.html] \
in-memory distributed data management platform.'''

    def license = "APACHE"
    def organization = [name: "SpringSource", url: "http://www.springsource.org/"]
    def developers = [ [name: "Graeme Rocher", email: "grocher@vmware.com"] ]
    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPGEMFIRE" ]
    def scm = [ url: "https://github.com/grails-plugins/grails-gemfire" ]

    // URL to the plugin's documentation
    def documentation = "http://grails-plugins.github.com/grails-gemfire/"

    def doWithSpring = {
		def gemfireConfig = application.config.grails?.gemfire
		def servers = gemfireConfig?.servers
		def cacheName 
		def poolName
		if(servers instanceof Closure) {
			def serverConfig = new CacheServerBuilder().evaluate(servers)
			for(entry in serverConfig) {
				def pf = entry.value.remove('pool')
				cacheName = entry.key
				"${entry.key}"(org.springframework.data.gemfire.CacheFactoryBean) {
					properties = entry.value['properties'] ?: [:]
				}
			}
		}
		else {
        	defaultGemfireCache(org.springframework.data.gemfire.CacheFactoryBean){
				properties = ['log-level':gemfireConfig?.logLevel ?: 'warning']
			}			
			cacheName = "defaultGemfireCache"
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
					cache = ref(cacheName)
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
    }
}
