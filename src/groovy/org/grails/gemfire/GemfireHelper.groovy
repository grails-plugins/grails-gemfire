package org.grails.gemfire

import com.gemstone.gemfire.cache.Region
import org.springframework.data.gemfire.GemfireCallback

class GemfireHelper {

    private gemfireTemplate

    void setTemplate(template) {
        gemfireTemplate = template
    }
    
    def query(q) {
        def callback = { Region reg ->
            reg.query q
        } as GemfireCallback

        gemfireTemplate.execute callback
    }

    def remove(key) {
        def callback = { Region reg ->
            reg.remove key
        } as GemfireCallback

        gemfireTemplate.execute callback
    }
    
    def put(key, value) {
        def callback = { Region reg ->
            reg.put key, value
        } as GemfireCallback

        gemfireTemplate.execute callback
    }
    
    void putAt(key, value) {
        def callback = { Region reg ->
            reg.put key, value
        } as GemfireCallback

        gemfireTemplate.execute callback
    }

    def getAt(String key) {
        def callback = { Region reg ->
            reg.get key
        } as GemfireCallback

        gemfireTemplate.execute callback
    }

    def getAt(key) {
        def callback = { Region reg ->
            reg.get key
        } as GemfireCallback

        gemfireTemplate.execute callback
    }

    void putAt(String key, value) {
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