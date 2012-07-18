package com.danveloper.gormish

import com.danveloper.gormish.model.Registrant
import com.danveloper.gormish.config.BootStrap

/**
 * Simply a runnable class that uses the supplied BootStrap class to initialize the minimum application context required for GORM
 *
 * @author Dan Woods g:(daniel.p.woods@gmail.com) t:(@danveloper)
 *
 */
class Gormish {
    public static void main(String[] args) {
        BootStrap.init()

        def r = new Registrant(firstName: "Dan", lastName: "Woods").save(flush: true)

        assert Registrant.findAllByLastName("Woods").size() == 1

    }
}
