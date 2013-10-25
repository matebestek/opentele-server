package org.opentele.server.model.types


enum DataType {
    
	BOOLEAN('Boolean'), INTEGER('Integer'),INTEGER_ARRAY("Integer[]"),FLOAT('Float'),FLOAT_ARRAY('Float[]'), STRING('String'),STRING_ARRAY('String[]'),RADIOCHOICE('Radio')
	
	private final String value
	
	DataType(String value) {
		this.value = value
	}

	String value() {
		value
	}
}
