package org.opentele.server.model.types

enum PatientState implements Serializable {

	ACTIVE('A'),DECEASED('D'),DISCHARGED_EQUIPMENT_DELIVERED('DCD'), DISCHARGED_EQUIPMENT_NOT_DELIVERED('DCND'),

    // Never stored in the database
    PAUSED('P')
	
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

    public static List<PatientState> getValuesForPersisting() {
        [ACTIVE, DECEASED, DISCHARGED_EQUIPMENT_DELIVERED, DISCHARGED_EQUIPMENT_NOT_DELIVERED]
    }
}

