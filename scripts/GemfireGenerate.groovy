includeTargets << grailsScript("_GrailsBootstrap")

import org.grails.datastore.gorm.gemfire.config.*
import org.springframework.datastore.mapping.core.*
import com.gemstone.gemfire.internal.cache.xmlcache.*

target(main: "The description of the script goes here!") {
	bootstrap()
	
	def datastore = appCtx.getBean(Datastore)
	println "Generating cache.xml to ${grailsSettings.projectTargetDir}/cache.xml"
	new File("${grailsSettings.projectTargetDir}/cache.xml").withPrintWriter {
		CacheXmlGenerator.generate((com.gemstone.gemfire.cache.Cache)datastore.gemfireCache, it);		
	}
	println "Done."
}

setDefaultTarget(main)
