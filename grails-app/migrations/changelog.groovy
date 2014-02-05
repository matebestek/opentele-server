// import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
databaseChangeLog = {

    // generic properties used for mapping between sql dialects

    def app = ctx.getBean('grailsApplication')

    def updateOnStartSchema = app.config.grails.plugin.databasemigration.updateOnStartDefaultSchema

    // Mssql
    property name: "id.type", value:"bigint", dbms:"mssql"
    property name: "float.type", value:"float", dbms:"mssql"
    property name: "double.type", value:"double precision", dbms:"mssql"
    property name: "integer.type", value:"int", dbms:"mssql"
    property name: "boolean.type", value:"bit", dbms:"mssql"
    property name: "datetime.type", value:"datetime2(7)", dbms:"mssql"
    property name: "string.type", value:"nvarchar(1024)", dbms:"mssql"
    property name: "string128.type", value:"nvarchar(1024)", dbms:"mssql"
    property name: "string255.type", value:"varchar(255)", dbms:"mssql"
    property name: "string2000.type", value:"varchar(2000)", dbms:"mssql"
    property name: "string2048.type", value:"varchar(2048)", dbms:"mssql"
    property name: "text.type", value:"varchar(max)", dbms:"mssql"
    property name: "blob.type", value:"varbinary(max)", dbms:"mssql"
    property name: "now", value:"getdate()", dbms:"mssql"
    property name: "schemaName", value: "${updateOnStartSchema}", dbms:"mssql"
    if (!updateOnStartSchema) {
        property name: "fullSchemaName", value: "", dbms:"mssql"
    } else {
        property name: "fullSchemaName", value: "[${updateOnStartSchema}].", dbms:"mssql"
    }

    // MySql
    property name: "id.type", value:"bigint", dbms:"mysql"
    property name: "float.type", value:"float", dbms:"mysql"
    property name: "double.type", value:"double precision", dbms:"mysql"
    property name: "integer.type", value:"integer", dbms:"mysql"
    property name: "boolean.type", value:"bit", dbms:"mysql"
    property name: "datetime.type", value:"datetime", dbms:"mysql"
    property name: "string.type", value:"varchar(1024)", dbms:"mysql"
    property name: "string128.type", value:"varchar(128)", dbms:"mysql" // MySQL cannot index fields > 767 bytes
    property name: "string255.type", value:"varchar(255)", dbms:"mysql"
    property name: "string2000.type", value:"varchar(2000)", dbms:"mysql"
    property name: "string2048.type", value:"varchar(2048)", dbms:"mysql"
    property name: "text.type", value:"longtext", dbms:"mysql"
    property name: "blob.type", value:"blob", dbms:"mysql"
    property name: "now", value:"now()", dbms:"mysql"
    property name: "schemaName", value: "", dbms:"mysql"
    property name: "fullSchemaName", value: "", dbms:"mysql"

    // H2
    property name: "id.type", value:"bigint", dbms:"h2"
    property name: "float.type", value:"float", dbms:"h2"
    property name: "double.type", value:"double precision", dbms:"h2"
    property name: "integer.type", value:"integer", dbms:"h2"
    property name: "boolean.type", value:"bit", dbms:"h2"
    property name: "datetime.type", value:"datetime", dbms:"h2"
    property name: "string.type", value:"varchar(1024)", dbms:"h2"
    property name: "string128.type", value:"varchar(1024)", dbms:"h2"
    property name: "string255.type", value:"varchar(255)", dbms:"h2"
    property name: "string2000.type", value:"varchar(2000)", dbms:"h2"
    property name: "string2048.type", value:"varchar(2048)", dbms:"h2"
    property name: "text.type", value:"clob", dbms:"h2"
    property name: "blob.type", value:"blob", dbms:"h2"
    property name: "now", value:"CURRENT_TIMESTAMP()", dbms:"h2"
    property name: "schemaName", value: "", dbms:"h2"
    property name: "fullSchemaName", value: "", dbms:"h2"

    println "Starting migrations..."

    // Includes

    include file: '1_0_baseline.groovy'

    include file: '1_2_1_update.groovy'

    include file: '1_4_0_update.groovy'

    include file: '1_5_3_update.groovy'

    include file: '1_8_0_update.groovy'

    include file: '1_8_4_update.groovy'

    include file: '1_9_0_update.groovy'

    include file: '1_11_0_update.groovy'

    include file: '1_13_0_update.groovy'

    include file: '1_15_0_update.groovy'

    include file: '1_16_0_update.groovy'
}

