dataSource {
    pooled = true
//    driverClassName = "org.h2.Driver"
//    dialect = "org.opentele.server.util.H2Dialect"
//    username = "sa"
//    password = ""
    logSql = false
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}
// environment specific settings
environments {
    
    mysqltest {
        dataSource {
             pooled = true
             dialect = "org.opentele.server.util.MySQLInnoDBDialect"
             driverClassName = "com.mysql.jdbc.Driver"
             username = "openteledev"
             password = "openteledev"
//             dbCreate = "create-drop"
             url = "jdbc:mysql://localhost:3306/openteledev"
         } 
    }

    development {
        dataSource {
            username = "sa"
            password = ""
            driverClassName = "org.h2.Driver"
            dialect = "org.opentele.server.util.H2Dialect"
            // dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = "jdbc:h2:devDb;MVCC=TRUE;IGNORECASE=TRUE"
            // url = "jdbc:h2:mem:devDb;MVCC=TRUE;IGNORECASE=TRUE"
        }
    }

    test {
        dataSource {
//            dbCreate = "create-drop"
            username = "sa"
            password = ""
            driverClassName = "org.h2.Driver"
            dialect = "org.opentele.server.util.H2Dialect"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;IGNORECASE=TRUE"
           }
    }
}
