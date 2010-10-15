package org.grails.plugins.gemfire

import com.gemstone.gemfire.cache.client.*

class CacheServerBuilder {
	def servers = [:]
	def currentServer
	def currentPool 
	
	def evaluate(Closure callable) {
		callable.delegate = this
		callable.resolveStrategy = Closure.DELEGATE_FIRST
		callable.call()		
		return servers
	}
	
	def invokeMethod(String name, args) {
		if(args && (args[-1] instanceof Closure)) {
			currentServer = name
			servers[currentServer] = [:]
			def callable = args[-1]
			callable.delegate = this
			callable.resolveStrategy = Closure.DELEGATE_FIRST
			callable.call()
		}
	}
	
	def pool(Closure callable) {
		PoolFactory.metaClass.methodMissing = { String name, args
			if(args) {
				def mp = currentPool.metaClass.getMetaProperty(name)
				if(mp) {
					mp.setProperty(delegate, args[-1])
					return null
				}				
			}	
			throw new MissingMethodException(name, PoolManager, arguments)		
		}
		currentPool = PoolManager.createFactory()
		callable.delegate = currentPool
		callable.resolveStrategy = Closure.DELEGATE_ONLY		
		servers[currentServer]['pool'] = currentPool
		try {
			callable.call()				
		}
		finally {
			currentPool= null
		}
	
	}
	
	void setProperty(String name, val) {
		if(currentPool != null) {
			currentPool[name] = val
		}
		else {
			servers[currentServer][name] = val 			
		}
	}

}