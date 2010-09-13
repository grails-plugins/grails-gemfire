package org.grails.gemfire

import com.gemstone.gemfire.cache.Region
import org.springframework.data.gemfire.GemfireCallback

class GemfireHelperTests extends GroovyTestCase {

    void testSubscriptAccess() {
       def gemfireTemplate = new DummyGemfireTemplate()
       def helper = new GemfireHelper(gemfireTemplate: gemfireTemplate)

       helper['language'] = 'Groovy'
       assertEquals 'Groovy', helper['language']

       helper[9] = 'Nine'
       assertEquals 'Nine', helper[9]
    }
}

class DummyGemfireTemplate {

    def map = [:]

    def execute(GemfireCallback callback) {
        def dummyRegion = [put:{ k, v -> map[k] = v}, get: { k -> map[k]}] as Region
        callback.doInGemfire(dummyRegion)
    }
}
