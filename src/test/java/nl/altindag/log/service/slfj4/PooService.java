package nl.altindag.log.service.slfj4;

import nl.altindag.log.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PooService implements Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(PooService.class);

    @Override
    public void sayHello() {
        LOGGER.error("Keyboard not responding. Press {} key to continue...", "Enter");
    }

}
