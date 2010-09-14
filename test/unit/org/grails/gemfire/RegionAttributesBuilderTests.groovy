package org.grails.gemfire

import com.gemstone.gemfire.cache.ExpirationAttributes 

class RegionAttributesBuilderTests extends GroovyTestCase {

    void testIntegerTimeToLive() {
        def attributes = RegionAttributesBuilder.execute {
            entryTimeToLive = 7
        }

        assertEquals 7, attributes.entryTimeToLive.timeout
    }

    void testExpirationAttributesTimeToLive() {
        def attributes = RegionAttributesBuilder.execute {
            entryTimeToLive = new ExpirationAttributes(42)
        }

        assertEquals 42, attributes.entryTimeToLive.timeout
    }
}
