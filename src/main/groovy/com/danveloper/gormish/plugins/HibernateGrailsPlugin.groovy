package com.danveloper.gormish.plugins

import org.codehaus.groovy.grails.commons.AnnotationDomainClassArtefactHandler
import org.codehaus.groovy.grails.plugins.orm.hibernate.HibernatePluginSupport

class HibernateGrailsPlugin {
    def author = "Graeme Rocher"
    def title = "Hibernate for Grails"
    def description = "Provides integration between Grails and Hibernate through GORM"

    def grailsVersion = "2.0 > *"
    def version = "2.1.0"
    def documentation = "http://grails.org/doc/$version"
    def observe = ['domainClass']

    def dependsOn = [dataSource: "2.1 > *",
            i18n: "2.1 > *",
            core: "2.1 > *",
            domainClass: "2.1 > *"]

    def loadAfter = ['controllers', 'domainClass']

    def watchedResources = ["file:./grails-app/conf/hibernate/**.xml"]

    def artefacts = [new AnnotationDomainClassArtefactHandler()]

    def doWithSpring = HibernatePluginSupport.doWithSpring

    def doWithDynamicMethods = HibernatePluginSupport.doWithDynamicMethods

    def onChange = HibernatePluginSupport.onChange
}
