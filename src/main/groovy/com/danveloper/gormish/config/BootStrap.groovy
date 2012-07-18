package com.danveloper.gormish.config

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.spring.GrailsRuntimeConfigurator
import org.codehaus.groovy.grails.commons.spring.WebRuntimeSpringConfiguration
import org.codehaus.groovy.grails.plugins.PluginManagerHolder
import org.hibernate.SessionFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.orm.hibernate3.SessionHolder
import org.springframework.transaction.support.TransactionSynchronizationManager
import org.springframework.web.context.WebApplicationContext

/**
 * This class sets up the Spring and Grails application Contexts as well as
 * setting up the necessary GORM-related plugins and binding the Hibernate SessionFactory
 *
 * @author Dan Woods
 */
class BootStrap {
    /**
     * This method setups the application contexts and makes all the Entity classes GORMy
     * :)
     */
    static void init() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.danveloper.gormish")

        Configuration.initializePlugins()

        def grailsApplication = ApplicationHolder.application
        def pluginManager = PluginManagerHolder.pluginManager
        WebRuntimeSpringConfiguration springConfiguration =  new WebRuntimeSpringConfiguration(applicationContext, grailsApplication.classLoader)

        pluginManager.doRuntimeConfiguration(springConfiguration)

        grailsApplication.setMainContext(springConfiguration.getUnrefreshedApplicationContext())
        applicationContext = (WebApplicationContext) springConfiguration.getApplicationContext()

        pluginManager.applicationContext = applicationContext

        pluginManager.doDynamicMethods()

        bindSessionFactory(applicationContext)
    }
    private static void bindSessionFactory(applicationContext) {
        def sessionFactory = (SessionFactory)applicationContext.getBean(GrailsRuntimeConfigurator.SESSION_FACTORY_BEAN)
        if (!TransactionSynchronizationManager.hasResource(sessionFactory)) {
            TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(sessionFactory.openSession()))
        }
    }
}
