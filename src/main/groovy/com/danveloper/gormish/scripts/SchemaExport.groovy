package com.danveloper.gormish.scripts

import org.codehaus.groovy.grails.commons.ApplicationHolder

import org.hibernate.tool.hbm2ddl.SchemaExport as HibernateSchemaExport
import com.danveloper.gormish.config.BootStrap

class SchemaExport {
    public static void main(String[] args) {
        BootStrap.init()

        def ctx = ApplicationHolder.application.mainContext
        def configuration = ctx.getBean("&sessionFactory").configuration

        def schemaExport = new HibernateSchemaExport(configuration)
        schemaExport.with {
            haltOnError = true
            outputFile = "${System.properties['java.io.tmpdir']}/ddl.sql"
            delimiter = ";"
            execute true, false, false, false
        }
    }
}
