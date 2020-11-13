package nl.altindag.log.service.exception;

import nl.altindag.log.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ZooService implements Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZooService.class);

    @Override
    public void sayHello() {
        try {
            tryToSpeak();
        } catch (IOException e) {
            LOGGER.error("Caught unexpected exception", e);
        }
    }

    private void tryToSpeak() throws IOException {
        throw new IOException("KABOOM!");
    }

}
