import groovy.lang.Closure;

import org.grails.gemfire.RegionMetadataBuilder

class GemfireGrailsPlugin {
    // the plugin version
    def version = "0.1.M1"

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
                    cache = defaultGemfireCache
                    name = regionName
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
}
