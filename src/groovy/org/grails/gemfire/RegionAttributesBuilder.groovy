package org.grails.gemfire

import com.gemstone.gemfire.cache.AttributesFactory 
import com.gemstone.gemfire.cache.ExpirationAction 
import com.gemstone.gemfire.cache.ExpirationAttributes 
import com.gemstone.gemfire.cache.RegionAttributes 

class RegionAttributesBuilder {
    
    
    static RegionAttributes execute(Closure c) {
        def helper = new RegionAttributesBuilderHelper()
        c = c.clone()
        c.resolveStrategy = Closure.DELEGATE_ONLY
        c.delegate = helper
        c()
        
        helper.attributes
    }
}

class RegionAttributesBuilderHelper extends AttributesFactory {
    
    RegionAttributesBuilderHelper() {
        statisticsEnabled = true
    }
    
    def expirationAttributes(timeout) {
        new ExpirationAttributes(timeout)
    }

        def getAttributes() {
        create()
    }
}