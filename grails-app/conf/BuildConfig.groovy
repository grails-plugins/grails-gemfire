grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()

        mavenRepo 'http://maven.springframework.org/milestone'
        mavenRepo 'http://dist.gemstone.com/maven/release'
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.5'
        
        compile('org.springframework.data.gemfire:spring-gemfire:1.0.1.RELEASE') {
            excludes 'spring-context', 'spring-tx'
        }
        compile('com.gemstone.gemfire:gemfire:6.5.1.4') {
            excludes 'antlr'
        }

	    def excludes = {
	        excludes "slf4j-simple", "persistence-api", "commons-logging", "jcl-over-slf4j", "slf4j-api", "jta"
	        excludes "spring-core", "spring-beans", "spring-aop", "spring-tx", "spring-context", "spring-web"
	    }
    }
    
    plugins {
        test ':functional-test:1.2.7'
        build ":tomcat:$grailsVersion"
    }
}
