package org.opentele.server.model

class Department extends AbstractObject {

    static hasMany = [patientgroups: PatientGroup, monitorKits: MonitorKit]
    
    String name
    
    static constraints = {
        name(nullable:false)
    }
	
	String toString () {
		name
	}
}
