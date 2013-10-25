package org.opentele.server.util

import java.beans.PropertyEditorSupport
import org.springframework.util.StringUtils

class CustomFloatEditor extends PropertyEditorSupport {

    private final boolean allowEmpty

    CustomFloatEditor(boolean allowEmpty) {
        this.allowEmpty = allowEmpty
    }
    
    @Override
    void setAsText(String text) {
        
        if (this.allowEmpty && !StringUtils.hasText(text)) {
            // Treat empty String as null value.
            setValue(null);
        } else {
            setValue(parse(text))
        }
    }
    
    @Override
    String getAsText() {
        Float floatVal = (Float) getValue()
        "${floatVal}"
    }
    
    Float parse(String text) {
        if (text) {
            if (text.indexOf('.') > -1) {
                throw new IllegalArgumentException("Kun komma er tilladt som decimal-tals separator")
            }
            try {
                // Hack for now..
                
                NumberFormatUtil df = new NumberFormatUtil()
                return (Float) df.parse(text)
            } catch(Exception exception){
                throw new IllegalArgumentException("${text} er ikke et decimaltal. Angiv venligst et decimaltal.")
            }
        } else {
            return null
        }
    }
}