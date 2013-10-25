package org.opentele.server.model

import grails.plugins.springsecurity.Secured
import org.opentele.server.annotations.SecurityWhiteListController
import org.opentele.server.model.types.PermissionName
import org.opentele.server.util.ThresholdValidationUtil

@Secured(PermissionName.NONE)
class BloodPressureThresholdController {
    @Secured(PermissionName.THRESHOLD_READ)
    @SecurityWhiteListController
    def show() {
        def standardThresholdInstance = BloodPressureThreshold.get(params.id)
        if (!standardThresholdInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bloodPressureThreshold.label', default: 'BloodPressureThreshold'), params.id])
            redirect(action: "list")
            return
        }

        [standardThresholdInstance: standardThresholdInstance]
    }


    @Secured(PermissionName.THRESHOLD_WRITE)
    @SecurityWhiteListController
    def edit() {
        def standardThresholdInstance = BloodPressureThreshold.get(params.id)
        if (!standardThresholdInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bloodPressureThreshold.label', default: 'BloodPressureThreshold'), params.id])
            redirect(action: session.lastAction, controller: session.lastController)
            return
        }

        def standardThresholdSet = StandardThresholdSet.forThreshold(standardThresholdInstance)

        [
            standardThresholdInstance: standardThresholdInstance,
            patientGroup: standardThresholdSet?.patientGroup
        ]
    }

    @Secured(PermissionName.THRESHOLD_WRITE)
    @SecurityWhiteListController
    def update() {
        def standardThresholdInstance = BloodPressureThreshold.get(params.id)
        if (!standardThresholdInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bloodPressureThreshold.label', default: 'BloodPressureThreshold'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (standardThresholdInstance.version > version) {
                standardThresholdInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'bloodPressureThreshold.label', default: 'BloodPressureThreshold')] as Object[],
                        "Another user has updated this BloodPressureThreshold while you were editing")
                render(view: "edit", model: [standardThresholdInstance: standardThresholdInstance])
                return
            }
        }

        if(!ThresholdValidationUtil.validateFloatInput(params.diastolicAlertHigh, standardThresholdInstance, "diastolicAlertHigh")) {
            standardThresholdInstance.errors.reject("default.threshold.edit.error",
                    [message(code: "default.threshold.diastolicAlertHigh.label")] as Object[],
                    "i18n missing")
        }
        if(!ThresholdValidationUtil.validateFloatInput(params.diastolicWarningHigh, standardThresholdInstance, "diastolicWarningHigh")) {
            standardThresholdInstance.errors.reject("default.threshold.edit.error",
                    [message(code: "default.threshold.diastolicWarningHigh.label")] as Object[],
                    "i18n missing")
        }
        if (!ThresholdValidationUtil.validateFloatInput(params.diastolicWarningLow, standardThresholdInstance, "diastolicWarningLow")) {
            standardThresholdInstance.errors.reject("default.threshold.edit.error",
                    [message(code: "default.threshold.diastolicWarningLow.label")] as Object[],
                    "i18n missing")
        }
        if (!ThresholdValidationUtil.validateFloatInput(params.diastolicAlertLow, standardThresholdInstance, "diastolicAlertLow")) {
            standardThresholdInstance.errors.reject("default.threshold.edit.error",
                    [message(code: "default.threshold.diastolicAlertLow.label")] as Object[],
                    "i18n missing")
        }

        if(!ThresholdValidationUtil.validateFloatInput(params.systolicAlertHigh, standardThresholdInstance, "systolicAlertHigh")) {
            standardThresholdInstance.errors.reject("default.threshold.edit.error",
                    [message(code: "default.threshold.systolicAlertHigh.label")] as Object[],
                    "i18n missing")
        }
        if(!ThresholdValidationUtil.validateFloatInput(params.systolicWarningHigh, standardThresholdInstance, "systolicWarningHigh")) {
            standardThresholdInstance.errors.reject("default.threshold.edit.error",
                    [message(code: "default.threshold.systolicWarningHigh.label")] as Object[],
                    "i18n missing")
        }
        if (!ThresholdValidationUtil.validateFloatInput(params.systolicWarningLow, standardThresholdInstance, "systolicWarningLow")) {
            standardThresholdInstance.errors.reject("default.threshold.edit.error",
                    [message(code: "default.threshold.systolicWarningLow.label")] as Object[],
                    "i18n missing")
        }
        if (!ThresholdValidationUtil.validateFloatInput(params.systolicAlertLow, standardThresholdInstance, "systolicAlertLow")) {
            standardThresholdInstance.errors.reject("default.threshold.edit.error",
                    [message(code: "default.threshold.systolicAlertLow.label")] as Object[],
                    "i18n missing")
        }

        if(!standardThresholdInstance.hasErrors()) {
            if (!standardThresholdInstance.save(flush: true)) {
                render(view: "edit", model: [standardThresholdInstance: standardThresholdInstance])
                return
            }

            flash.message = message(code: 'default.updated.message', args: [message(code: 'bloodPressureThreshold.label', default: 'BloodPressureThreshold'), standardThresholdInstance.id])
            redirect(action: session.lastAction, controller: session.lastController, params: session.lastParams)
        } else {
            render(view: "edit", model: [standardThresholdInstance: standardThresholdInstance])
        }
    }
}