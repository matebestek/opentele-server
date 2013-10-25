package org.opentele.server.model

import grails.plugins.springsecurity.Secured
import org.opentele.server.annotations.SecurityWhiteListController
import org.opentele.server.model.types.PermissionName
import org.opentele.server.util.ThresholdValidationUtil

@Secured(PermissionName.NONE)
class UrineGlucoseThresholdController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	/**
	 * The controller actions create(), delete() and save() have been removed since
	 * those actions should not be possible at runtime; the standard thresholds match
	 * (by name) the threshold values defined in the Patient domain object, and are
	 * added in bootstrap. Thresholds cannot be added to the system (i.e. Patient
	 * object) without changing the patient class thus having to re-deploy.
	 */

    @Secured(PermissionName.THRESHOLD_READ)
    @SecurityWhiteListController
	def show() {
		def standardThresholdInstance = UrineGlucoseThreshold.get(params.id)
		if (!standardThresholdInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'standardThreshold.label', default: 'StandardThreshold'), params.id])
			redirect(action: "list")
			return
		}

		[standardThresholdInstance: standardThresholdInstance]
	}

    @Secured(PermissionName.THRESHOLD_WRITE)
    @SecurityWhiteListController
	def edit() {
		def standardThresholdInstance = UrineGlucoseThreshold.get(params.id)
		if (!standardThresholdInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'standardThreshold.label', default: 'StandardThreshold'), params.id])
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
		def standardThresholdInstance = UrineGlucoseThreshold.get(params.id)
		if (!standardThresholdInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'standardThreshold.label', default: 'StandardThreshold'), params.id])
			redirect(action: "list")
			return
		}

		if (params.version) {
			def version = params.version.toLong()
			if (standardThresholdInstance.version > version) {
				standardThresholdInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						  [message(code: 'standardThreshold.label', default: 'StandardThreshold')] as Object[],
						  "Another user has updated this StandardThreshold while you were editing")
				render(view: "edit", model: [standardThresholdInstance: standardThresholdInstance])
				return
			}
		}

        if(!ThresholdValidationUtil.validateUrineGlucoseInput(params.alertHigh, standardThresholdInstance, "alertHigh")) {
            standardThresholdInstance.errors.reject("default.urineGlucoseThreshold.edit.error",
                    [message(code: "default.threshold.alertHigh.label")] as Object[],
                    "i18n missing")
        }
        if(!ThresholdValidationUtil.validateUrineGlucoseInput(params.warningHigh, standardThresholdInstance, "warningHigh")) {
            standardThresholdInstance.errors.reject("default.urineGlucoseThreshold.edit.error",
                    [message(code: "default.threshold.warningHigh.label")] as Object[],
                    "i18n missing")
        }
        if (!ThresholdValidationUtil.validateUrineGlucoseInput(params.warningLow, standardThresholdInstance, "warningLow")) {
            standardThresholdInstance.errors.reject("default.urineGlucoseThreshold.edit.error",
                    [message(code: "default.threshold.warningLow.label")] as Object[],
                    "i18n missing")
        }
        if (!ThresholdValidationUtil.validateUrineGlucoseInput(params.alertLow, standardThresholdInstance, "alertLow")) {
            standardThresholdInstance.errors.reject("default.urineGlucoseThreshold.edit.error",
                    [message(code: "default.threshold.alertLow.label")] as Object[],
                    "i18n missing")
        }

        if(!standardThresholdInstance.hasErrors()) {
            if (!standardThresholdInstance.save(flush: true)) {
                render(view: "edit", model: [standardThresholdInstance: standardThresholdInstance])
                return
            }

            flash.message = message(code: 'default.updated.message', args: [message(code: 'standardThreshold.label', default: 'StandardThreshold'), standardThresholdInstance.id])
            redirect(action: session.lastAction, controller: session.lastController, params: session.lastParams)
        } else {
            render(view: "edit", model: [standardThresholdInstance: standardThresholdInstance])
        }
	}
}
