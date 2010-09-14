class SimpleTemplateController {
    
    def region1
    
    def removeFromCache = {
        def key = params.key
        region1.remove key
        redirect action: 'displayCache'
    }

    def addToCache = {
        def key = params.key
        def value = params.value
        region1[key] = value
        redirect action: 'displayCache'
    }
    
    def retrieveFromCache = {
        def key = params.key
        def value = region1[key]
        render "Retrieved value from cache... ${key} = ${value}"
    }
    
    def displayCache = {
        def result = region1.entrySet()
        [cacheEntries: result]
    }
}