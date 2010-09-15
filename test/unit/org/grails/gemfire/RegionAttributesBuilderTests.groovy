package org.grails.gemfire

import com.gemstone.gemfire.cache.DataPolicy
import com.gemstone.gemfire.cache.ExpirationAction 
import com.gemstone.gemfire.cache.ExpirationAttributes 
import com.gemstone.gemfire.cache.Scope;

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

    void testScope() {
        def attributes = RegionAttributesBuilder.execute {
            scope = DISTRIBUTED_ACK
        }
        assertEquals Scope.DISTRIBUTED_ACK, attributes.scope
        
        attributes = RegionAttributesBuilder.execute {
            scope = DISTRIBUTED_NO_ACK
        }
        assertEquals Scope.DISTRIBUTED_NO_ACK, attributes.scope
        
        attributes = RegionAttributesBuilder.execute {
            scope = GLOBAL
        }
        assertEquals Scope.GLOBAL, attributes.scope
        
        attributes = RegionAttributesBuilder.execute {
            scope = LOCAL
        }
        assertEquals Scope.LOCAL, attributes.scope
    }    

    void testDataPolicy() {
        def attributes = RegionAttributesBuilder.execute {
            dataPolicy = EMPTY
        }
        assertEquals DataPolicy.EMPTY, attributes.dataPolicy
        
        attributes = RegionAttributesBuilder.execute {
            dataPolicy = NORMAL
        }
        assertEquals DataPolicy.NORMAL, attributes.dataPolicy
        
        attributes = RegionAttributesBuilder.execute {
            dataPolicy = PARTITION
        }
        assertEquals DataPolicy.PARTITION, attributes.dataPolicy
        
        attributes = RegionAttributesBuilder.execute {
            dataPolicy = PERSISTENT_REPLICATE
        }
        assertEquals DataPolicy.PERSISTENT_REPLICATE, attributes.dataPolicy
        
        attributes = RegionAttributesBuilder.execute {
            dataPolicy = PRELOADED
        }
        assertEquals DataPolicy.PRELOADED, attributes.dataPolicy
        
        attributes = RegionAttributesBuilder.execute {
            dataPolicy = REPLICATE
        }
        assertEquals DataPolicy.REPLICATE, attributes.dataPolicy
    }
}
