package org.opentele.server.model

class Role extends AbstractObject {

	String authority
    
	static mapping = {
		cache true
	}

	static constraints = {
		authority blank: false, unique: true,  maxSize: 128
	}
}