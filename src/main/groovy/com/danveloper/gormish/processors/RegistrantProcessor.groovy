package com.danveloper.gormish.processors

import com.danveloper.gormish.model.Registrant

class RegistrantProcessor {
    Registrant processNewRegistrant(firstName, lastName) {
        def registrant = new Registrant(firstName: firstName, lastName: lastName)
        if (!registrant.save(flush: true)) {
            throw new RuntimeException("Something went wrong processing a new registrant")
        }

        return registrant
    }

}
