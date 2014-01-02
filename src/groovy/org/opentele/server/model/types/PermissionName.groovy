package org.opentele.server.model.types

import dk.silverbullet.kih.api.auditlog.AuditLogPermissionName

/**
 * Complete list of permissions in the system.
 *
 * Please note: When adding new permissions in this list, remember to insert a new description in the messages file!
 */
public class PermissionName {
    public static final String NONE = 'ROLE_NONE' // Default permission, giving no-one access. Should be the default permission on controller classes.

    public static final String WEB_LOGIN = 'ROLE_WEB_LOGIN'

    public static final String SET_PATIENT_RESPONSIBILITY = 'ROLE_SET_PATIENT_RESPONSIBILITY'

    public static final String PATIENT_READ = 'ROLE_PATIENT_READ'
    public static final String PATIENT_READ_ALL = 'ROLE_PATIENT_READ_ALL'
    public static final String PATIENT_WRITE = 'ROLE_PATIENT_WRITE'
    public static final String PATIENT_CREATE = 'ROLE_PATIENT_CREATE'
    public static final String PATIENT_REMOVE_BLUE_ALARMS = 'ROLE_PATIENT_REMOVE_BLUE_ALARMS'
    public static final String PATIENT_PREFERENCES_WRITE = 'ROLE_PATIENT_PREFERENCES_WRITE'

    public static final String NEXT_OF_KIN_READ = 'ROLE_NEXT_OF_KIN_READ'
    public static final String NEXT_OF_KIN_WRITE = 'ROLE_NEXT_OF_KIN_WRITE'
    public static final String NEXT_OF_KIN_CREATE = 'ROLE_NEXT_OF_KIN_CREATE'
    public static final String NEXT_OF_KIN_DELETE = 'ROLE_NEXT_OF_KIN_DELETE'

    public static final String QUESTIONNAIRE_READ = 'ROLE_QUESTIONNAIRE_READ'
    public static final String QUESTIONNAIRE_READ_ALL = 'ROLE_QUESTIONNAIRE_READ_ALL'
    public static final String QUESTIONNAIRE_WRITE = 'ROLE_QUESTIONNAIRE_WRITE'
    public static final String QUESTIONNAIRE_CREATE = 'ROLE_QUESTIONNAIRE_CREATE'
    public static final String QUESTIONNAIRE_DELETE = 'ROLE_QUESTIONNAIRE_DELETE'
    public static final String QUESTIONNAIRE_ACKNOWLEDGE = 'ROLE_QUESTIONNAIRE_ACKNOWLEDGE'
    public static final String QUESTIONNAIRE_IGNORE = 'ROLE_QUESTIONNAIRE_IGNORE'
    public static final String QUESTIONNAIRE_UPLOAD = 'ROLE_QUESTIONNAIRE_UPLOAD'
    public static final String QUESTIONNAIRE_DOWNLOAD = 'ROLE_QUESTIONNAIRE_DOWNLOAD'
    public static final String QUESTIONNAIRE_ACKNOWLEDGED_READ = 'ROLE_QUESTIONNAIRE_ACKNOWLEDGED_READ'

    public static final String QUESTIONNAIRE_GROUP_READ = 'ROLE_QUESTIONNAIRE_GROUP_READ'
    public static final String QUESTIONNAIRE_GROUP_READ_ALL = 'ROLE_QUESTIONNAIRE_GROUP_READ_ALL'
    public static final String QUESTIONNAIRE_GROUP_WRITE = 'ROLE_QUESTIONNAIRE_GROUP_WRITE'
    public static final String QUESTIONNAIRE_GROUP_CREATE = 'ROLE_QUESTIONNAIRE_GROUP_CREATE'
    public static final String QUESTIONNAIRE_GROUP_DELETE = 'ROLE_QUESTIONNAIRE_GROUP_DELETE'

    public static final String MESSAGE_READ = 'ROLE_MESSAGE_READ'
    public static final String MESSAGE_WRITE = 'ROLE_MESSAGE_WRITE'
    public static final String MESSAGE_WRITE_JSON = 'ROLE_MESSAGE_WRITE_JSON'
    public static final String MESSAGE_CREATE = 'ROLE_MESSAGE_CREATE'
    public static final String MESSAGE_READ_JSON = 'ROLE_MESSAGE_READ_JSON'

