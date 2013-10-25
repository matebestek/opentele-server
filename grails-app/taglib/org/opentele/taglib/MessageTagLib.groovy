package org.opentele.taglib
import org.opentele.server.ClinicianService
import org.opentele.server.MessageService
import org.opentele.server.model.Patient

class MessageTagLib {

    MessageService messageService
    ClinicianService clinicianService

    static namespace = "message"

    def unreadMessages = { attrs ->
        if (messageService.getUnreadMessageCount() > 0) {
            out << """<strong class="messagecount ${attrs.class ?: ''}"> ("""
            out << messageService.getUnreadMessageCount()
            out << ")</strong> "
        }
    }

    def canUseMessaging = { attrs, body ->
        def patient = Patient.get(session.patientId)
        if(messageService.clinicianCanSendMessagesToPatient(clinicianService.currentClinician, patient)) {
            out << body()
        }
    }

}
