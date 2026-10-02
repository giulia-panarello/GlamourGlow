package com.glamourglow.glamourglow.services.logservice;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Handler;
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

        Field field = LogService.class.getDeclaredField("applicationLogger");
        field.setAccessible(true);
        field.set(null, null);

        // Uso di percorsi relativi alla cartella di lavoro corrente
        Path logPath = Paths.get("ecommerce_log.0.0.txt");
        Path backupPath = Paths.get("ecommerce_log.0.0.txt.test-backup");

        boolean fileEsistente = Files.exists(logPath);

        try {

            if (fileEsistente) {
                Files.move(logPath, backupPath);
            }

            // Crea una directory con lo stesso nome del file per simulare l'errore di I/O
            Files.createDirectory(logPath);

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    LogService::getApplicationLogger
            );

            assertNotNull(exception.getCause());
            assertTrue(
                    exception.getCause() instanceof java.io.IOException
            );

        } finally {

            if (Files.isDirectory(logPath)) {
                Files.delete(logPath);
            }

            if (fileEsistente && Files.exists(backupPath)) {
                Files.move(backupPath, logPath);
            }

            for (Handler handler : logger.getHandlers()) {
                handler.close();
                logger.removeHandler(handler);
            }

            field.set(null, null);
        }
    }
}