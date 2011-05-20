/* Copyright (C) 2010 SpringSource
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.grails.plugins.gemfire

import com.gemstone.gemfire.cache.*
import com.gemstone.gemfire.cache.client.Pool
import org.codehaus.groovy.grails.plugins.GrailsPluginManager
import org.grails.datastore.gorm.events.AutoTimestampEventListener
import org.grails.datastore.gorm.events.DomainEventListener
import org.springframework.beans.factory.FactoryBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.datastore.mapping.gemfire.GemfireDatastore
import org.springframework.datastore.mapping.model.MappingContext

/**
 * Constructs a RedisDatastore instance
 *
 * @author Graeme Rocher
 * @since 1.0
 */
class GemfireDatastoreFactoryBean implements FactoryBean<GemfireDatastore>, ApplicationContextAware {

  Map<String, String> config
  MappingContext mappingContext
  GrailsPluginManager pluginManager
  Cache cache
  Pool pool
  ApplicationContext applicationContext
  
  GemfireDatastore getObject() {
    def datastore;
 	if(cache == null)
		datastore = new GemfireDatastore(mappingContext, config, (ConfigurableApplicationContext)applicationContext)
	else
		datastore = new GemfireDatastore(mappingContext, cache, (ConfigurableApplicationContext)applicationContext)
	
	if(pool) {
		datastore.gemfirePool = pool
	}	

    applicationContext.addApplicationListener new DomainEventListener(datastore)
    applicationContext.addApplicationListener new AutoTimestampEventListener(datastore)
    
	datastore.afterPropertiesSet()

    return datastore
  }

  Class<?> getObjectType() { GemfireDatastore }

  boolean isSingleton() { true }
}
