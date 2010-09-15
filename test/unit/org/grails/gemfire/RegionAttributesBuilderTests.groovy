package org.grails.gemfire

import com.gemstone.gemfire.cache.ExpirationAttributes 

class RegionAttributesBuilderTests extends GroovyTestCase {

    void testTimeToLiveWithExpirationAttributesConvenienceMethod() {
        def attributes = RegionAttributesBuilder.execute {
            entryTimeToLive = expirationAttributes(7)
        }

        assertEquals 7, attributes.entryTimeToLive.timeout
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
    }
    
    void testExpirationAttributesIdleTimeout() {
        def attributes = RegionAttributesBuilder.execute {
            entryIdleTimeout = new ExpirationAttributes(42)
        }
        
        assertEquals 42, attributes.entryIdleTimeout.timeout
    }
}
