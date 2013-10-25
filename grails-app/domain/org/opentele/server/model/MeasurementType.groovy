package org.opentele.server.model

import org.opentele.server.model.types.MeasurementTypeName

class MeasurementType extends AbstractObject {

    static hasMany = [ measurements:Measurement ]
    
    MeasurementTypeName name

    public MeasurementType(MeasurementTypeName typeName) {
        this.name = typeName
    }

    static constraints = {
        name (unique: true)
    }
    
    static mapping = {
        name (index: 'name_idx', sqlType: "varchar(255)") // sqlType necessary: See error: http://jira.grails.org/browse/GRAILS-8486 
    }

    String toString() {
        name
    }

    /**
     * Compares this MeasurementType to a MeasurementTypeName enum
     * such that equals on MeasurementTypes works with (and as) enums
     * @param other A MeasurementTypeName enum
     * @return true iff this this.name == other (is the same enum)
     */
    public boolean equals(MeasurementTypeName other) {
        return other == this.name
    }

    /**
     * Compares this MeasurementType to an other MeasurementType on
     * their .name attribute (enum), such that equals on MeasurementTypes
     * work with (and as) enums.
     * @param other A MeasurementType
     * @return true iff this.name == other.name
     */
    public boolean equals(MeasurementType other) {
        return other.name == this.name
    }
}
