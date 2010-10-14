includeTargets << grailsScript("_GrailsBootstrap")

import org.grails.datastore.gorm.gemfire.config.*
import org.springframework.datastore.mapping.core.*

target(main: "The description of the script goes here!") {
	bootstrap()
	
	def datastore = appCtx.getBean(Datastore)
	
	def generator = new CacheServerConfigGenerator(datastore)
	
	println "Generating cache.xml to ${grailsSettings.projectTargetDir}/cache.xml"
	generator.generate(new File("${grailsSettings.projectTargetDir}/cache.xml"))
	println "Done."
}

setDefaultTarget(main)
