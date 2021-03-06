package org.grails.plugins.gemfire

import com.gemstone.gemfire.cache.AttributesFactory 
import com.gemstone.gemfire.cache.DataPolicy 
import com.gemstone.gemfire.cache.ExpirationAction 
import com.gemstone.gemfire.cache.ExpirationAttributes 
import com.gemstone.gemfire.cache.RegionAttributes 
import com.gemstone.gemfire.cache.Scope

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
    
    RegionAttributesBuilderHelper() {
        statisticsEnabled = true
    }
    
    def expirationAttributes(int timeout) {
        new ExpirationAttributes(timeout)
    }
    
    def expirationAttributes(int timeout, ExpirationAction action) {
        new ExpirationAttributes(timeout, action)
    }
    
    def getINVALIDATE() {
        ExpirationAction.INVALIDATE
    }
    
    def getLOCAL_INVALIDATE() {
        ExpirationAction.LOCAL_INVALIDATE
    }
    
    def getDESTROY() {
        ExpirationAction.DESTROY
    }
    
    def getLOCAL_DESTROY() {
        ExpirationAction.LOCAL_DESTROY
    }
    
    def getEMPTY() {
        DataPolicy.EMPTY
    }
    
    def getNORMAL() {
        DataPolicy.NORMAL
    }
    
    def getPARTITION() {
        DataPolicy.PARTITION
    }
    
    def getPERSISTENT_REPLICATE() {
        DataPolicy.PERSISTENT_REPLICATE
    }
    
    def getPRELOADED() {
        DataPolicy.PRELOADED
    }
    
    def getREPLICATE() {
        DataPolicy.REPLICATE
    }
    
    def getDISTRIBUTED_ACK() {
        Scope.DISTRIBUTED_ACK
    }
    
    def getDISTRIBUTED_NO_ACK() {
        Scope.DISTRIBUTED_NO_ACK
    }
    
    def getGLOBAL() {
        Scope.GLOBAL
    }
    
    def getLOCAL() {
        Scope.LOCAL
    }
    
    def getAttributes() {
        create()
    }
}