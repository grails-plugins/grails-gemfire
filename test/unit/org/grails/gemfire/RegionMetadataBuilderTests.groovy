package org.grails.gemfire

class RegionMetadataBuilderTests extends GroovyTestCase {

    void testBuilder() {
        def builder = new RegionMetadataBuilder()
        def closure = {
            region1()
            region2 {
                entryTimeToLive = expirationAttributes(7)
            }
            region3 {
                entryTimeToLive = expirationAttributes(9)
                entryIdleTimeout = expirationAttributes(12)
            }
        }
        
        closure.delegate = builder
        closure()
        
        def regions = builder.regions
        
        assertEquals 3, regions?.size()
        def region1 = regions.find {it.name == 'region1'}
        def region2 = regions.find {it.name == 'region2'}
        def region3 = regions.find {it.name == 'region3'}
        
        assertNotNull region1
        assertNotNull region2
        assertNotNull region3
        
        assertEquals 7, region2.attributes.entryTimeToLive.timeout
        assertEquals 9, region3.attributes.entryTimeToLive.timeout
        assertEquals 12, region3.attributes.entryIdleTimeout.timeout
    }
}
