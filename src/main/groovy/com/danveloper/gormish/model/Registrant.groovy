package com.danveloper.gormish.model

import grails.persistence.Entity

@Entity
class Registrant {
    String firstName
    String lastName

    static constraints = {
        firstName(nullable: false)
        lastName(nullable: false)
    }
}
