package com.danveloper.gormish

import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.codehaus.groovy.grails.web.context.GrailsConfigUtils
import org.springframework.mock.web.MockServletContext
import org.springframework.web.context.WebApplicationContext
import javax.servlet.ServletContext
import org.codehaus.groovy.grails.commons.spring.WebRuntimeSpringConfiguration
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.plugins.PluginManagerHolder

class Gormish {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.danveloper.gormish")
        def grailsApplication = ApplicationHolder.application
        def pluginManager = PluginManagerHolder.pluginManager
        WebRuntimeSpringConfiguration springConfiguration =  new WebRuntimeSpringConfiguration(applicationContext, grailsApplication.classLoader)

        pluginManager.doRuntimeConfiguration(springConfiguration)

        grailsApplication.setMainContext(springConfiguration.getUnrefreshedApplicationContext())
        applicationContext = (WebApplicationContext) springConfiguration.getApplicationContext()

        pluginManager.applicationContext = applicationContext

        def dataSource = applicationContext.getBean("dataSource")
        println dataSource
    }
}
