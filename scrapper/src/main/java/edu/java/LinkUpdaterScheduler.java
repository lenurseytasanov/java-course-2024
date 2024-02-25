package edu.java;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class LinkUpdaterScheduler {

    private static final Logger LOGGER = LogManager.getLogger();

    @Scheduled(fixedDelayString = "#{ @scheduler.interval() }")
    public void update() {
        LOGGER.info("get update");
    }
}
