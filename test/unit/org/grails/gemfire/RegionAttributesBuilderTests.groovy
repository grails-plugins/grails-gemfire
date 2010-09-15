package org.grails.gemfire

import com.gemstone.gemfire.cache.ExpirationAction 
import com.gemstone.gemfire.cache.ExpirationAttributes 

class RegionAttributesBuilderTests extends GroovyTestCase {

    void testTimeToLiveWithExpirationAttributesConvenienceMethod() {
        def attributes = RegionAttributesBuilder.execute {
            entryTimeToLive = expirationAttributes(7)
        }

        assertEquals 7, attributes.entryTimeToLive.timeout
        assertEquals ExpirationAction.INVALIDATE, attributes.entryTimeToLive.action
    }

    void testTimeToLiveExpirationActionWithExpirationAttributesConvenienceMethod() {
        def attributes = RegionAttributesBuilder.execute {
            entryTimeToLive = expirationAttributes(7, LOCAL_INVALIDATE)
        }
        
        assertEquals 7, attributes.entryTimeToLive.timeout
        assertEquals ExpirationAction.LOCAL_INVALIDATE, attributes.entryTimeToLive.action
        
        attributes = RegionAttributesBuilder.execute {
            entryTimeToLive = expirationAttributes(14, DESTROY)
        }
        
        assertEquals 14, attributes.entryTimeToLive.timeout
        assertEquals ExpirationAction.DESTROY, attributes.entryTimeToLive.action
        
        attributes = RegionAttributesBuilder.execute {
            entryTimeToLive = expirationAttributes(3, LOCAL_DESTROY)
        }
        
        assertEquals 3, attributes.entryTimeToLive.timeout
        assertEquals ExpirationAction.LOCAL_DESTROY, attributes.entryTimeToLive.action
        
        attributes = RegionAttributesBuilder.execute {
            entryTimeToLive = expirationAttributes(2112, INVALIDATE)
        }
        
        assertEquals 2112, attributes.entryTimeToLive.timeout
        assertEquals ExpirationAction.INVALIDATE, attributes.entryTimeToLive.action
    }
    
    void testExpirationAttributesTimeToLive() {
        def attributes = RegionAttributesBuilder.execute {
            entryTimeToLive = new ExpirationAttributes(42)
        }

        assertEquals 42, attributes.entryTimeToLive.timeout
    }
    
    void testIdleTimeoutWithExpirationAttributesConvenienceMethod() {
        def attributes = RegionAttributesBuilder.execute {
            entryIdleTimeout = expirationAttributes(7)
        }
        
        assertEquals 7, attributes.entryIdleTimeout.timeout
        assertEquals ExpirationAction.INVALIDATE, attributes.entryTimeToLive.action
    }
    
    void testExpirationAttributesIdleTimeout() {
        def attributes = RegionAttributesBuilder.execute {
            entryIdleTimeout = new ExpirationAttributes(42)
        }
        
        assertEquals 42, attributes.entryIdleTimeout.timeout
    }
}
