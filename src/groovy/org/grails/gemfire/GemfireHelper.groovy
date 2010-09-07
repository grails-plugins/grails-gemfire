package org.grails.gemfire

import com.gemstone.gemfire.cache.Region
import org.springframework.data.gemfire.GemfireCallback

class GemfireHelper {

    private gemfireTemplate

    void setTemplate(template) {
        gemfireTemplate = template
    }
    
    def put(key, value) {
        def callback = { Region reg ->
            reg.put key, value
        } as GemfireCallback

        gemfireTemplate.execute callback
    }
    
    def entrySet() {
        def callback = { Region reg ->
            reg.entrySet()
        } as GemfireCallback
        
        gemfireTemplate.execute(callback)        
    }
}