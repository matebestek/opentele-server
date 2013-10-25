package org.opentele.server.model.types

enum PatientState implements Serializable {

	ACTIVE('A'),DECEASED('D'),DISCHARGED_EQUIPMENT_DELIVERED('DCD'), DISCHARGED_EQUIPMENT_NOT_DELIVERED('DCND')
	
	private final String value
	
	PatientState(String value) {
		this.value = value
	}

	String value() {
		value
	}
	
	@Override
	String toString() { name() }
	
	String getKey() { name() }
	
}

