import org.springframework.data.gemfire.GemfireCallback
import com.gemstone.gemfire.cache.Region

class SimpleTemplateController {
    
    def gemfireTemplate
    
    def addToCache = {
        def key = params.key
        def value = params.value
        
        def callback = { Region reg ->
            reg.put key, value
        } as GemfireCallback
        
        gemfireTemplate.execute callback
        redirect action: 'displayCache'
    }
    
    def displayCache = {
        def callback = { Region reg ->
            reg.entrySet()
        } as GemfireCallback
        
        def result = gemfireTemplate.execute(callback)
        [cacheEntries: result]
    }
}