    public static final String METER_READ = 'ROLE_METER_READ'
    public static final String METER_READ_ALL = 'ROLE_METER_READ_ALL'
    public static final String METER_CREATE = 'ROLE_METER_CREATE'
    public static final String METER_WRITE = 'ROLE_METER_WRITE'
    public static final String METER_DELETE = 'ROLE_METER_DELETE'

    public static final String MONITOR_KIT_READ = 'ROLE_MONITOR_KIT_READ'
    public static final String MONITOR_KIT_READ_ALL = 'ROLE_MONITOR_KIT_READ_ALL'
    public static final String MONITOR_KIT_CREATE = 'ROLE_MONITOR_KIT_CREATE'
    public static final String MONITOR_KIT_WRITE = 'ROLE_MONITOR_KIT_WRITE'
    public static final String MONITOR_KIT_DELETE = 'ROLE_MONITOR_KIT_DELETE'

    public static final String CLINICIAN_READ = 'ROLE_CLINICIAN_READ'
    public static final String CLINICIAN_READ_ALL = 'ROLE_CLINICIAN_READ_ALL'
    public static final String CLINICIAN_WRITE = 'ROLE_CLINICIAN_WRITE'
    public static final String CLINICIAN_CREATE = 'ROLE_CLINICIAN_CREATE'
    public static final String CLINICIAN_DELETE = 'ROLE_CLINICIAN_DELETE'
    public static final String CLINICIAN_CHANGE_PASSWORD = 'ROLE_CLINICIAN_CHANGE_PASSWORD'

    public static final String CLINICIAN_PATIENT_GROUP_DELETE = 'ROLE_CLINICIAN_PATIENT_GROUP_DELETE'

    public static final String MONITORING_PLAN_READ = 'ROLE_MONITORING_PLAN_READ'
    public static final String MONITORING_PLAN_WRITE = 'ROLE_MONITORING_PLAN_WRITE'
    public static final String MONITORING_PLAN_CREATE = 'ROLE_MONITORING_PLAN_CREATE'
    public static final String MONITORING_PLAN_DELETE = 'ROLE_MONITORING_PLAN_DELETE'

    public static final String THRESHOLD_READ = 'ROLE_THRESHOLD_READ'
    public static final String THRESHOLD_WRITE = 'ROLE_THRESHOLD_WRITE'
    public static final String THRESHOLD_CREATE = 'ROLE_THRESHOLD_CREATE'
    public static final String THRESHOLD_READ_ALL = 'ROLE_THRESHOLD_READ_ALL'
    public static final String THRESHOLD_DELETE = 'ROLE_THRESHOLD_DELETE'

    public static final String PATIENT_QUESTIONNAIRE_READ_ALL = 'ROLE_PATIENT_QUESTIONNAIRE_READ_ALL'
    public static final String PATIENT_QUESTIONNAIRE_CREATE = 'ROLE_PATIENT_QUESTIONNAIRE_CREATE'
    public static final String PATIENT_QUESTIONNAIRE_DELETE = 'ROLE_PATIENT_QUESTIONNAIRE_DELETE'
    public static final String PATIENT_QUESTIONNAIRE_WRITE = 'ROLE_PATIENT_QUESTIONNAIRE_WRITE'
    public static final String PATIENT_QUESTIONNAIRE_READ = 'ROLE_PATIENT_QUESTIONNAIRE_READ'

    public static final String NODE_RESULT_IGNORE = 'ROLE_NODE_RESULT_IGNORE'

    public static final String COMPLETED_QUESTIONNAIRE_READ = 'ROLE_COMPLETED_QUESTIONNAIRE_READ'
    public static final String COMPLETED_QUESTIONNAIRE_READ_ALL = 'ROLE_COMPLETED_QUESTIONNAIRE_READ_ALL'

    public static final String USER_DELETE = 'ROLE_USER_DELETE'
    public static final String USER_WRITE = 'ROLE_USER_WRITE'
    public static final String USER_READ = 'ROLE_USER_READ'
    public static final String USER_CREATE = 'ROLE_USER_CREATE'
    public static final String USER_READ_ALL = 'ROLE_USER_READ_ALL'
    
    public static final String STANDARD_THRESHOLD_DELETE = 'ROLE_STANDARD_THRESHOLD_DELETE'
    public static final String STANDARD_THRESHOLD_READ = 'ROLE_STANDARD_THRESHOLD_READ'
    public static final String STANDARD_THRESHOLD_WRITE = 'ROLE_STANDARD_THRESHOLD_WRITE'
    public static final String STANDARD_THRESHOLD_CREATE = 'ROLE_STANDARD_THRESHOLD_CREATE'
    public static final String STANDARD_THRESHOLD_READ_ALL = 'ROLE_STANDARD_THRESHOLD_READ_ALL'

