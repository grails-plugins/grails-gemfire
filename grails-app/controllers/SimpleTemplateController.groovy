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
        def stats = region1.statistics
        [cacheEntries: result, statistics: stats]
    }
    
    def displayShortValues = {
        def q = 'length < 20'
        def result = region1.query(q)
        
        render view: 'queryResults', model: [query: q, results: result]
        
    }
    
    def displayLongValues = {
        def q = 'length >= 20'
        def result = region1.query(q)
        
        render view: 'queryResults', model: [query: q, results: result]
    }
}