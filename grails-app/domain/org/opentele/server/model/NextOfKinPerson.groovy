package org.opentele.server.model

class NextOfKinPerson extends AbstractObject {
	String firstName
	String lastName
	String relation
	String phone
	String address
	String city
	String note
	
	Patient patient

	String toString() {
        nameAndRelation
	}

    String getNameAndRelation() {
        "${name}${relation ? " (${relation})" : ''}"
    }

    String getName() {
        "${firstName ?: ''} ${lastName ?: ''}"
    }

    static transients = ['name']

    static constraints = {
		firstName(nullable:true)
		lastName(nullable:true)
		relation(nullable:true)
		phone(nullable:true)
		address(nullable:true)
		city(nullable:true)
		note(nullable:true)
        patient(nullable:true)

        firstName(validator: { val, obj ->
            if (obj.firstName == null &&
                    obj.lastName == null &&
                    obj.relation == null &&
                    obj.phone == null &&
                    obj.address == null &&
                    obj.city == null &&
                    obj.note == null) {
                return ['cannotCreateEmpty'];
            }
        })
    }
}
