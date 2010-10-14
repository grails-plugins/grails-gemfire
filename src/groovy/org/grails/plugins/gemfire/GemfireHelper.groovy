package org.grails.plugins.gemfire

import com.gemstone.gemfire.cache.Region
import org.springframework.data.gemfire.GemfireCallback

class GemfireHelper {

    private gemfireTemplate

    def getStatistics() {
        invokeCallback { Region reg ->
            reg.getStatistics()
        }
    }
    
    void setTemplate(template) {
        gemfireTemplate = template
    }
    
    def query(q) {
        invokeCallback { Region reg ->
            reg.query q
        }
    }

    def remove(key) {
        invokeCallback { Region reg ->
            reg.remove key
        }
    }
    
    def put(key, value) {
        invokeCallback { Region reg ->
            reg.put key, value
        } 
    }
    
    void putAt(key, value) {
        invokeCallback { Region reg ->
            reg.put key, value
        }
    }

    def getAt(String key) {
        invokeCallback { Region reg ->
            reg.get key
        }
    }

    def getAt(key) {
        invokeCallback { Region reg ->
            reg.get key
        }
    }

    void putAt(String key, value) {
        invokeCallback { Region reg ->
            reg.put key, value
        } 
    }

    def entrySet() {
        invokeCallback { Region reg ->
            reg.entrySet()
        }
    }
    
    private invokeCallback(closure) {
        gemfireTemplate.execute(closure as GemfireCallback)
    }
}