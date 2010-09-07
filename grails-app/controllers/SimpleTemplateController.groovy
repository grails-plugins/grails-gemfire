class SimpleTemplateController {
    
    def region1Gemfire
    
    def addToCache = {
        def key = params.key
        def value = params.value
        region1Gemfire.put key, value
        redirect action: 'displayCache'
    }
    
    def displayCache = {
        def result = region1Gemfire.entrySet()
        [cacheEntries: result]
    }
}