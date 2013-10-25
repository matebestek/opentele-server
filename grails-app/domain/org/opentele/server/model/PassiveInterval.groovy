package org.opentele.server.model

class PassiveInterval extends AbstractObject{

    static hasOne = [patient: Patient]

    Date intervalStartDate
    Date intervalEndDate
    String comment

    Boolean inInterval() {
        Date d = new Date();
        intervalStartDate <= d && d <= intervalEndDate
    }

    static constraints = {
        comment(nullable: true);
        intervalStartDate(nullable: true);
        intervalEndDate(nullable: false);
        intervalStartDate(validator: { intervalStart, obj ->
            if (intervalStart) {
                //start date should be before end date
                if (obj.intervalEndDate && intervalStart.after(obj.intervalEndDate)) {
                    return "dateStartAfterEnd"
                }
            } else {
                return "nullable"
            }
        })
    }
}
