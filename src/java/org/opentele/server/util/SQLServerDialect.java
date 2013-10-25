package org.opentele.server.util;

import java.sql.Types;

public class SQLServerDialect extends org.hibernate.dialect.SQLServer2008Dialect {
 
   /**
    * Initializes a new instance of the {@link SQLServerDialect} class.
    */
    public SQLServerDialect() {
//        registerColumnType(Types.BIGINT, "bigint");
//        registerColumnType(Types.BIT, "bit");
//        registerColumnType(Types.CHAR, "nchar(1)");
//        registerColumnType(Types.VARCHAR, 4000, "nvarchar($l)");
        registerColumnType(Types.VARCHAR, 255, "nvarchar(1024)");
        registerColumnType(Types.VARBINARY, 4000, "varbinary($1)");
        registerColumnType(Types.VARBINARY, 255, "varbinary(max)");
        registerColumnType(Types.BLOB, "varbinary(max)");
        registerColumnType(Types.CLOB, "varchar(max)");
    }
}