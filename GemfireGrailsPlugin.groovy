import groovy.lang.Closure;

import org.grails.gemfire.RegionMetadataBuilder

class GemfireGrailsPlugin {
    // the plugin version
    def version = "0.1-SNAPSHOT"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.4 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp",
            "grails-app/controllers/**",
            "grails-app/views/simpleTemplate/*",
            "**/.gitignore"
    ]

    // TODO Fill in these fields
    def author = "Jeff Brown"
    def authorEmail = "jeff.brown@springsource.com"
    def title = "Grails Gemfire Plugin"
    def description = '''\\
The Gemfire plugin for Grails provides integration with the GemFire in-memory distributed
data management platform.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/gemfire"

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before 
    }

    def doWithSpring = {
        defaultGemfireCache(org.springframework.data.gemfire.CacheFactoryBean){}

        def replicatedRegions = application.config.grails?.gemfire?.regions?.replicated
        if(replicatedRegions instanceof Closure) {
            def builder = new RegionMetadataBuilder()
            replicatedRegions.resolveStrategy = Closure.DELEGATE_FIRST
            replicatedRegions.delegate = builder
            replicatedRegions()
            
            def regions = builder.regions
            regions.each { regionMetadata ->
                def regionName = regionMetadata.name
                def regionAttributes = regionMetadata.attributes
                "${regionName}GemfireRegion"(org.springframework.data.gemfire.RegionFactoryBean) {
                    dataPolicy = 'REPLICATE'
                    cache = defaultGemfireCache
                    name = regionName
                    scope = 'DISTRIBUTED_ACK'
                    if(regionAttributes) {
                        attributes = regionAttributes
                    }
                }
                "${regionName}GemfireTemplate"(org.springframework.data.gemfire.GemfireTemplate) {
                    region = ref("${regionName}GemfireRegion")
                }
                "${regionName}"(org.grails.gemfire.GemfireHelper) {
                    template = ref("${regionName}GemfireTemplate")
                }
            }
        }
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
