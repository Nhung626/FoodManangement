package com.hivetech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Ine3Application {
    private static final Logger logger = LoggerFactory.getLogger(Ine3Application.class);
    public static void main(String[] args) {
        logger.trace("A TRACE Message");
        logger.debug("A DEBUG Message");
        logger.info("An INFO Message");
        logger.warn("A WARN Message");
        logger.error("An ERROR Message");
        SpringApplication.run(Ine3Application.class, args);
    }

}
