package org.opentele.server.model.types


enum Operation {
	GREATER_THAN('GT'), LESS_THAN('LE'),EQUALS('EQ')
	
	private final String value
	
	Operation(String value) {
		this.value = value
	}

	String value() {
		value
	}
}
