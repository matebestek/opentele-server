package org.opentele.server.model

import grails.validation.Validateable
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.opentele.server.model.types.GlucoseInUrineValue
import org.opentele.server.model.types.MeasurementTypeName
import org.opentele.server.model.types.ProteinValue
import org.opentele.server.model.types.Sex
import org.opentele.server.util.NumberFormatUtil
import org.opentele.server.util.PasswordUtil

@Validateable
//Provides hasErrors and validate()
class CreatePatientCommand extends AbstractObject {

    //Attributes from basicInfo params
    String firstName
    String lastName
    String cpr
    Sex sex
    String address
    String postalCode
    String city
    String phone
    String mobilePhone
    String email

    String username
    String cleartextPassword

    List<String> groupIds
    Set<Threshold> thresholds
    List<NextOfKinPerson> nextOfKins

    String comment
    boolean thresholdSetWasReduced

    def setBasicInformation(def params) {
        firstName = params.firstName
        lastName = params.lastName
        cpr = params.cpr
        sex = params.sex != null ? Sex.valueOf(params.sex) : null
        address = params.address
        postalCode = params.postalCode
        city = params.city
        phone = params.phone
        mobilePhone = params.mobilePhone
        email = params.email

        //Fix CPR if needed
        cpr = cpr?.replaceAll(" ", "")
        cpr = cpr?.replaceAll("-", "")
    }

    def setAuthentication(def username, def cleartextPassword) {
        this.username = username
        this.cleartextPassword = cleartextPassword
    }

    def setPatientGroups(String[] groupIds) {
        this.groupIds = []
        this.thresholds = [] as Set<Threshold>

        this.groupIds.addAll(groupIds as List)

        //Find the standard thresholds belonging to all (1-n) patient groups

        List<Threshold> standardThresholds = PatientGroup.findAll().grep {
            this.groupIds.contains(it.id as String)
        }.collectMany { pgs -> pgs.standardThresholdSet.thresholds }

        def initialNumberOfStandardThresholds = standardThresholds.size()

        //Initialize patient thresholds from standardThresholdSets
        standardThresholds.unique({ a, b -> a.type.toString() <=> b.type.toString() }).each { threshold ->
            this.thresholds << threshold.duplicate()
        }

        this.thresholdSetWasReduced = standardThresholds.size() < initialNumberOfStandardThresholds
    }

    def updateThresholds(GrailsParameterMap params) {
        //It is not possible to add more thresholds to the patient during the flow
        //thus we can assume only the thresholds in this.thresholds need to be updated
        this.thresholds.each { Threshold threshold ->
            switch (threshold.type.name) {
                case MeasurementTypeName.BLOOD_PRESSURE:
                    def newThreshold = params[MeasurementTypeName.BLOOD_PRESSURE.toString()]
                    if (newThreshold) {
                        threshold.properties = newThreshold
                    }
                    break;
                case MeasurementTypeName.URINE:
                    def newThreshold = params[MeasurementTypeName.URINE.toString()]
                    if (newThreshold) {
                        threshold.alertHigh = parseUrineThreshold(newThreshold.alertHigh, "alertHigh", threshold)
                        threshold.warningHigh = parseUrineThreshold(newThreshold.warningHigh, "warningHigh", threshold)
                        threshold.warningLow = parseUrineThreshold(newThreshold.warningLow, "warningLow", threshold)
                        threshold.alertLow = parseUrineThreshold(newThreshold.alertLow, "alertLow", threshold)
                    }
                    break;

                case MeasurementTypeName.URINE_GLUCOSE:
                    def newThreshold = params[MeasurementTypeName.URINE_GLUCOSE.toString()]
                    if (newThreshold) {
                        threshold.alertHigh = parseUrineGlucoseThreshold(newThreshold.alertHigh, "alertHigh", threshold)
                        threshold.warningHigh = parseUrineGlucoseThreshold(newThreshold.warningHigh, "warningHigh", threshold)
                        threshold.warningLow = parseUrineGlucoseThreshold(newThreshold.warningLow, "warningLow", threshold)
                        threshold.alertLow = parseUrineGlucoseThreshold(newThreshold.alertLow, "alertLow", threshold)
                    }
                    break;
                default: //Is Numeric
                    def newThreshold = params[threshold.type.name.toString()]
                    if (newThreshold) {
                        threshold.properties = newThreshold
                    }
                    break;
            }
            threshold.validate()
        }
    }

    static constraints = {
        //Step 1
        importFrom(Patient)

        firstName(blank: false)
        lastName(blank: false)
        sex(blank: false)
        address(blank: false)
        postalCode(blank: false)
        city(blank: false)

        cpr(validator: { val, obj ->
            def similarPatient = Patient.findAllByCpr(val)
            if (similarPatient && similarPatient.size() > 0) {
                ["validate.patient.cpr.exists"]
            } else if (val?.length() != 10) {
                ["validate.patient.cpr.length", "CPR"]
            } else if (!val) {
                ["validate.patient.default.blank", "CPR"]
            } else {
                return true
            }
        })

        //Step 2
        username nullable: false, blank: false, maxSize: 128, validator: { value ->
            if (User.findByUsername(value)) {
                return "not.unique"
            }
        }
        cleartextPassword(blank: false, validator: PasswordUtil.passwordValidator)

        //Step 3
        groupIds(validator: { val, obj ->
            if (!val || val.size() < 1) {
                ["validate.patient.nogroupselected"]
            }
        })

        //Step 4
        //Validated by domain classes

        //Step 5
        //Already imported from Patient

        //Step 6
        //Validated by domain class
    }

    def setComment(def comment) {
        this.comment = comment
    }

    //Validating from here
    def addNextOfKinPerson(def nextOfKinPerson) {
        if (!this.nextOfKins) {
            this.nextOfKins = new ArrayList<NextOfKinPerson>()
        }
        nextOfKins << nextOfKinPerson
    }

    //Needed for summary state
    public Threshold getThreshold(MeasurementType type) {
        return getThreshold(type.name)
    }

    public Threshold getThreshold(MeasurementTypeName typeName) {
        return thresholds.find { it.type.name.equals(typeName) }
    }


    private ProteinValue parseUrineThreshold(def newValue, def type, Threshold threshold) {
        if (newValue == null || newValue == "") {
            return null
        } else {
            try {
                return ProteinValue.fromString(newValue)
            } catch (IllegalArgumentException e) {
                threshold.errors.reject("default.urineThreshold.edit.error", [type] as Object[], 'i18n missing')
                return threshold."${type}"
            }
        }
    }

    private GlucoseInUrineValue parseUrineGlucoseThreshold(def newValue, def type, Threshold threshold) {
        if (newValue == null || newValue == "") {
            return null
        } else {
            try {
                return GlucoseInUrineValue.fromString(newValue)
            } catch (IllegalArgumentException e) {
                threshold.errors.reject("default.urineGlucoseThreshold.edit.error", [type] as Object[])
                return threshold."${type}"
            }
        }
    }

    private Float parseNumericThreshold(def newValue, def type, Threshold threshold) {
        if (newValue == null || newValue == "") {
            return null
        } else {

            try {
                return NumberFormatUtil.parseFloatWithCommaOrPeriod(newValue)
            } catch (NumberFormatException e) {
                threshold.errors.reject("default.threshold.edit.error", [type] as Object[])
                return threshold."${type}"
            }

        }
    }
}
