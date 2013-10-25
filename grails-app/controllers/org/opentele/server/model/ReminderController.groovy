package org.opentele.server.model

import grails.plugins.springsecurity.Secured
import org.opentele.server.annotations.SecurityWhiteListController
import org.opentele.server.model.types.PermissionName
import grails.converters.JSON

@Secured(PermissionName.NONE)
@SecurityWhiteListController
class ReminderController {

    def springSecurityService
    def questionnaireService

    @Secured(PermissionName.PATIENT_LOGIN)
    def next() {
        def user = springSecurityService.currentUser
        def patient = Patient.findByUser(user)
        def now = Calendar.getInstance()

        // Get the next reminder time from the questionnaire service.
        def nextReminders = questionnaireService.getNextReminders(patient, now)
        render nextReminders as JSON
    }
}
