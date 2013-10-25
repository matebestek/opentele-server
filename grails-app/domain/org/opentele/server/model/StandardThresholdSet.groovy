package org.opentele.server.model

class StandardThresholdSet extends AbstractObject{
    static hasMany = [thresholds: Threshold]
    static hasOne = [patientGroup: PatientGroup]

    static constraints = {
        patientGroup(nullable:true)
        //Only one threshold of each type
        thresholds( validator: { val, obj ->
            if (val == null) {
                return true
            }

            //Group thresholds by their type
            def groups = val.groupBy {it.type.name}
            //Ensure that there exists only one threshold of each type
            return groups.values().every {it.size() == 1}
        } )
    }

    static mapping = {
        thresholds cascade: "all-delete-orphan"
    }

    /**
     * This is really hard to do with normal "findBy*" because of the database modeling.
     */
    static StandardThresholdSet forThreshold(Threshold threshold) {
        StandardThresholdSet.createCriteria().get {
            thresholds {
                eq('id', threshold.id)
            }
        }
    }
}
