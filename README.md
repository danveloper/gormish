Using GORM outside of Grails (Grails v2.1.0)
===

Synopsis
---
GORM is almost certainly the greatest feature of Grails. I often find myself cursing the Hibernate Gods when I have to switch between Grails and Java. This example project was inspired because I wanted to use the GORM features in a simple Groovy standalone application. Dependency management for this application is handled by Gradle, and the Grails-related features are built on Grails v2.1.0 dependencies.

Notice
---
This application is a work of trickery... Essentially what this is doing is setting up a minimally required Grails application context for the GORM features to work.

Notes
---
I hate -- with a firey passion -- XML configuration files, so this application has an entirely annotation-driven Spring configuration. Additional configuration, like DataSource is consumed through Groovy's ConfigSlurper and incorporated into the application context in the same way that Grails would natively do this.

Hints
---
com.danveloper.gormish.Gormish is the runnable "test" class (for these purposes, it's just a proof-of-concept). Bean configuration is managed through com.danveloper.gormish.config.Configuration, and application context initialization is handled through com.danveloper.gormish.config.BootStrap#init

License
---
Free-for-all. Contact me if you need help: g(daniel.p.woods@gmail.com) && t(@danveloper)

dan woods 2012.