    public static final String ROLE_DELETE = 'ROLE_ROLE_DELETE'
    public static final String ROLE_WRITE = 'ROLE_ROLE_WRITE'
    public static final String ROLE_READ = 'ROLE_ROLE_READ'
    public static final String ROLE_CREATE = 'ROLE_ROLE_CREATE'
    public static final String ROLE_READ_ALL = 'ROLE_ROLE_READ_ALL'

    public static final String QUESTIONNAIRE_SCHEDULE_DELETE = 'ROLE_QUESTIONNAIRE_SCHEDULE_DELETE'
    public static final String QUESTIONNAIRE_SCHEDULE_WRITE = 'ROLE_QUESTIONNAIRE_SCHEDULE_WRITE'
    public static final String QUESTIONNAIRE_SCHEDULE_CREATE = 'ROLE_QUESTIONNAIRE_SCHEDULE_CREATE'

    public static final String PATIENT_NOTE_WRITE = 'ROLE_PATIENT_NOTE_WRITE'
    public static final String PATIENT_NOTE_MARK_SEEN = 'ROLE_PATIENT_NOTE_MARK_SEEN'
    public static final String PATIENT_NOTE_READ = 'ROLE_PATIENT_NOTE_READ'
    public static final String PATIENT_NOTE_CREATE = 'ROLE_PATIENT_NOTE_CREATE'
    public static final String PATIENT_NOTE_READ_ALL_TEAM = 'ROLE_PATIENT_NOTE_READ_ALL_TEAM'
    public static final String PATIENT_NOTE_READ_ALL = 'ROLE_PATIENT_NOTE_READ_ALL'
    public static final String PATIENT_NOTE_DELETE = 'ROLE_PATIENT_NOTE_DELETE'

    public static final String PATIENT_GROUP_READ_ALL = 'ROLE_PATIENT_GROUP_READ_ALL'
    public static final String PATIENT_GROUP_CREATE = 'ROLE_PATIENT_GROUP_CREATE'
    public static final String PATIENT_GROUP_READ = 'ROLE_PATIENT_GROUP_READ'
    public static final String PATIENT_GROUP_WRITE = 'ROLE_PATIENT_GROUP_WRITE'
    public static final String PATIENT_GROUP_DELETE   = 'ROLE_PATIENT_GROUP_DELETE  '

    public static final String MEASUREMENT_READ = 'ROLE_MEASUREMENT_READ'

    public static final String PATIENT_LOGIN = 'ROLE_PATIENT_LOGIN'
    public static final String PATIENT_DELETE = 'ROLE_PATIENT_DELETE'

    public static final String VIDEO_CALL = 'ROLE_VIDEO_CALL'
    public static final String JOIN_VIDEO_CALL = 'ROLE_JOIN_VIDEO_CALL'

    public static final String AUDITLOG_READ = AuditLogPermissionName.AUDITLOG_READ

    public static final String PASSIVE_INTERVAL_READ = 'ROLE_PASSIVE_INTERVAL_READ'
    public static final String PASSIVE_INTERVAL_READ_ALL = 'ROLE_PASSIVE_INTERVAL_READ_ALL'
    public static final String PASSIVE_INTERVAL_CREATE = 'ROLE_PASSIVE_INTERVAL_CREATE'
    public static final String PASSIVE_INTERVAL_WRITE = 'ROLE_PASSIVE_INTERVAL_WRITE'
    public static final String PASSIVE_INTERVAL_DELETE = 'ROLE_PASSIVE_INTERVAL_DELETE'
    public static final String SCHEDULEWINDOW_READ = 'ROLE_SCHEDULEWINDOW_READ'
    public static final String SCHEDULEWINDOW_WRITE = 'ROLE_SCHEDULEWINDOW_WRITE'
    public static final String SCHEDULEWINDOW_READ_ALL = 'ROLE_SCHEDULEWINDOW_READ_ALL'

    public static final String CONFERENCE_READ = 'ROLE_CONFERENCE_READ'

    public static List<String> allValues() {
        PermissionName.fields.findAll { it.name.matches("\\p{Upper}+(_?|\\p{Upper})*") }.collect()*.get(PermissionName)
    }
}
