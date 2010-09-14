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

class RegionAttributesBuilderHelper {
    
    @Delegate AttributesFactory attributesFactory = new AttributesFactory(statisticsEnabled: true)
    
    void setEntryTimeToLive(timeToLive) {
        if(timeToLive instanceof ExpirationAttributes) {
            attributesFactory.entryTimeToLive = timeToLive
        } else {
            attributesFactory.entryTimeToLive = new ExpirationAttributes(timeToLive, ExpirationAction.DESTROY)
        }
    }
    
    def getAttributes() {
        attributesFactory.create()
    }
}