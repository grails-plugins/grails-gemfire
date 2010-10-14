package org.grails.plugins.gemfire

class RegionMetadataBuilder {

    def regions = []
    
    def methodMissing(String methodName, args) {
        def region = [name: methodName]
        if(args && args[0] instanceof Closure) {
           region.attributes = RegionAttributesBuilder.execute(args[0])
        }
        regions << region
        region
    }
}


