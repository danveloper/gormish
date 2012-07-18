package com.danveloper.gormish

import javax.annotation.PostConstruct
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.plugins.PluginManagerHolder
import org.codehaus.groovy.grails.commons.spring.WebRuntimeSpringConfiguration
import org.springframework.web.context.WebApplicationContext
import org.codehaus.groovy.grails.commons.spring.GrailsRuntimeConfigurator
import org.springframework.transaction.support.TransactionSynchronizationManager
import org.springframework.orm.hibernate3.SessionHolder
import org.hibernate.SessionFactory

class BootStrap {
    @PostConstruct
    static void init() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.danveloper.gormish")
        def grailsApplication = ApplicationHolder.application
        def pluginManager = PluginManagerHolder.pluginManager
        WebRuntimeSpringConfiguration springConfiguration =  new WebRuntimeSpringConfiguration(applicationContext, grailsApplication.classLoader)

        pluginManager.doRuntimeConfiguration(springConfiguration)

        grailsApplication.setMainContext(springConfiguration.getUnrefreshedApplicationContext())
        applicationContext = (WebApplicationContext) springConfiguration.getApplicationContext()

        pluginManager.applicationContext = applicationContext

        pluginManager.doDynamicMethods()

        def sessionFactory = (SessionFactory)applicationContext.getBean(GrailsRuntimeConfigurator.SESSION_FACTORY_BEAN)
        if (!TransactionSynchronizationManager.hasResource(sessionFactory)) {
            TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(sessionFactory.openSession()))
        }
    }
}
