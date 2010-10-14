includeTargets << grailsScript("_GrailsInit")



gemfireHome = System.getenv("GEMFIRE")
target(main: "Starts a Gemfire Cache server") {
	depends(parseArguments)
	if(!gemfireHome) {
		println "Please set the GEMFIRE environment variable to the location of your Gemfire installation"
		exit 1
	}
	else {
		def name = argsMap.params ? argsMap.params[0] : null
		if(name) {
			def cacheConfig = new File("${basedir}/grails-app/conf/cache.xml")
			def locators = argsMap.locators ?: null
			def port = argsMap.port ?: null
			def mcastPort =  argsMap['mcast-port'] ?: null
			if(!cacheConfig.exists()) {
				cacheConfig = new File("${grailsSettings.projectTargetDir}/cache.xml")
			}
			
			if(cacheConfig.exists()) {
				println "Starting Gemfire Cache Server..."
				def dir = "${grailsSettings.projectTargetDir}/cache-servers/$name"
				ant.mkdir(dir:dir)
				def arguments = {
					arg value:'start'
					arg value:"cache-xml-file=${cacheConfig.absolutePath}"				
					if(locators) {
						arg value:"locators=$locators"
						arg value:"mcast-port=0"
					}
					if(port) {
						arg value:"server-port=${port}"
					}
					if(mcastPort) {
						arg value:"mcast-port=${mcastPort}"
					}
					arg value:"-dir=$dir"			
				}
				ant.exec(osfamily:'unix', executable:"$gemfireHome/bin/cacheserver", arguments) 
				ant.exec(osfamily:'windows', executable:"$gemfireHome/bin/cacheserver.bat", arguments)
				
				println "Started Gemfire Cache Server"				
			}
			else {
				println "Error: Gemfire cache.xml file doesn't exist. Either run 'grails gemfire-generate' to generate one or create one manually at grails-app/conf/cache.xml"
				exit 1
			}
		}
		else {
			println "Please specify a cache server name. Example: grails start-cache-server server1"
			exit 1
		}

	}
}

setDefaultTarget(main)
