includeTargets << grailsScript("_GrailsInit")

gemfireHome = System.getenv("GEMFIRE")
target(main: "Stops a Gemfire Cache server") {
	depends(parseArguments)
	if(!gemfireHome) {
		println "Please set the GEMFIRE environment variable to the location of your Gemfire installation"
		exit 1
	}
	else {
		def name = argsMap.params ? argsMap.params[0] : null
		if(name) {
			println "Stopping Gemfire Cache Server..."
			def dir = "${grailsSettings.projectTargetDir}/cache-servers/$name"
			ant.mkdir(dir:dir)
			ant.exec(osfamily:'unix', executable:"$gemfireHome/bin/cacheserver") {
				arg value:'stop'
				arg value:"-dir=$dir"			
			}
			ant.exec(osfamily:'windows', executable:"$gemfireHome/bin/cacheserver.bat") {
				arg value:'stop'				
				arg value:"-dir=$dir"			
			}		
			println "Stopped Gemfire Cache Server"			
		}
		else {
			println "Please specify a cache server name. Example: grails start-cache-server server1"
			exit 1
		}

	}
}

setDefaultTarget(main)
