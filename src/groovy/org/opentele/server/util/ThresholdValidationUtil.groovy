package org.opentele.server.util

import org.opentele.server.model.types.GlucoseInUrineValue
import org.opentele.server.model.types.ProteinValue

class ThresholdValidationUtil {

    def grailsApplication

    public static validateAlertHigh(val, obj, curry) {
        validateThreshold(val, obj, ThresholdLevel.ALERT_HIGH, curry)
    }

    public static validateWarningHigh(val, obj, curry) {
        validateThreshold(val, obj, ThresholdLevel.WARNING_HIGH, curry)
    }

    public static validateWarningLow(val, obj, curry) {
        validateThreshold(val, obj, ThresholdLevel.WARNING_LOW, curry)
    }

    public static validateAlertLow(val, obj, curry) {
        validateThreshold(val, obj, ThresholdLevel.ALERT_LOW, curry)
    }

    public static boolean validateFloatInput(val, obj, property) {
        return validateInput(val, obj, ThresholdType.FLOAT, property)
    }

    public static boolean validateProtienInput(val, obj, property) {
        return validateInput(val, obj, ThresholdType.PROTEIN_VALUE, property)
    }
    public static boolean validateUrineGlucoseInput(val, obj, property) {
        return validateInput(val, obj, ThresholdType.URINE_GLUCOSE_VALUE, property)
    }


    private static validateThreshold(val, obj, ThresholdLevel level, valueOf) {
        def res = true

        switch (level) {
            case ThresholdLevel.ALERT_HIGH:
                if (val != null && valueOf("warningHigh") != null) {
                    res = val > valueOf("warningHigh") ? true : obj.errors.reject("validate.threshold.interval.edit.error2", ["Rød høj", "Gul høj"] as Object[], "i18n missing")
                } else if (val != null && valueOf("warningLow") != null) {
                    res = val > valueOf("warningLow") ? true : obj.errors.reject("validate.threshold.interval.edit.error2", ["Rød høj", "Gul lav"] as Object[], "i18n missing")
                } else if (val != null && valueOf("alertLow") != null) {
                    res = val > valueOf("alertLow") ? true : obj.errors.reject("validate.threshold.interval.edit.error2", ["Rød høj", "Rød lav"] as Object[], "i18n missing")
                }
                break;
            case ThresholdLevel.WARNING_HIGH:
                if (val != null && valueOf("warningLow") != null) {
                    res = val > valueOf("warningLow") ? true : obj.errors.reject("validate.threshold.interval.edit.error", ["Gul høj", "Gul lav", "Rød høj"] as Object[], "i18n missing")
                } else if (val != null && valueOf("alertLow") != null) {
                    res = val > valueOf("alertLow") ? true : obj.errors.reject("validate.threshold.interval.edit.error2", ["Gul høj", "Rød lav"] as Object[], "i18n missing")
                }

                if (val != null && valueOf("alertHigh") != null) {
                    res = res && val < valueOf("alertHigh") ? true : obj.errors.reject("validate.threshold.interval.edit.error", ["Gul høj", "Gul lav", "Rød høj"] as Object[], "i18n missing")
                }
                break;
            case ThresholdLevel.WARNING_LOW:
                if (val != null && valueOf("alertLow") != null) {
                    res = val > valueOf("alertLow") ? true : obj.errors.reject("validate.threshold.interval.edit.error2", ["Gul lav", "Rød lav"] as Object[], "i18n missing")
                }

                if (val != null && valueOf("warningHigh") != null) {
                    res = res && val < valueOf("warningHigh") ? true : obj.errors.reject("validate.threshold.interval.edit.error3", ["Gul lav", "Gul høj"] as Object[], "i18n missing")
                } else if (val != null && valueOf("alertHigh") != null) {
                    res = res && val < valueOf("alertHigh") ? true : obj.errors.reject("validate.threshold.interval.edit.error3", ["Gul lav", "Rød høj"] as Object[], "i18n missing")
                }
                break;
            case ThresholdLevel.ALERT_LOW:
                if (val != null && valueOf("warningLow") != null) {
                    res = val < valueOf("warningLow") ? true : obj.errors.reject("validate.threshold.interval.edit.error3", ["Rød lav", "Gul lav"] as Object[], "i18n missing")
                } else if (val != null && valueOf("warningHigh") != null) {
                    res = val < valueOf("warningHigh") ? true : obj.errors.reject("validate.threshold.interval.edit.error3", ["Rød lav", "Gul høj"] as Object[], "i18n missing")
                } else if (val != null && valueOf("alertHigh") != null) {
                    res = val < valueOf("alertHigh") ? true : obj.errors.reject("validate.threshold.interval.edit.error3", ["Rød lav", "Røj høj"] as Object[], "i18n missing")
                }

                break;
        }
        return res
    }

    private static boolean validateInput(val, obj, type, property) {
        def propertyValue

        if (val != null && val != "") {
            try {
                switch(type) {
                    case ThresholdType.FLOAT:
                        propertyValue = NumberFormatUtil.parseFloatWithCommaOrPeriod(val);
                        break;
                    case ThresholdType.PROTEIN_VALUE:
                        propertyValue = ProteinValue.fromString(val);
                        break;
                    case ThresholdType.URINE_GLUCOSE_VALUE:
                        propertyValue = GlucoseInUrineValue.fromString(val)
                        break;
                }
            } catch (Exception e) { //Pokémon-catch
                return false
            }
        } else {
            propertyValue = null
        }

        //Set the casted property-value on the threshold obj
        obj.properties."$property" = propertyValue

        return true
    }

    static enum ThresholdLevel {ALERT_HIGH, WARNING_HIGH, WARNING_LOW, ALERT_LOW}
    static enum ThresholdType {FLOAT, PROTEIN_VALUE, URINE_GLUCOSE_VALUE}

    static thresholdValueProxy = { obj ->
        return {value -> obj."$value"}
    }

    static bloodPressureThresholdValueProxy = { obj ->
        return {type ->
            return {value ->
                //Need to change capitalization to match camelCase
                def cValue = value.capitalize()
                obj."$type$cValue"
            }
        }
    }


}


