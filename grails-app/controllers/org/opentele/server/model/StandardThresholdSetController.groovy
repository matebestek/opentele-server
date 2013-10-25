package org.opentele.server.model
import grails.plugins.springsecurity.Secured
import org.opentele.server.annotations.SecurityWhiteListController
import org.opentele.server.model.types.PermissionName
import org.opentele.server.util.ThresholdValidationUtil
import org.springframework.dao.DataIntegrityViolationException

import static org.opentele.server.model.types.MeasurementTypeName.*

@Secured(PermissionName.NONE)
@SecurityWhiteListController
class StandardThresholdSetController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST", addThreshold: "POST"]

    def sessionService

    @Secured(PermissionName.STANDARD_THRESHOLD_READ_ALL)
    def index() {
        redirect(action: "list", params: params)
    }

    @Secured(PermissionName.STANDARD_THRESHOLD_READ_ALL)
    @SecurityWhiteListController
    def list() {
        def list = StandardThresholdSet.list(fetch: [thresholds: "eager"])
        [standardThresholdSetInstanceList: list]
    }

    @Secured(PermissionName.STANDARD_THRESHOLD_CREATE)
    def save() {
        def standardThresholdSetInstance = new StandardThresholdSet(params)
        if (!standardThresholdSetInstance.save(flush: true)) {
            render(view: "create", model: [standardThresholdSetInstance: standardThresholdSetInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'standardThresholdSet.label', default: 'StandardThresholdSet'), standardThresholdSetInstance.id])
        redirect(action: "list")
    }

    @Secured(PermissionName.STANDARD_THRESHOLD_WRITE)
    def edit() {
        sessionService.setAccessTokens(session)
        def standardThresholdSetInstance = StandardThresholdSet.get(params.id)
        if (!standardThresholdSetInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'standardThresholdSet.label', default: 'StandardThresholdSet'), params.id])
            redirect(action: "list")
            return
        }

        [
            standardThresholdSetInstance: standardThresholdSetInstance,
            patientGroup: standardThresholdSetInstance.patientGroup
        ]
    }

    @Secured(PermissionName.STANDARD_THRESHOLD_WRITE)
    def update() {
        def standardThresholdSetInstance = StandardThresholdSet.get(params.id)
        if (!standardThresholdSetInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'standardThresholdSet.label', default: 'StandardThresholdSet'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (standardThresholdSetInstance.version > version) {
                standardThresholdSetInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'standardThresholdSet.label', default: 'StandardThresholdSet')] as Object[],
                        "Another user has updated this StandardThresholdSet while you were editing")
                render(view: "edit", model: [standardThresholdSetInstance: standardThresholdSetInstance])
                return
            }
        }

        standardThresholdSetInstance.properties = params

        if (!standardThresholdSetInstance.save(flush: true)) {
            render(view: "edit", model: [standardThresholdSetInstance: standardThresholdSetInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'standardThresholdSet.label', default: 'StandardThresholdSet'), standardThresholdSetInstance.id])
        redirect(action: "list")
    }

    @Secured(PermissionName.STANDARD_THRESHOLD_WRITE)
    def addThreshold() {
        sessionService.setAccessTokens(session)
        def standardThresholdSetInstance = StandardThresholdSet.get(params.id)
        Threshold t
        if (params.thresholdtype == BLOOD_PRESSURE.toString()) {
            t = new BloodPressureThreshold()
        } else if (params.thresholdtype == URINE.toString()) {
            t = new UrineThreshold()
        } else if (params.thresholdtype == URINE_GLUCOSE.toString()) {
            t = new UrineGlucoseThreshold()
        } else {
            t = new NumericThreshold()
        }
        render(view: "create", model: [
                standardThresholdSetInstance: standardThresholdSetInstance,
                standardThresholdInstance: t,
                thresholdType: params.thresholdtype,
                notUsedThresholds: getUnusedThresholds(standardThresholdSetInstance),
                patientGroup: standardThresholdSetInstance.patientGroup
        ])
    }

    @Secured(PermissionName.STANDARD_THRESHOLD_WRITE)
    @SecurityWhiteListController
    def chooseThreshold(String type) {
        log.debug("ChooseThreshold $type")
        switch (type) {
            case BLOOD_PRESSURE.name():
                render(template: '/bloodPressureThreshold/form', model: [standardThresholdInstance: new BloodPressureThreshold()])
                break
            case URINE.name():
                render(template: '/urineThreshold/form', model: [standardThresholdInstance: new UrineThreshold()])
                break
            case URINE_GLUCOSE.name():
                render(template: '/urineGlucoseThreshold/form', model: [standardThresholdInstance: new UrineGlucoseThreshold()])
                break
            default:
                render(template: '/numericThreshold/form', model: [standardThresholdInstance: new NumericThreshold()])
        }
    }

    private getUnusedThresholds(StandardThresholdSet standardThresholdSet) {
        def currentMeasurementTypes = standardThresholdSet.thresholds*.type*.name

        def list = (currentMeasurementTypes.size() < 1 ? MeasurementType.list() : MeasurementType.withCriteria {
            not {
                inList('name', currentMeasurementTypes)
            }
        })*.name.sort { it.name() }
        list
    }

    @Secured(PermissionName.STANDARD_THRESHOLD_DELETE)
    def removeThreshold(Long id, Long threshold) {
        def standardThresholdSetInstance = StandardThresholdSet.get(id)
        def thresholdInstance = Threshold.get(threshold)

        standardThresholdSetInstance.removeFromThresholds(thresholdInstance)
        standardThresholdSetInstance.save()
        thresholdInstance.delete()
        redirect(action: "list", fragment: "${standardThresholdSetInstance.id}")

    }

    @Secured(PermissionName.STANDARD_THRESHOLD_WRITE)
    def saveThresholdToSet(Long id) {
        def standardThresholdSetInstance = StandardThresholdSet.get(id)
        Threshold t
        MeasurementType measurementType = MeasurementType.findByName(params.type)

        def propertiesBloodPressure = ['diastolicAlertHigh', 'diastolicAlertLow', 'diastolicWarningHigh', 'diastolicWarningLow','systolicAlertHigh', 'systolicAlertLow', 'systolicWarningHigh', 'systolicWarningLow']
        def propertiesEverythingElse = ['alertHigh', 'alertLow', 'warningHigh', 'warningLow']

        switch(measurementType.name) {
            case BLOOD_PRESSURE:
                t = new BloodPressureThreshold()

                propertiesBloodPressure.each {
                    ThresholdValidationUtil.validateFloatInput(params."${it}", t, it)
                }

                break;
            case URINE:
                t = new UrineThreshold()

                propertiesEverythingElse.each {
                    ThresholdValidationUtil.validateProtienInput(params."${it}", t, it)
                }
                break;
            case URINE_GLUCOSE:
                t = new UrineGlucoseThreshold()

                propertiesEverythingElse.each {
                    ThresholdValidationUtil.validateUrineGlucoseInput(params."${it}", t, it)
                }
                break;
            default: //All numeric types, excluding BP
                t = new NumericThreshold()

                propertiesEverythingElse.each {
                    ThresholdValidationUtil.validateFloatInput(params."${it}", t, it)
                }

                break;
        }

        t.type = measurementType

        if (t.hasErrors() || !t.save(flush: true)) {
            render(view:  "create", model: [standardThresholdSetInstance: standardThresholdSetInstance, standardThresholdInstance: t, thresholdType: measurementType.name, notUsedThresholds: getUnusedThresholds(standardThresholdSetInstance), patientGroup: standardThresholdSetInstance.patientGroup])
            return
        }

        standardThresholdSetInstance.addToThresholds(t)
        if(!standardThresholdSetInstance.validate()) {
            standardThresholdSetInstance.removeFromThresholds(t)
            standardThresholdSetInstance.clearErrors()
            standardThresholdSetInstance.errors.reject('standardThresholdSet.add.error', [t.prettyToString(), standardThresholdSetInstance.patientGroup.name] as Object[], 'Kunne ikke tilføje {0} til gruppen {1}: Der findes allerede en tærskelværdi af denne type for denne gruppe.')
            render(view: "list", model:  [standardThresholdSetInstance: standardThresholdSetInstance, standardThresholdSetInstanceList: StandardThresholdSet.findAll()])
        } else {
            standardThresholdSetInstance.save(flush: true)
            redirect(action:  "list", fragment: "${standardThresholdSetInstance.id}")
        }
    }

    /*
    delete() will only empty the group, not remove it
     */
    @Secured(PermissionName.STANDARD_THRESHOLD_DELETE)
    def delete() {
        def standardThresholdSetInstance = StandardThresholdSet.get(params.id)
        if (!standardThresholdSetInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'standardThresholdSet.label', default: 'StandardThresholdSet'), params.id])
            redirect(action: "list")
            return
        }
        try {
            standardThresholdSetInstance.thresholds.clear()

            flash.message = message(code: 'default.deleted.message', args: [message(code: 'standardThresholdSet.label', default: 'StandardThresholdSet'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'standardThresholdSet.label', default: 'StandardThresholdSet'), params.id])
            redirect(action: "list")
        }
    }
}
