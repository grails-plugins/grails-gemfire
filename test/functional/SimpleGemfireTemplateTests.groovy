class SimpleGemfireTemplateTests extends functionaltestplugin.FunctionalTestCase {
    
    void testAddingToCache() {
        get '/displayCache'
        
        assertContentDoesNotContain 'key1 = val1'
        assertContentDoesNotContain 'key2 = val2'
        assertContentDoesNotContain 'key3 = val2'
        
        get '/add/key1/val1'
        assertContentContains 'key1 = val1'
        assertContentDoesNotContain 'key2 = val2'
        assertContentDoesNotContain 'key3 = val2'
        
        get '/add/key2/val2'
        assertContentContains 'key1 = val1'
        assertContentContains 'key2 = val2'
        assertContentDoesNotContain 'key3 = val2'
        
        get '/add/key3/val3'
        assertContentContains 'key1 = val1'
        assertContentContains 'key2 = val2'
        assertContentContains 'key3 = val3'
    }
}