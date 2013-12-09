import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.opentele.server.UserDetailsService
import org.opentele.server.constants.OpenteleAuditLogLookup
import org.opentele.server.util.*
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.authentication.session.ConcurrentSessionControlStrategy
import org.springframework.security.web.session.ConcurrentSessionFilter
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import wslite.http.HTTPClient
import wslite.soap.SOAPClient

// Place your Spring DSL code here
beans = {
    localeResolver(SessionLocaleResolver) {
        defaultLocale = grailsApplication.config.defaultLocale
        Locale.setDefault(grailsApplication.config.defaultLocale)

        customPropertyEditorRegistrar(CustomPropertyEditorRegistrar)
        auditLogLookupBean(OpenteleAuditLogLookup)
        userDetailsService(UserDetailsService)
    }
    caseInsensitivePasswordAuthenticationProvider(CaseInsensitivePasswordAuthenticationProvider) {
        userDetailsService = ref('userDetailsService')
        passwordEncoder = ref('passwordEncoder')
        userCache = ref('userCache')
        saltSource = ref('saltSource')
        preAuthenticationChecks = ref('preAuthenticationChecks')
        postAuthenticationChecks = ref('postAuthenticationChecks')
        hideUserNotFoundExceptions = SpringSecurityUtils.securityConfig.dao.hideUserNotFoundExceptions
    }

    openTeleSecurityBadCredentialsEventListener(OpenTeleSecurityBadCredentialsEventListener)
    openTeleSecurityGoodAttemptEventListener(OpenTeleSecurityGoodAttemptEventListener)
    basicAuthenticationFilter(OpenteleSecurityBasicAuthenticationFilter) {
        authenticationManager = ref('authenticationManager')
        authenticationEntryPoint = ref('basicAuthenticationEntryPoint')
    }

    if (grailsApplication.config.milou.run) {
        milouHttpClient(HTTPClient) {
            connectTimeout = 5000
            readTimeout = 10000
            useCaches = false
            followRedirects = false
            sslTrustAllCerts = true
        }

        milouSoapClient(SOAPClient) {
            serviceURL = grailsApplication.config.milou.serverURL
            httpClient = ref('milouHttpClient')
        }
    }

    sessionRegistry(SessionRegistryImpl)

    sessionAuthenticationStrategy(ConcurrentSessionControlStrategy, sessionRegistry) {
        maximumSessions = -1
    }

    concurrentSessionFilter(ConcurrentSessionFilter){
        sessionRegistry = sessionRegistry
        expiredUrl = '/login/concurrentSession'
    }
}
