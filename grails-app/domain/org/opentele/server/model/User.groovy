package org.opentele.server.model

import org.opentele.server.util.PasswordUtil
import org.springframework.security.core.context.SecurityContextHolder

class User extends AbstractObject {

	transient springSecurityService

	String username
	String password
	boolean enabled
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
    String cleartextPassword
    int badLoginAttemps

	def name() {
		def patient = Patient.findByUser(this)
		if (patient) {
			return patient.toString()
		}
				
		def clinician = Clinician.findByUser(this)
		if (clinician) {
			return clinician.toString()
		}
		
		return username
	}
	
	def isPatient() {
        Patient.findByUser(this) != null
    }

    def isClinician() {
        Clinician.findByUser(this) != null
    }

    static constraints = {
        username nullable: false, unique: true, blank: false, maxSize:  128
        password nullable: false, blank: false, validator: PasswordUtil.passwordValidator
        cleartextPassword nullable: true, blank: true, validator: PasswordUtil.passwordValidator
	}

	static mapping = {
		password column: 'password'
        table 'users'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}

	def beforeInsert() {

        def createdDate =  new Date()

        this.createdDate = createdDate
        this.createdBy = SecurityContextHolder.context.authentication?.name ?: "Unknown"
        this.modifiedDate = createdDate
        this.modifiedBy = SecurityContextHolder.context.authentication?.name ?: "Unknown"

		encodePassword()
	}

	def beforeUpdate() {

        this.modifiedBy = SecurityContextHolder.context.authentication?.name ?: "Unknown"
        this.modifiedDate = new Date()

        if (!password || password.empty) {
            return; //Don't encode empty passwords. They will cause the constraint password: 'blank:false' not to fail
        }
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService.encodePassword(password.toLowerCase())
	}
}
