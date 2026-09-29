package com.glamourglow.glamourglow.services.logservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class LogServiceTest {

    @Test
    void getApplicationLogger_erroreCreazioneFile_generaRuntimeException()
            throws Exception {

        Logger logger = Logger.getLogger("rubrica");

        for (Handler handler : logger.getHandlers()) {
            handler.close();
            logger.removeHandler(handler);
        }


        Field field =
                LogService.class.getDeclaredField("applicationLogger");
        field.setAccessible(true);
        field.set(null, null);

        java.nio.file.Path logPath =
                java.nio.file.Paths.get(
                        "/Users/giuliapanarello/Desktop/ProgettoSistemiWeb",
                        "ecommerce_log.0.0.txt"
                );


        java.nio.file.Path backupPath =
                java.nio.file.Paths.get(
                        "/Users/giuliapanarello/Desktop/ProgettoSistemiWeb",
                        "ecommerce_log.0.0.txt.test-backup"
                );

        boolean fileEsistente = java.nio.file.Files.exists(logPath);

        try {

            if (fileEsistente) {
                java.nio.file.Files.move(logPath, backupPath);
            }


            java.nio.file.Files.createDirectory(logPath);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    LogService::getApplicationLogger
            );


            assertNotNull(exception.getCause());
            assertTrue(
                    exception.getCause() instanceof java.io.IOException
            );

        } finally {


            if (java.nio.file.Files.isDirectory(logPath)) {
                java.nio.file.Files.delete(logPath);
            }


            if (fileEsistente && java.nio.file.Files.exists(backupPath)) {
                java.nio.file.Files.move(backupPath, logPath);
            }


            for (Handler handler : logger.getHandlers()) {
                handler.close();
                logger.removeHandler(handler);
            }

            field.set(null, null);
        }
    }
}