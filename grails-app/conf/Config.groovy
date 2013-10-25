import grails.plugins.springsecurity.SecurityConfigType
import org.apache.log4j.DailyRollingFileAppender


grails.config.locations = [ "file:c:/kihdatamon/settings/datamon-config.properties", "file:${userHome}/.kih/datamon-config.properties"]

grails.project.groupId = appName
grails.mime.file.extensions = true
grails.mime.use.accept.header = true
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
	xml: ['text/xml', 'application/xml'],
	text: 'text/plain',
	js: 'text/javascript',
	rss: 'application/rss+xml',
	atom: 'application/atom+xml',
	css: 'text/css',
	csv: 'text/csv',
	all: '*/*',
	json: ['application/json','text/json'],
	form: 'application/x-www-form-urlencoded',
	multipartForm: 'multipart/form-data'
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']



// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"

// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// enable query caching by default
grails.hibernate.cache.queries = true

// Som standard er email disabled og lige så greenmail. I de miljøer hvor det skal enables, skal der stå
// grails {
//   mail {
//     disabled = false
//   }
//}
//greenmail.disabled=false
// samt den øvrige mail konfiguration
grails {
    mail {
        disabled = true
        'default' {
            from="svar-ikke@opentele.dk"
        }
    }
}
greenmail.disabled = true
video.connection.timeoutMillis = 5 * 60 * 1000 // 5 minutes
video.connection.asyncTimeoutMillis = 6 * 60 * 1000 // 6 minutes

// set per-environment serverURL stem for creating absolute links
environments {
	development {
		grails.logging.jul.usebridge = true
        
        milou.run = false
        milou.repeatIntervalMillis = 180000

        video.enabled = true
        video.serviceURL = 'https://silverbullet.vconf.dk/services/v1_1/VidyoPortalUserService/'
        video.client.serviceURL = 'https://silverbullet.vconf.dk/services/VidyoPortalGuestService/'

        // Database plugin settings: Run autoupdate in all devel contexts.. But nowhere else..
        grails.plugin.databasemigration.dropOnStart = true
        grails.plugin.databasemigration.updateOnStart = true

        // If developing using MSSQL server:
        //grails.plugin.databasemigration.updateOnStartDefaultSchema = 'opentele].[dbo'

        grails.plugin.databasemigration.updateOnStartFileNames = ['changelog.groovy']
        grails.plugin.databasemigration.autoMigrateScripts = ['RunApp', 'TestApp']

        kihdb.run = false

        // Systemnavn som overføres til KIH Databasen, hvorn navnet
        // anvendes til at vise hvor maalinger kommer fra.
        kihdb.createdByText = "OpenTele udvikling"
        kihdb.repeatIntervalMillis = 180000
        kihdb.service.url = "http://localhost:8090/kih_database/services/monitoringDataset"

        // SOSI Settings
        seal.sts.url = "http://test1.ekstern-test.nspop.dk:8080/sts/services/SecurityTokenService"
        cpr.service.url = "http://test1.ekstern-test.nspop.dk:8080/stamdata-cpr-ws/service/StamdataPersonLookup"
        cpr.lookup.enabled = true

        // Keystore settings
        // Removed in open-source version

        grails {
            mail {
                disabled = false
                host = "localhost"
                port = com.icegreen.greenmail.util.ServerSetupTest.SMTP.port
            }
        }
        greenmail.disabled = false
        // For at tillade Greenmail i UDV
        grails.plugins.springsecurity.controllerAnnotations.staticRules = [
                '/greenmail/**': ['IS_AUTHENTICATED_ANONYMOUSLY']
        ]
    }
    test {
        // Database plugin settings: Run autoupdate in all devel contexts..
        // Assumes H2 database
        grails.plugin.databasemigration.dropOnStart = true
        grails.plugin.databasemigration.updateOnStart = true
        grails.plugin.databasemigration.updateOnStartFileNames = ['changelog.groovy']
        grails.plugin.databasemigration.autoMigrateScripts = ['RunApp', 'TestApp']
        milou.run = false

        kihdb.createdByText = "OpenTele intern test"
        kihdb.run = false
        kihdb.repeatIntervalMillis = 180000
        kihdb.serverURL = "https://kihdb-test.rn.dk/XXX"

        seal.sts.url = "http://test1.ekstern-test.nspop.dk:8080/sts/services/SecurityTokenService"
        cpr.service.url = "http://test1.ekstern-test.nspop.dk:8080/stamdata-cpr-ws/service/StamdataPersonLookup"

        cpr.lookup.enabled = true

        // SOSI Settings
        // Removed in open-source version

        video.enabled = false

        grails {
            mail {
                disabled = false
                host = "localhost"
                port = com.icegreen.greenmail.util.ServerSetupTest.SMTP.port
            }
        }
        greenmail.disabled = false
    }
}

String logDirectory = "${System.getProperty('catalina.base') ?: '.'}/logs"

// Logging
String commonPattern = "%d [%t] %-5p %c{2} %x - %m%n"

log4j = {
    appenders {
        console name: "stdout",
                layout: pattern(conversionPattern: commonPattern)
        appender new DailyRollingFileAppender(
                name:"opentele", datePattern: "'.'yyyy-MM-dd",
                file:"${logDirectory}/opentele.log",
                layout: pattern(conversionPattern: commonPattern))
    }

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
            'org.codehaus.groovy.grails.web.pages', //  GSP
            'org.codehaus.groovy.grails.web.sitemesh', //  layouts
            'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
            'org.codehaus.groovy.grails.web.mapping', // URL mapping
            'org.codehaus.groovy.grails.commons', // core / classloading
            'org.codehaus.groovy.grails.plugins', // plugins
            'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration,
            'org.codehaus.groovy.grails.commons.cfg',
            'org.springframework',
            'org.hibernate',
            'org.apache',
            'net.sf.ehcache.hibernate',
            'grails.app.services.org.grails.plugin.resource',
            'grails.app.taglib.org.grails.plugin.resource',
            'grails.app.resourceMappers.org.grails.plugin.resource',
            'grails.app.service.grails.buildtestdata.BuildTestDataService',
            'grails.app.buildtestdata',
            'grails.app.services.grails.buildtestdata',
            'grails.buildtestdata.DomainInstanceBuilder'

    root {
        error 'opentele', 'stdout'
    }

    environments {
        development {
            debug 'grails.app',
                    'org.opentele'
//            debug 'org.hibernate.SQL'
//           trace 'org.hibernate.type'
        }
        test {
            debug 'grails.app',
                    'org.opentele'
        }
    }
}

// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'org.opentele.server.model.User'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'org.opentele.server.model.UserRole'
grails.plugins.springsecurity.authority.className = 'org.opentele.server.model.Role'
grails.plugins.springsecurity.securityConfigType = SecurityConfigType.Annotation
grails.plugins.springsecurity.useBasicAuth = true
grails.plugins.springsecurity.basic.realmName = "OpenTele Server"
grails.plugins.springsecurity.useSecurityEventListener = true

grails.plugins.springsecurity.filterChain.chainMap = [
	'/rest/**': 'JOINED_FILTERS,-exceptionTranslationFilter,-sessionManagementFilter',
    '/**': 'JOINED_FILTERS,-basicAuthenticationFilter,-basicExceptionTranslationFilter'
 ]
grails.plugins.springsecurity.providerNames = [
        'caseInsensitivePasswordAuthenticationProvider',
        'anonymousAuthenticationProvider',
        'rememberMeAuthenticationProvider'
]


passwordRetryGracePeriod=120
passwordMaxAttempts=3
reminderEveryMinutes=15
