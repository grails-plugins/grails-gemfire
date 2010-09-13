class SimpleTemplateController {
    
    def region1Gemfire
    
    def removeFromCache = {
        def key = params.key
        region1Gemfire.remove key
        redirect action: 'displayCache'
    }

    def addToCache = {
        def key = params.key
        def value = params.value
        region1Gemfire[key] = value
        redirect action: 'displayCache'
    }
    
    def retrieveFromCache = {
        def key = params.key
        def value = region1Gemfire[key]
        render "Retrieved value from cache... ${key} = ${value}"
    }
    
    def displayCache = {
        def result = region1Gemfire.entrySet()
        [cacheEntries: result]
    }
}