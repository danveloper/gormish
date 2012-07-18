package com.danveloper.gormish.config

/**
 * http://grails.org/doc/latest/guide/conf.html#dataSource
 */

dataSource {
    pooled = true
    driverClassName = "com.mysql.jdbc.Driver"
    dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
    username = "root"
    password = ""
    dbCreate = "create-drop"
    url = "jdbc:mysql://localhost/gormish?useUnicode=yes&characterEncoding=UTF-8"
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}
