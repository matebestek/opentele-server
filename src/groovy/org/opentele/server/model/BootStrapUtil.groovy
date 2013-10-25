package org.opentele.server.model

import org.opentele.server.model.types.MeasurementTypeName
import org.opentele.server.model.types.MeterTypeName
import org.opentele.server.model.types.PermissionName
import org.opentele.server.model.types.ProteinValue
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class BootStrapUtil {
    private static final Logger log = LoggerFactory.getLogger(BootStrapUtil.class.name)

    public static enum RoleName {
        ROLE_ADMIN("Administrator"),ROLE_PATIENT("Patient"),ROLE_CLINICIAN("Kliniker"),ROLE_VIDEO_CONSULTANT("Videokonsultør")

        private final String value

        RoleName(String value) {
            this.value = value
        }

        String value() {
            value
        }
    }

	Date now = new Date()

    static void removePermission(String permissionName) {
        def permission = Permission.findByPermission(permissionName)
        if (!permission) {
            return
        }

        Role.findAll().each { role ->
            RolePermission.remove(role, permission, true)
        }
        permission.delete(flush: true)
    }

	void addUserToRoleIfNotExists(User user, Role role) {
		if (!user.authorities.contains(role)) {
    		UserRole.create(user,role)
		}
	}

    Permission createPermissionIfNotExists(String permission) {
        Permission retVal = Permission.findByPermission(permission) ?: new Permission(permission:permission).save(failOnError: true)
        return retVal
    }

	Role setupRoleIfNotExists(String authority) {
		return setupRoleIfNotExists(authority,null)
	}

	Role setupRoleIfNotExists(String authority, Date date) {
        Role retVal = Role.findByAuthority(authority) ?: new Role(authority: authority).save(failOnError:true)
		return retVal
	}

	Department createDepartmentIfNotExists(String name) {

        Department dept = Department.findByName(name)
		if (!dept) {
            dept = new Department(name: name,createdBy: "System", modifiedBy: "System", createdDate: now, modifiedDate: now)
    		dept.save(failOnError:true)
		}
		return dept
	}

	def createPatientGroupIfNotExists(String name, Department dep, Date date, StandardThresholdSet standardThresholdSet) {

        PatientGroup retVal = PatientGroup.findByNameAndDepartment(name, dep)
        if (!retVal) {
    		retVal = new PatientGroup(name: name, department: dep, createdBy: "System", modifiedBy: "System", createdDate: date, modifiedDate: date, standardThresholdSet: standardThresholdSet)
            retVal.standardThresholdSet = standardThresholdSet
            retVal.save(failOnError:true)
        }
        retVal
	}

	def createMeasurementTypeIfNotExists(MeasurementTypeName name, Date date) {
        MeasurementType retVal = MeasurementType.findByName(name)
        if (!retVal) {
            retVal = new MeasurementType(name:name, createdBy: "System", modifiedBy: "System", createdDate: date, modifiedDate: date)
    		retVal.save(failOnError: true)
        }
		retVal
	}

	def createMonitorKit(String name, Department dep, Patient p, Date date) {
        MonitorKit retVal = MonitorKit.findByNameAndDepartment(name, dep)
        if (!retVal) {
    		retVal = new MonitorKit(name:name, department:dep, patient:p, createdBy: "System", modifiedBy: "System", createdDate: date, modifiedDate: date)
    		retVal.save(failOnError: true)
        }
		retVal
	}

    def createScheduleWindowIfNotExists(ScheduleWindow scheduleWindow){
        ScheduleWindow retVal = ScheduleWindow.findByScheduleType(scheduleWindow.scheduleType)
        if (!retVal) {
            scheduleWindow.save(failOnError: true)
        }
    }

	def createMeterTypeIfNotExists(MeterTypeName name){
        MeterType retVal = MeterType.findByName(name)
        if (!retVal) {
            retVal = new MeterType(name:name, createdBy: "System", modifiedBy: "System", createdDate: now, modifiedDate: now)
            retVal.save(failOnError: true)
        }
        retVal
    }

	def createMeter(String model, String meterId, MonitorKit kit, Patient p, MeterType type) {

        Meter m = Meter.findByMeterId(meterId)
        if (!m) {
            m = new Meter(model:model, meterId: meterId, monitorKit:kit,patient:p, meterType: type, active:true, createdBy: "System", modifiedBy: "System", createdDate: now, modifiedDate: now)
    		m.save(failOnError:true)
        }
		m
	}

    def createStandardThresholdSetForPatientGroup(thresholds) {
        StandardThresholdSet st = new StandardThresholdSet()
        st.save()
        st.refresh()
        thresholds.each {type ->
            Threshold t
            switch(type) {
                case 'urine':
                    t = new UrineThreshold(type: MeasurementType.findByName(MeasurementTypeName.URINE),alertHigh:  ProteinValue.PLUS_FOUR, alertLow:  ProteinValue.NEGATIVE, warningHigh:  ProteinValue.PLUS_THREE, warningLow:  ProteinValue.PLUSMINUS )
                    break
                case 'urine_glucose':
                    t = new UrineThreshold(type: MeasurementType.findByName(MeasurementTypeName.URINE_GLUCOSE),alertHigh:  ProteinValue.PLUS_FOUR, alertLow:  ProteinValue.NEGATIVE, warningHigh:  ProteinValue.PLUS_THREE, warningLow:  ProteinValue.PLUSMINUS )
                    break
                case 'temperature':
                    t = new NumericThreshold(type: MeasurementType.findByName(MeasurementTypeName.TEMPERATURE), alertHigh: 39,	warningHigh: 38.5,	warningLow: 36.5, 	alertLow: 36)
                    break
                case 'hemoglobin':
                    t = new NumericThreshold(type: MeasurementType.findByName(MeasurementTypeName.HEMOGLOBIN), 	            alertHigh: 3,	warningHigh: 2,		warningLow: 1, 		alertLow: 0)
                    break
                case 'pulse':
                    t = new NumericThreshold(type: MeasurementType.findByName(MeasurementTypeName.PULSE), 		            alertHigh: 100,	warningHigh: 90,	warningLow: 50, 	alertLow: 30)
                    break
                case 'saturation':
                    t = new NumericThreshold(type: MeasurementType.findByName(MeasurementTypeName.SATURATION), 	            alertHigh: 3,	warningHigh: 2,		warningLow: 1, 		alertLow: 0)
                    break
                case 'weight':
                    t = new NumericThreshold(type: MeasurementType.findByName(MeasurementTypeName.WEIGHT), 		            alertHigh: 100,	warningHigh: 90,	warningLow: 55, 	alertLow: 50)
                    break
                case 'bloodPressure':
                    t = new BloodPressureThreshold(type: MeasurementType.findByName(MeasurementTypeName.BLOOD_PRESSURE),	systolicAlertHigh: 140,	systolicWarningHigh: 120,	systolicWarningLow: 65, 	systolicAlertLow: 50, diastolicAlertHigh: 100, diastolicWarningHigh: 95,	diastolicWarningLow: 70, 	diastolicAlertLow: 60)
                    break
                case 'crp':
                    t = new NumericThreshold(type: MeasurementType.findByName(MeasurementTypeName.CRP),                       alertHigh: 100, warningHigh: 99,    warningLow: 10,     alertLow: 5)
                    break
                default:
                    throw new IllegalArgumentException("Threshold type is unknown: '${type}'")
            }
            t.save(failOnError: true)
            st.thresholds.add(t)
        }
        st.save()
        return st
    }

    RolePermission createRolePermissionIfNotExists(Role r, Permission p) {
        RolePermission retVal = RolePermission.findByRoleAndPermission(r,p) ?: new RolePermission(role:r, permission:p).save(failOnError: true)
        return retVal
    }

    List<RolePermission> setupPermissionsForRole(Role role) {
        List<RolePermission> result = new ArrayList<RolePermission>()
        permissionsForRole(role).each {
            def permission = Permission.findByPermission(it)
            def rolePermission = createRolePermissionIfNotExists(role, permission)
            result << rolePermission
        }

        result
    }

    private def permissionsForRole(Role r) {
        switch (r.authority) {
            case RoleName.ROLE_ADMIN.value():
                return [
                    PermissionName.WEB_LOGIN,
                    PermissionName.QUESTIONNAIRE_READ,
                    PermissionName.QUESTIONNAIRE_READ_ALL,
                    PermissionName.QUESTIONNAIRE_WRITE,
                    PermissionName.QUESTIONNAIRE_CREATE,
                    PermissionName.QUESTIONNAIRE_DELETE,
                    PermissionName.MONITOR_KIT_READ,
                    PermissionName.MONITOR_KIT_READ_ALL,
                    PermissionName.MONITOR_KIT_CREATE,
                    PermissionName.MONITOR_KIT_WRITE,
                    PermissionName.MONITOR_KIT_DELETE,
                    PermissionName.METER_READ,
                    PermissionName.METER_READ_ALL,
                    PermissionName.METER_CREATE,
                    PermissionName.METER_WRITE,
                    PermissionName.METER_DELETE,
                    PermissionName.CLINICIAN_READ,
                    PermissionName.CLINICIAN_READ_ALL,
                    PermissionName.CLINICIAN_WRITE,
                    PermissionName.CLINICIAN_CREATE,
                    PermissionName.CLINICIAN_DELETE,
                    PermissionName.CLINICIAN_CHANGE_PASSWORD,
                    PermissionName.USER_DELETE,
                    PermissionName.USER_WRITE,
                    PermissionName.USER_READ,
                    PermissionName.USER_CREATE,
                    PermissionName.USER_READ_ALL,
                    PermissionName.STANDARD_THRESHOLD_DELETE,
                    PermissionName.STANDARD_THRESHOLD_READ,
                    PermissionName.STANDARD_THRESHOLD_WRITE,
                    PermissionName.STANDARD_THRESHOLD_CREATE,
                    PermissionName.STANDARD_THRESHOLD_READ_ALL,
                    PermissionName.ROLE_DELETE,
                    PermissionName.ROLE_WRITE,
                    PermissionName.ROLE_READ,
                    PermissionName.ROLE_CREATE,
                    PermissionName.ROLE_READ_ALL,
                    PermissionName.PATIENT_GROUP_READ_ALL,
                    PermissionName.PATIENT_GROUP_READ,
                    PermissionName.PATIENT_GROUP_CREATE,
                    PermissionName.PATIENT_GROUP_WRITE,
                    PermissionName.PATIENT_GROUP_DELETE,
                    PermissionName.CLINICIAN_PATIENT_GROUP_DELETE,
                    PermissionName.SCHEDULEWINDOW_READ,
                    PermissionName.SCHEDULEWINDOW_READ_ALL,
                    PermissionName.SCHEDULEWINDOW_WRITE,
                    PermissionName.AUDITLOG_READ,
                    PermissionName.PASSIVE_INTERVAL_CREATE,
                    PermissionName.PASSIVE_INTERVAL_DELETE,
                    PermissionName.PASSIVE_INTERVAL_WRITE,
                    PermissionName.PASSIVE_INTERVAL_READ,
                    PermissionName.PASSIVE_INTERVAL_READ_ALL,
                    PermissionName.QUESTIONNAIRE_GROUP_READ,
                    PermissionName.QUESTIONNAIRE_GROUP_READ_ALL,
                    PermissionName.QUESTIONNAIRE_GROUP_WRITE,
                    PermissionName.QUESTIONNAIRE_GROUP_CREATE,
                    PermissionName.QUESTIONNAIRE_GROUP_DELETE,
                ]
            case RoleName.ROLE_CLINICIAN.value():
                return [
                    PermissionName.WEB_LOGIN,
                    PermissionName.CLAIM_PATIENT_RESPONSIBILITY,
                    PermissionName.SET_PATIENT_RESPONSIBILITY,
                    PermissionName.PATIENT_READ,
                    PermissionName.PATIENT_READ_ALL,
                    PermissionName.PATIENT_WRITE,
                    PermissionName.PATIENT_DELETE,
                    PermissionName.PATIENT_CREATE,
                    PermissionName.PATIENT_REMOVE_BLUE_ALARMS,
                    PermissionName.PATIENT_PREFERENCES_WRITE,
                    PermissionName.NEXT_OF_KIN_READ,
                    PermissionName.NEXT_OF_KIN_CREATE,
                    PermissionName.NEXT_OF_KIN_WRITE,
                    PermissionName.NEXT_OF_KIN_DELETE,
                    PermissionName.THRESHOLD_READ,
                    PermissionName.THRESHOLD_READ_ALL,
                    PermissionName.THRESHOLD_DELETE,
                    PermissionName.THRESHOLD_WRITE,
                    PermissionName.THRESHOLD_CREATE,
                    PermissionName.QUESTIONNAIRE_READ,
                    PermissionName.QUESTIONNAIRE_WRITE,
                    PermissionName.QUESTIONNAIRE_CREATE,
                    PermissionName.QUESTIONNAIRE_DELETE,
                    PermissionName.QUESTIONNAIRE_ACKNOWLEDGE,
                    PermissionName.PATIENT_QUESTIONNAIRE_READ_ALL,
                    PermissionName.PATIENT_QUESTIONNAIRE_CREATE,
                    PermissionName.PATIENT_QUESTIONNAIRE_DELETE,
                    PermissionName.PATIENT_QUESTIONNAIRE_WRITE,
                    PermissionName.PATIENT_QUESTIONNAIRE_READ,
                    PermissionName.COMPLETED_QUESTIONNAIRE_READ,
                    PermissionName.COMPLETED_QUESTIONNAIRE_READ_ALL,
                    PermissionName.QUESTIONNAIRE_SCHEDULE_DELETE,
                    PermissionName.QUESTIONNAIRE_SCHEDULE_WRITE,
                    PermissionName.QUESTIONNAIRE_SCHEDULE_CREATE,
                    PermissionName.QUESTIONNAIRE_IGNORE,
                    PermissionName.NODE_RESULT_IGNORE,
                    PermissionName.PATIENT_NOTE_WRITE,
                    PermissionName.PATIENT_NOTE_MARK_SEEN,
                    PermissionName.PATIENT_NOTE_READ,
                    PermissionName.PATIENT_NOTE_CREATE,
                    PermissionName.PATIENT_NOTE_READ_ALL_TEAM,
                    PermissionName.PATIENT_NOTE_READ_ALL,
                    PermissionName.PATIENT_NOTE_DELETE,
                    PermissionName.PATIENT_GROUP_READ,
                    PermissionName.METER_READ,
                    PermissionName.METER_WRITE,
                    PermissionName.METER_READ_ALL,
                    PermissionName.MONITOR_KIT_READ,
                    PermissionName.MONITOR_KIT_WRITE,
                    PermissionName.MONITOR_KIT_READ_ALL,
                    PermissionName.MONITORING_PLAN_READ,
                    PermissionName.MONITORING_PLAN_WRITE,
                    PermissionName.MONITORING_PLAN_CREATE,
                    PermissionName.MONITORING_PLAN_DELETE,
                    PermissionName.MESSAGE_READ,
                    PermissionName.MESSAGE_WRITE,
                    PermissionName.MESSAGE_CREATE,
                    PermissionName.MESSAGE_DELETE,
                    PermissionName.MEASUREMENT_READ,
                    PermissionName.MEASUREMENT_DELETE,
                    PermissionName.MEASUREMENT_WRITE,
                    PermissionName.MEASUREMENT_CREATE,
                    PermissionName.MEASUREMENT_READ_ALL,
                    PermissionName.CLINICIAN_CHANGE_PASSWORD,
                    PermissionName.STANDARD_THRESHOLD_READ,
                    PermissionName.PASSIVE_INTERVAL_CREATE,
                    PermissionName.PASSIVE_INTERVAL_DELETE,
                    PermissionName.PASSIVE_INTERVAL_WRITE,
                    PermissionName.PASSIVE_INTERVAL_READ,
                    PermissionName.PASSIVE_INTERVAL_READ_ALL,
                    PermissionName.CONFERENCE_READ
                ]
            case RoleName.ROLE_PATIENT.value():
                return [
                    PermissionName.WEB_LOGIN, // Maybe, maybe not?

                    PermissionName.PATIENT_QUESTIONNAIRE_READ_ALL,
                    PermissionName.PATIENT_QUESTIONNAIRE_WRITE,

                    PermissionName.COMPLETED_QUESTIONNAIRE_READ,
                    PermissionName.COMPLETED_QUESTIONNAIRE_READ_ALL,
                    PermissionName.QUESTIONNAIRE_UPLOAD,
                    PermissionName.QUESTIONNAIRE_DOWNLOAD,
                    PermissionName.QUESTIONNAIRE_READ_ALL,
                    PermissionName.PATIENT_LOGIN,

                    PermissionName.MESSAGE_READ_JSON,
                    PermissionName.MESSAGE_WRITE_JSON,
                    PermissionName.MESSAGE_DELETE_JSON,

                    PermissionName.JOIN_VIDEO_CALL
                ]
            case RoleName.ROLE_VIDEO_CONSULTANT.value():
                return [
                    PermissionName.VIDEO_CALL
                ]
        }
    }
}
