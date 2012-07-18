package com.danveloper.gormish

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

@org.springframework.context.annotation.Configuration
class Configuration {
    static def domainClasses = [
            'com.danveloper.gormish.model.Registrant'
    ]
    static def plugins = [
            'org.codehaus.groovy.grails.plugins.DomainClassGrailsPlugin',
            'com.danveloper.gormish.HibernateGrailsPlugin'
    ]
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
        def datasourceConfig = new ConfigSlurper().parse(grailsApplication.classLoader.loadClass("com.danveloper.gormish.Datasource"))
        grailsApplication.config.merge(datasourceConfig)

        grailsApplication
    }
    @Bean
    @DependsOn(['grailsApplication'])
    public GrailsPluginManager pluginManager() {
        def grailsApplication = ApplicationHolder.application

        def hibernatePlugin = ("com.danveloper.gormish.HibernateGrailsPlugin")

        def loadedPluginClasses = plugins.collect { plugin -> grailsApplication.classLoader.loadClass(plugin)}

        def pluginManager = new DefaultGrailsPluginManager(loadedPluginClasses.toArray(new Class[loadedPluginClasses.size()]), grailsApplication)

        PluginManagerHolder.pluginManager = pluginManager
        pluginManager.loadPlugins()
        pluginManager.application = grailsApplication
        pluginManager.doArtefactConfiguration()
        ApplicationHolder.application.initialise()
    }
}
