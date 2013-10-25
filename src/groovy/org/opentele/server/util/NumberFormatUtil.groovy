package org.opentele.server.util

import org.opentele.server.model.types.MeasurementTypeNameVisitor
import org.opentele.server.questionnaire.MeasurementResult

import java.text.DecimalFormat
import org.opentele.server.model.Measurement
import java.text.DecimalFormatSymbols
import org.opentele.server.model.types.MeasurementTypeName

class NumberFormatUtil {
    private final DecimalFormat weightFormatter
    private final DecimalFormat bloodPressureFormatter
    private final DecimalFormat pulseFormatter
    private final DecimalFormat saturationFormatter
    private final DecimalFormat defaultFormatter
    private final DecimalFormat defaultFormatterForEditing
    private final DecimalFormat kommaDecimalFormatter

    private final String weightFormat = "0.0"
    private final String bloodPressureFormat = "0"
    private final String pulseFormat = "0"
    private final String saturationFormat = "0"
    private final String defaultFormat = "0.00"
    private final String commaDecimalFormat = "#.##"

    private static final NumberFormatUtil instance = new NumberFormatUtil()

    private static class MeasurementResultFormatter implements MeasurementTypeNameVisitor {
        String result
        MeasurementResult measurement

        MeasurementResultFormatter(MeasurementResult measurement) {
            this.measurement = measurement
        }

        @Override
        void visitBloodPressure() {
            def (systolic, diastolic, pulse) = (measurement.value as String).split(",") as List
            def systolicString = instance.bloodPressureFormatter.format(systolic as Double)
            def diastolicString = instance.bloodPressureFormatter.format(diastolic as Double)

            if(pulse) {
                def pulseString = instance.pulseFormatter.format(pulse as Double)
                result = "${systolicString}/${diastolicString}, ${pulseString}"
            } else {
                result = "${systolicString}/${diastolicString}"
            }
        }

        @Override
        void visitCtg() {
            result = (measurement?.exported ? 'CTG.' : 'CTG')
        }

        @Override
        void visitTemperature() {
            result = formatDouble(measurement.value)
        }

        @Override
        void visitUrine() {
            result = measurement.value
        }

        @Override
        void visitUrineGlucose() {
            result = measurement.value
        }

        @Override
        void visitPulse() {
            result = instance.pulseFormatter.format(measurement.value)
        }

        @Override
        void visitWeight() {
            result = instance.weightFormatter.format(measurement.value)
        }

        @Override
        void visitHemoglobin() {
            result = formatDouble(measurement.value)
        }

        @Override
        void visitSaturation() {
            def (sat, pulse) = (measurement.value as String).split(",") as List
            if(pulse) {
                result = instance.saturationFormatter.format(sat as Double) + "/" + instance.saturationFormatter.format(pulse as Double)
            } else {
                result = instance.saturationFormatter.format(sat as Double)
            }
        }

        @Override
        void visitCrp() {
            result = measurement.value
        }

        @Override
        void visitBloodSugar() {
            result = (measurement.value as String).split(",").collect{formatDouble(it as Double)}.join(" ")
        }

        @Override
        void visitLungFunction() {
            result = formatDouble(measurement.value)
        }
    }

    private static class MeasurementFormatter implements MeasurementTypeNameVisitor {
        String result
        Measurement measurement

        MeasurementFormatter(Measurement measurement) {
            this.measurement = measurement
        }

        @Override
        void visitBloodPressure() {
            def systolic = instance.bloodPressureFormatter.format(measurement.systolic)
            def diastolic = instance.bloodPressureFormatter.format(measurement.diastolic)
            result = "${systolic}/${diastolic}"
        }

        @Override
        void visitCtg() {
            result = measurement.fhr.replaceAll("\\[","").replaceAll("\\]","").tokenize(",").toList().size().toString()
        }

        @Override
        void visitTemperature() {
            result = formatDouble(measurement.value)
        }

        @Override
        void visitUrine() {
            result = measurement.protein?.value
        }

        @Override
        void visitUrineGlucose() {
            result = measurement.glucoseInUrine?.value
        }

        @Override
        void visitPulse() {
            result = instance.pulseFormatter.format(measurement.value)
        }

        @Override
        void visitWeight() {
            result = instance.weightFormatter.format(measurement.value)
        }

        @Override
        void visitHemoglobin() {
            result = formatDouble(measurement.value)
        }

        @Override
        void visitSaturation() {
            result = instance.saturationFormatter.format(measurement.value)
        }

        @Override
        void visitCrp() {
            result = measurement.value as Integer
        }

        @Override
        void visitBloodSugar() {
            result = formatDouble(measurement.value)
        }

        @Override
        void visitLungFunction() {
            result = formatDouble(measurement.value)
        }
    }

    private NumberFormatUtil() {
        Locale currentLocale = Locale.getDefault()
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(currentLocale);
        otherSymbols.setDecimalSeparator(','.toCharacter().charValue());

        weightFormatter = new DecimalFormat(weightFormat, otherSymbols);
        weightFormatter.setGroupingUsed(false)
        
        bloodPressureFormatter = new DecimalFormat(bloodPressureFormat,otherSymbols)
        bloodPressureFormatter.setGroupingUsed(false)
        
        pulseFormatter = new DecimalFormat(pulseFormat,otherSymbols)
        pulseFormatter.setGroupingUsed(false)
        
        saturationFormatter = new DecimalFormat(saturationFormat,otherSymbols)
        saturationFormatter.setGroupingUsed(false)
        
        defaultFormatter = new DecimalFormat(defaultFormat,otherSymbols)
        defaultFormatter.setGroupingUsed(false)
        
        defaultFormatterForEditing = new DecimalFormat()
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(','.toCharacter().charValue())
        
        defaultFormatterForEditing.setDecimalFormatSymbols(symbols);
        defaultFormatterForEditing.setGroupingUsed(false)

        kommaDecimalFormatter = new DecimalFormat(commaDecimalFormat)
        DecimalFormatSymbols decimalSeparator = new DecimalFormatSymbols()
        decimalSeparator.setDecimalSeparator((char)',')
        kommaDecimalFormatter.setDecimalFormatSymbols(decimalSeparator)
    }

    public static Number parse(String s) {
        instance.defaultFormatterForEditing.parse(s)
    }
    
    public static String formatFloat(Float f) {
        f == null ? null : instance.defaultFormatterForEditing.format(f)
    }

    public static String formatDouble(Double d) {
        d == null ? null : instance.defaultFormatterForEditing.format(d)
    }

    public static Float parseFloatWithCommaOrPeriod(String s) {
        s.contains(",") ? parseFloatWithComma(s) : Float.parseFloat(s)
    }
    public static Float parseFloatWithComma(String s) {
        s ? instance.kommaDecimalFormatter.parse(s).floatValue() : null
    }

    public static String format(Measurement measurement) {
        MeasurementFormatter formatter = new MeasurementFormatter(measurement)
        measurement.measurementType.name.visit(formatter)
        formatter.result
    }

    public static String formatMeasurementResult(MeasurementResult measurement, MeasurementTypeName type) {
        MeasurementResultFormatter formatter = new MeasurementResultFormatter(measurement)
        type.visit(formatter)
        formatter.result
    }
}
