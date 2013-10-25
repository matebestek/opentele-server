package org.opentele.server.model

class Message extends AbstractObject {

    Department department
    Patient patient
	Message inReplyTo
	
    String title
    String text

    Boolean sentByPatient = false

    Boolean isRead = false

    Date sendDate
    Date readDate
	
	static mapping = {
		sort sendDate: "desc"
	}
	
	String toString() {
 		"From: " + department +"\nTo: " + patient + "\nTitle: " + title
 	}
	
    static constraints = {
        department(nullable: false)
        patient(nullable: false)
        title(nullable: false)
        text(nullable: false, maxSize: 2000)
        isRead(nullable: false)
        sentByPatient(nullable: false)
        sendDate(nullable: true) // Hvis vi skal haandtere kladder
        readDate(nullable: true)
		inReplyTo(nullable:true)
    }
}
