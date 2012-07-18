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
        Class cls = this.classLoader.loadClass("org.springframework.util.Log4jConfigurer")
        invokeStaticMethod(cls, "initLogging", ["classpath:log4j.properties"].toArray(new Object[1]))
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

    /**
     * STOLEN FROM Grails project...
     *
     * Invokes the named method on a target object using reflection.
     * The method signature is determined by the classes of each argument.
     * @param target The object to call the method on.
     * @param name The name of the method to call.
     * @param args The arguments to pass to the method (may be an empty array).
     * @return The value returned by the method.
     */
    private static Object invokeStaticMethod(Class target, String name, Object[] args) {
        Class<?>[] argTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }

        try {
            return target.getMethod(name, argTypes).invoke(target, args);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
