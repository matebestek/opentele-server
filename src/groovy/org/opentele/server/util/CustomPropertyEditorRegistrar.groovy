package org.opentele.server.util

import org.springframework.beans.PropertyEditorRegistrar
import org.springframework.beans.PropertyEditorRegistry
    
class CustomPropertyEditorRegistrar implements PropertyEditorRegistrar{
    
    public void registerCustomEditors(PropertyEditorRegistry registry) {
        registry.registerCustomEditor(float.class, new org.opentele.server.util.CustomFloatEditor(true));
        registry.registerCustomEditor(Float.class, new org.opentele.server.util.CustomFloatEditor(true));
    }
}
