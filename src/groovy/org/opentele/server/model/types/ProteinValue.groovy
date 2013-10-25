package org.opentele.server.model.types

enum ProteinValue {
    // In ascending order, so that ordinal() can be used for comparing
	NEGATIVE('Neg.'),
    PLUSMINUS('+/-'),
    PLUS_ONE('+1'),
    PLUS_TWO('+2'),
    PLUS_THREE('+3'),
    PLUS_FOUR('+4')
	
	private final String value

	ProteinValue(String value) {
		this.value = value
	}

    String value() {
		value
	}

	String toString() {
        value
	}

    static boolean hasOrdinal(def ordinal) {
        return ordinal instanceof Integer ? values().any { it.ordinal() == ordinal } : false
    }

    static ProteinValue fromOrdinal(def ordinal) {
        values().find { it.ordinal() == ordinal }
    }

    public static ProteinValue fromString(String text) {
        ProteinValue result = values().find { it.value.equalsIgnoreCase(text) }
        if (!result) {
            throw new IllegalArgumentException("No enum value of ProteinValue with text '${text}'");
        }
        result
    }
}
