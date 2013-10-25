package org.opentele.server.util;

import java.sql.Types;

public class H2Dialect extends org.hibernate.dialect.H2Dialect {
 
   /**
    * Initializes a new instance of the {@link H2Dialect} class.
    */
    public H2Dialect() {
        registerColumnType(Types.VARBINARY, 255, "blob");
        registerColumnType(Types.VARCHAR, 255, "varchar(512)"); // Hack: Seems Grails desperately maps strings to 255 chars pr. default
    }
}