package org.opentele.server.model

class Clinician extends AbstractObject {

    static hasMany = [
        clinician2PatientGroups: Clinician2PatientGroup,
        conferences: Conference
    ]
    
    String firstName
    String lastName
    String phone
    String mobilePhone
    String email
    String videoUser
    String videoPassword

    User user // Links to a user
   
	@Deprecated // Use property access instead
    String toString() {
		name
	}

    String getName() {
        "${firstName} ${lastName}"
    }

    static transients = ["name"]

    static constraints = {
        firstName(nullable:false, blank: false)
        lastName(nullable:false, blank: false)
        phone(nullable:true)
        mobilePhone(nullable:true)
        email(nullable:true)

        videoPassword(nullable: true)
        videoUser(nullable: true)

        user(nullable:true)
    }
    
    static mapping = {
        // firstNames index: 'name_idx'
        clinician2PatientGroups(lazy: false)
    }

    def departments() {
        def departments = []
        def c2pg = Clinician2PatientGroup.findAllByClinician(this)

        c2pg.each { group ->
            def d = group.patientGroup.department
            if (!departments.contains(d)) {
                departments << d
            }
        }

        departments
    }
}
