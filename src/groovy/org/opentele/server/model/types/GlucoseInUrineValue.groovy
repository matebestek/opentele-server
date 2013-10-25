package org.opentele.server.model.types

enum GlucoseInUrineValue {
    // In ascending order, so that ordinal() can be used for comparing
	NEGATIVE('Neg.'),
    PLUS_ONE('+1'),
    PLUS_TWO('+2'),
    PLUS_THREE('+3'),
    PLUS_FOUR('+4')

	private final String value

    GlucoseInUrineValue(String value) {
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

    static GlucoseInUrineValue fromOrdinal(def ordinal) {
        values().find { it.ordinal() == ordinal }
    }

    public static GlucoseInUrineValue fromString(String text) {
        GlucoseInUrineValue result = values().find { it.value.equalsIgnoreCase(text) }
        if (!result) {
            throw new IllegalArgumentException("No enum value of GlucoseInUrine with text '${text}'");
        }
        result
    }
}
