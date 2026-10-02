package com.glamourglow.glamourglow.services.logservice;

import java.io.IOException;
import java.util.logging.*;

import com.glamourglow.glamourglow.services.config.Configuration;

public class LogService {

    private static Logger applicationLogger;

    private LogService() {
    }

    // Metodo per azzerare lo stato statico durante i test
    public static void resetLogger() {
        applicationLogger = null;
    }

    public static Logger getApplicationLogger() {

        SimpleFormatter formatterTxt;
        Handler fileHandler;

        try {

            if (applicationLogger == null) {

                applicationLogger = Logger.getLogger(Configuration.GLOBAL_LOGGER_NAME);
                fileHandler = new FileHandler(Configuration.GLOBAL_LOGGER_FILE, true);
                formatterTxt = new SimpleFormatter();
                fileHandler.setFormatter(formatterTxt);
                applicationLogger.addHandler(fileHandler);
                applicationLogger.setLevel(Configuration.GLOBAL_LOGGER_LEVEL);
                applicationLogger.setUseParentHandlers(false);
                applicationLogger.log(Level.CONFIG, "Logger: {0} created.", applicationLogger.getName());

            }

        } catch (IOException e) {
            if (applicationLogger != null) {
                applicationLogger.log(Level.SEVERE, "Error occured in Logger creation", e);
            }
            applicationLogger = null; // Ripristina a null se la creazione fallisce
            throw new RuntimeException(e);
        }
        return applicationLogger;

    }

}