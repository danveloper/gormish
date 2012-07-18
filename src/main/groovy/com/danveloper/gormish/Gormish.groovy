package com.danveloper.gormish

import com.danveloper.gormish.model.Registrant
import org.codehaus.groovy.grails.commons.ApplicationHolder

class Gormish {
    public static void main(String[] args) {
        BootStrap.init()

        def r = new Registrant(firstName: "Dan", lastName: "Woods").save(flush: true)

        println Registrant.findAllByLastName("Woods")

    }
}
