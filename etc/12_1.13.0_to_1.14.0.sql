-- Fix til fejl introduceret ved migrering til 1.13.0.. (KIH-986)
update questionnaire_group2questionnaire_header
set standard_schedule_intro_period_weeks = null
where STANDARD_SCHEDULE_TIMES_OF_DAY is null
and STANDARD_SCHEDULE_WEEKDAYS is null
and STANDARD_SCHEDULE_WEEKDAYS_INTRO_PERIOD is null
and STANDARD_SCHEDULE_WEEKDAYS_SECOND_PERIOD is null
and STANDARD_SCHEDULE_REMINDER_TIME is null
and STANDARD_SCHEDULE_BLUE_ALARM_TIME is null
and STANDARD_SCHEDULE_DAYS_IN_MONTH is null
and STANDARD_SCHEDULE_INTERVAL_IN_DAYS is null
and STANDARD_SCHEDULE_STARTING_DATE is null
and STANDARD_SCHEDULE_SPECIFIC_DATE is null
and standard_schedule_reminder_start_minutes is null
and standard_schedule_intro_period_weeks is not null;