//module net.cpollet.gallery {
//    requires jcommander;
//    requires twitter.text;
//    requires jsr305;
//    requires postgresql;
//    requires spring.jdbc;
//    requires spring.beans;
//    requires spring.core;
//    requires spring.jcl;
//    requires spring.tx;
//    requires liquibase.core;
//    requires snakeyaml;
//    requires com.google.common;
//    requires failureaccess;
//    requires listenablefuture;
//    requires checker.qual;
//    requires error.prone.annotations;
//    requires j2objc.annotations;
//    requires animal.sniffer.annotations;
//    requires com.fasterxml.jackson.databind;
//    requires jackson.annotations;
//    requires com.fasterxml.jackson.core;
//    requires undertow.core;
//    requires org.jboss.logging;
//    requires xnio.api;
//    requires wildfly.common;
//    requires wildfly.client.config;
//    requires slf4j.api;
//    requires logback.classic;
//    requires org.apache.logging.slf4j;
//    requires org.apache.logging.log4j;
//    requires java.sql;
//}

module net.cpollet.gallery {
        requires jcommander;
        requires twitter.text;
        requires jsr305;
        requires spring.jdbc;
        requires spring.tx;
        requires liquibase.core;
        requires com.google.common;
        requires com.fasterxml.jackson.databind;
        requires com.fasterxml.jackson.core;
        requires undertow.core;
        requires slf4j.api;
        requires java.sql;
}
