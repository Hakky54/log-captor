package nl.altindag.log.service.apache;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.altindag.log.service.LogMessage;
import nl.altindag.log.service.Service;

public class FooService implements Service {

    private static final Logger LOGGER = LogManager.getLogger(FooService.class);

    @Override
    public void sayHello() {
        LOGGER.info(LogMessage.INFO.getMessage());
        LOGGER.warn(LogMessage.WARN.getMessage());
        LOGGER.debug(LogMessage.DEBUG.getMessage());
    }

}
