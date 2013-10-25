package org.opentele.server

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.opentele.server.constants.Constants
import org.opentele.server.model.NextOfKinPerson;
import org.opentele.server.model.Patient;

import javax.servlet.http.HttpSession;

class SessionService {
	void setPatient(HttpSession session, Patient patient) {
        session[Constants.SESSION_NAME] = patient?.toString()
        session[Constants.SESSION_CPR] = patient?.cpr[0..5]+"-"+patient?.cpr[6..9]
        session[Constants.SESSION_PATIENT_ID] = patient?.id
        session[Constants.SESSION_PHONE] = patient?.phone
        session[Constants.SESSION_MOBILE_PHONE] = patient?.mobilePhone
        session[Constants.SESSION_EMAIL] = patient?.email
        session[Constants.SESSION_FIRST_RELEATIVE] = NextOfKinPerson.findByPatient(patient)
        session[Constants.SEESION_DATARESPONSIBLE] = patient.dataResponsible
        session[Constants.SESSION_ACCESS_VALIDATED] = "true"
	}

    void setNoPatient(HttpSession session) {
        session[Constants.SESSION_NAME] = ""
        session[Constants.SESSION_CPR] = ""
        session[Constants.SESSION_PATIENT_ID] = ""
        session[Constants.SESSION_PHONE] = ""
        session[Constants.SESSION_MOBILE_PHONE] = ""
        session[Constants.SESSION_EMAIL] = ""
        session[Constants.SESSION_FIRST_RELEATIVE] = null
        session[Constants.SEESION_DATARESPONSIBLE] = null
        session[Constants.SESSION_ACCESS_VALIDATED] = "true"
    }

    void setAccessTokens(HttpSession session) {
        session[Constants.SESSION_ACCESS_VALIDATED] = "true"
    }

    void clearAccessTokens(HttpSession session) {
        session[Constants.SESSION_ACCESS_VALIDATED] = "false"
    }

    boolean hasPermission(String permission) {
        SpringSecurityUtils.ifAllGranted(permission)
    }
}
