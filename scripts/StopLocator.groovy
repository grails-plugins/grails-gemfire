includeTargets << grailsScript("_GrailsInit")

defaultPort = 55221
gemfireHome = System.getenv("GEMFIRE")
target(main: "Starts the Gemfire locator service") {
	depends(parseArguments)
	if(!gemfireHome) {
		println "Please set the GEMFIRE environment variable to the location of your Gemfire installation"
		exit 1
	}
	else {
		def port = argsMap.port ?: defaultPort
		println "Stopping Gemfire Locator Service..."
		ant.exec(osfamily:'unix', executable:"$gemfireHome/bin/gemfire") {
			arg value:"stop-locator"
			arg value:"-port=$port"			
		}
		ant.exec(osfamily:'windows', executable:"$gemfireHome/bin/gemfire.bat") {
			arg value:"stop-locator"
			arg value:"-port=$port"			
		}		
		println "Stopped Gemfire Locator Service"		
	}
}

setDefaultTarget(main)
