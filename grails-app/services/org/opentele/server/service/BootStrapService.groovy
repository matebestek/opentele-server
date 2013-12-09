package org.opentele.server.service

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat
import org.opentele.server.model.Role
import org.opentele.server.model.User
import org.opentele.server.model.UserRole

import org.opentele.server.util.CustomDomainClassJSONMarshaller
import org.opentele.server.util.CustomGroovyBeanJSONMarshaller
import grails.converters.JSON

class BootStrapService {
    // User role names
    public static String roleAdministrator = "Administrator"
    public static String rolePatient = "Patient"
    public static String roleClinician = "Kliniker"
    public static String roleVideoConsultant = "Videokonsultør"

    private static final DateTimeFormatter DATE_TIME_FORMATTER = ISODateTimeFormat.dateTime();

    def grailsApplication

	void registerCustomJSONMarshallers() {
		JSON.registerObjectMarshaller(new CustomDomainClassJSONMarshaller(false, grailsApplication), 2)
	    JSON.registerObjectMarshaller(new CustomGroovyBeanJSONMarshaller(), 1)
        JSON.registerObjectMarshaller(Date) {
            it == null ? null : DATE_TIME_FORMATTER.print(new DateTime(it))
        }
    }
}
