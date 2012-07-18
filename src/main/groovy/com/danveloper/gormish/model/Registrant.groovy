package com.danveloper.gormish.model

import grails.persistence.Entity

/**
 *  Classes that we intend to be treated as GORM DomainClasses must be annotated {@link Entity}
 *
 *  http://grails.org/doc/latest/guide/single.html#GORM
 */
@Entity
class Registrant {
    String firstName
    String lastName

    static constraints = {
        firstName(nullable: false)
        lastName(nullable: false)
    }
}
