class SimpleGemfireTemplateTests extends functionaltestplugin.FunctionalTestCase {
    
    void testAddingToCache() {
        get '/displayCache'
        assertStatus 200
        
        assertContentDoesNotContain 'key1 = val1'
        assertContentDoesNotContain 'key2 = val2'
        assertContentDoesNotContain 'key3 = val2'
        
        get '/add/key1/val1'
        assertStatus 200
        assertContentContains 'key1 = val1'
        assertContentDoesNotContain 'key2 = val2'
        assertContentDoesNotContain 'key3 = val2'
        
        get '/add/key2/val2'
        assertStatus 200
        assertContentContains 'key1 = val1'
        assertContentContains 'key2 = val2'
        assertContentDoesNotContain 'key3 = val2'
        
        get '/add/key3/val3'
        assertStatus 200
        assertContentContains 'key1 = val1'
        assertContentContains 'key2 = val2'
        assertContentContains 'key3 = val3'
    }

    void testRemovingFromCache() {
        get '/add/tempKey/tempValue'
        assertStatus 200
        assertContentContains 'tempKey = tempValue'

        get '/remove/tempKey'
        assertStatus 200
        assertContentDoesNotContain 'tempKey = tempValue'
    }
    
    void testRetrievingValueFromCache() {
        get '/add/framework/Grails'
        assertStatus 200
        assertContentContains 'framework = Grails'
        
        get '/get/framework'
        assertStatus 200
        assertContentContains 'Retrieved value from cache... framework = Grails'
    }
    
    void testRemovingValueFromCache() {
        get '/add/company/VMware'
        assertStatus 200
        assertContentContains 'company = VMware'
        
        get '/remove/company'
        assertStatus 200
        assertContentDoesNotContain 'company = VMware'
    }
}