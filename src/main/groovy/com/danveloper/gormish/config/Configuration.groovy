package com.danveloper.gormish.config

import org.springframework.context.annotation.Bean
import org.codehaus.groovy.grails.compiler.support.GrailsResourceLoader
import org.codehaus.groovy.grails.commons.GrailsResourceLoaderFactoryBean
import org.springframework.context.annotation.DependsOn
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import org.codehaus.groovy.grails.commons.spring.GrailsResourceHolder
import org.codehaus.groovy.grails.compiler.support.GrailsResourceLoaderHolder
import org.codehaus.groovy.grails.commons.GrailsApplicationFactoryBean
import org.codehaus.groovy.grails.plugins.GrailsPluginManager
import org.codehaus.groovy.grails.plugins.orm.hibernate.HibernatePluginSupport
import org.codehaus.groovy.grails.plugins.DefaultGrailsPluginManager
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.plugins.PluginManagerHolder

/**
 * Minimum bean configuration for the GORM-related features to work
 */
@org.springframework.context.annotation.Configuration
class Configuration {
    /**
     * Define domain classes in this static list
     */
    static def domainClasses = [
            'com.danveloper.gormish.model.Registrant'
    ]
    /**
     * DomainClassGrailsPlugin & HibernateGrailsPlugin must be available in order for GORM to work...
     *
     * Unfortunately, the HibernateGrailsPlugin class doesn't ship with the dependency,
     * so I've copy/pasted from the plugin zip into the listed class.
     */
    static def plugins = [
            'org.codehaus.groovy.grails.plugins.DomainClassGrailsPlugin',
            'com.danveloper.gormish.plugins.HibernateGrailsPlugin'
    ]

    /**
     * This is the exact equivalent of a grails-app's conf/DataSource.groovy...
     * I've named it DataSourceConfig to exemplify that, in this case, the naming convention is arbitrary & configurable
     */
    static def dataSourceConfig = "com.danveloper.gormish.config.DataSourceConfig"
    @Bean
    public GrailsResourceLoader grailsResourceLoader() {
        def a = new GrailsResourceLoaderFactoryBean()
        a.afterPropertiesSet()
        a.object;
    }
    @Bean
    @DependsOn(['grailsResourceLoader'])
    public GrailsApplication grailsApplication() {
        def grailsApplicationFactory = new GrailsApplicationFactoryBean()
        def grailsApplication = grailsApplicationFactory.with {
            grailsResourceLoader = GrailsResourceLoaderHolder.resourceLoader
            afterPropertiesSet()
            object
        }

        grailsApplication.@loadedClasses = domainClasses.collect { domainClass -> grailsApplication.classLoader.loadClass(domainClass) }

        // Setup the datasource
        def datasourceConfig = new ConfigSlurper().parse(grailsApplication.classLoader.loadClass(dataSourceConfig))
        grailsApplication.config.merge(datasourceConfig)

        grailsApplication
    }

    /**
     * Helper method for the {@link BootStrap} class to setup the plugins.
     */
    static void initializePlugins() {
        def grailsApplication = ApplicationHolder.application

        def loadedPluginClasses = plugins.collect { plugin -> grailsApplication.classLoader.loadClass(plugin)}

        def pluginManager = new DefaultGrailsPluginManager(loadedPluginClasses.toArray(new Class[loadedPluginClasses.size()]), grailsApplication)

        PluginManagerHolder.pluginManager = pluginManager
        pluginManager.loadPlugins()
        pluginManager.application = grailsApplication
        pluginManager.doArtefactConfiguration()
        ApplicationHolder.application.initialise()
    }
}
