package org.opentele.server.model.types

enum Severity {
	NONE('NONE', 'no_alarm.png'),
    GREEN('GREEN', 'green_alarm.png'),
    ORANGE('ORANGE', 'orange_alarm.png'),
    BLUE('BLUE', 'blue_alarm.png'),
    YELLOW('YELLOW', 'yellow_alarm.png'),
    RED('RED', 'red_alarm.png'),
    BELOW_THRESHOLD('BELOW_THRESHOLD', 'red_alarm.png'),
    ABOVE_THRESHOLD('ABOVE_THRESHOLD', 'red_alarm.png')
	
	private final String value
    private final String icon
	
	Severity(String value, String icon) {
		this.value = value
        this.icon = icon
	}

	String value() {
		value
	}

    String icon() {
        icon
    }

    static Severity find(String name) {
        if (GREEN.value().equalsIgnoreCase(name)) {
            GREEN
        } else if (YELLOW.value().equalsIgnoreCase(name)) {
            YELLOW
        } else if (RED.value().equalsIgnoreCase(name)) {
            RED
        } else if (BELOW_THRESHOLD.value().equalsIgnoreCase(name)) {
            BELOW_THRESHOLD
        } else if (ABOVE_THRESHOLD.value().equalsIgnoreCase(name)) {
            ABOVE_THRESHOLD
		} else if (BLUE.value().equalsIgnoreCase(name)) {
			BLUE
        } else {
            NONE
        }  
    }
}
