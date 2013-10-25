package org.opentele.server

import grails.plugins.springsecurity.Secured
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.opentele.server.annotations.SecurityWhiteListController
import org.opentele.server.model.Clinician
import org.opentele.server.model.Patient
import org.opentele.server.model.types.PermissionName

@Secured("IS_AUTHENTICATED_FULLY")
@SecurityWhiteListController
class HomeController {
    def sessionService
    def springSecurityService

    /**
     * Redirects based on role.
     */
    @Secured(["IS_AUTHENTICATED_REMEMBERED"])
    def index() {
		//Clear the selected patient if any
		sessionService.setNoPatient(session)
		
        if (!sessionService.hasPermission(PermissionName.WEB_LOGIN)) {
            log.debug "Everybody else"
            redirect uri: SpringSecurityUtils.securityConfig.logout.filterProcessesUrl
            return
        }

        def user = springSecurityService.currentUser
        if (user.isClinician() && sessionService.hasPermission(PermissionName.PATIENT_READ_ALL)) {
            log.debug 'Redirecting to clinician view'
            redirect(controller: "patientOverview")
        } else if (user.isPatient()) {
            log.debug 'Redirecting to patient view'
            redirect(controller: "patient", action: "questionnaires")
        } else {
            log.debug 'Redirecting to administrator view'
            redirect(controller: "questionnaire", action: "list")
        }
    }
}
