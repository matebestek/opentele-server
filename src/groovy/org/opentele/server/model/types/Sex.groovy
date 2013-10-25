package org.opentele.server.model.types

enum Sex implements Serializable {

    MALE('M'), FEMALE('F'), UNKNOWN('U')
    
    Sex(String value) { 
        this.value = value 
    }
    
    private final String value
    
//    String getId() { 
//        value
//    }
    
    String value() { 
        value 
    }
}