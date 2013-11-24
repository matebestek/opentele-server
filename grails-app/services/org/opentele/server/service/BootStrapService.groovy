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
    private static final DateTimeFormatter DATE_TIME_FORMATTER = ISODateTimeFormat.dateTime();

    def grailsApplication

	void registerCustomJSONMarshallers() {
		JSON.registerObjectMarshaller(new CustomDomainClassJSONMarshaller(false, grailsApplication), 2)
	    JSON.registerObjectMarshaller(new CustomGroovyBeanJSONMarshaller(), 1)
        JSON.registerObjectMarshaller(Date) {
            it == null ? null : DATE_TIME_FORMATTER.print(new DateTime(it))
        }
    }

	void setupAdminUserRole (Date date) {
		
		// Setup admin role
		def adminRole = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN',
			createdBy: "System",
			modifiedBy: "System",
			createdDate: date,
			modifiedDate: date).save(failOnError: true)

			
			def adminUser = User.findByUsername('admin') ?: new User(
				username: 'admin',
				password: 'admin',
				enabled: true,
				createdBy: "System",
				modifiedBy: "System",
				createdDate: date,
				modifiedDate: date).save(failOnError: true)

		if (!adminUser.authorities.contains(adminRole)) {
			log.debug (UserRole.create(adminUser, adminRole))
		}
	}
	
	
	void setupRoles(Date date) {
		// Setup super user
		Role patientRole = new Role(authority: "DEFAULT_PATIENT_ROLE", createdBy: "System", modifiedBy: "System", createdDate: date, modifiedDate: date)
		patientRole.save(failOnError:true)

		Role clinicRole = new Role(authority: "ROLE_CLINICAL", createdBy: "System", modifiedBy: "System", createdDate: date, modifiedDate: date)
		clinicRole.save(failOnError:true)


	}
	
	

}
