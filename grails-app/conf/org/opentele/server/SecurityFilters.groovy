package org.opentele.server
import grails.plugins.artefactmessaging.ArtefactMessagingService
import org.codehaus.groovy.grails.commons.GrailsClass
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.codehaus.groovy.grails.web.util.WebUtils
import org.opentele.server.annotations.SecurityWhiteListController
import org.opentele.server.constants.Constants
import org.opentele.server.model.User

class SecurityFilters {

    def patientService
    def sessionService
    def springSecurityService
    ArtefactMessagingService artefactMessagingService
    def grailsApplication

    def filters = {

        primarySecurityFilter (controller: '*', action: '*') {

            before = {
                GrailsClass controllerClass = grailsApplication.controllerClasses.find {it.logicalPropertyName == controllerName}
                boolean hasControllerAnnotation = controllerClass.clazz.getAnnotation(SecurityWhiteListController) != null
                boolean isResourceController = controllerClass.name.endsWith('Resource')
                boolean isAuditLogEntryController = "auditLogEntry".equals(controllerName)
                def calculatedActionName = actionName ?: controllerClass.defaultActionName
                Closure hasActionAnnotation = {
                    def isActionWithNameAndAnnotation = { action, name -> action.name == name && action.getAnnotation(SecurityWhiteListController) }
                    def methods = controllerClass.clazz.declaredMethods
                    def fields = controllerClass.clazz.declaredFields

                    methods.any { method ->
                        isActionWithNameAndAnnotation.call(method, calculatedActionName)
                    } || fields.any { field ->
                        // Handling webflows - special case
                        isActionWithNameAndAnnotation.call(field, calculatedActionName+"Flow")
                    }
                }
                def greenmailEnabled = !grailsApplication.config.greenmail.disabled
                def isGreenmailController = controllerName == "greenmail"
                boolean isWhiteListed = isResourceController || isAuditLogEntryController || hasControllerAnnotation || hasActionAnnotation.call() || (greenmailEnabled && isGreenmailController)

                if (isWhiteListed) {
                    sessionService.setAccessTokens(session)
                } else if ("POST".equals(request.getMethod())) {
                    // In case of POST and no annotation, do redirect instead of handling post
                    log.debug "For POST: Setting view to noAccess"
                    sessionService.setNoPatient(session)
                    redirect(controller: 'meta', action: "noAccess")
                    return false
                } else {
                    sessionService.clearAccessTokens(session)
                }
            }

            after = { Map model ->
                String accessValidated = session[Constants.SESSION_ACCESS_VALIDATED]
                String patientId = session[Constants.SESSION_PATIENT_ID]

                log.debug "Is token set to '${accessValidated}' CPR set in session: ${patientId}  "

                if (accessValidated != null && accessValidated.equals("false")) {
                    log.debug "Setting view to noAccess"
                    redirect(controller: 'meta', action: "noAccess")
                } else if (patientId && !patientId.equals("")) {
                    def pId = Long.valueOf(patientId)

                    def allowedToView = patientService.allowedToView(pId)
                    log.debug "Allowed to View: " + allowedToView

                    if (!allowedToView) {
                        log.debug "Setting view to noAccess"
                        sessionService.setNoPatient(session)
                        redirect(controller: 'meta', action: "noAccess")
                    }
                }
            }

            afterView = { Exception e ->

            }

        }

        changePasswordFilter(controller: '*', action: '*') {
            before = {
                if(request.xhr || controllerName in ['login','logout','password']) return true

                User user = springSecurityService.currentUser as User
                if(user?.cleartextPassword) {
                    def message = artefactMessagingService.getMessage(code: "password.must.be.changed")
                    flash.message = message
                    redirect(controller: 'password', action: 'change')
                    return false
                }
            }
        }

        clincianOnlyFilter(uri: '/**') {
            before = {
                if (controllerName in ['login', 'logout', 'password', 'meta', 'videoResource']) return true

                def contextPath = applicationContext.grailsApplication.mainContext.servletContext.contextPath
                def urlRequested = WebUtils.getForwardURI(request) - contextPath

                if (urlRequested.startsWith('/rest/')) return true

                User user = springSecurityService.currentUser as User
                if (user.isClinician()) return true
                redirect uri: SpringSecurityUtils.securityConfig.logout.filterProcessesUrl // '/j_spring_security_logout'
                return false
            }
        }
    }
}
