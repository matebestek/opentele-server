package org.opentele.server.model.types


enum Unit {
	MMHG('mmHg'),
    BPM('BPM'),
    KILO('Kilo'),
    CTG('CTG'),
    PROTEIN('PROTEIN'),
    GLUCOSE('GLUCOSE'),
    DEGREES_CELSIUS('DEGREES_CELSIUS'),
    GRAM_DL("g/dL"),
    PERCENTAGE("PERCENTAGE"),
    MGRAM_L("mg/L"),
    MMOL_L("mmol/L"),
    LITER("LITER")
	
	private final String value
	
	Unit(String value) {
		this.value = value
	}

	String value() {
		value
	}
}
