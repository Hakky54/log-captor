package nl.altindag.log.service.apache;

import nl.altindag.log.service.LogMessage;
import nl.altindag.log.service.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FooService implements Service {

    private static final Logger LOGGER = LogManager.getLogger(FooService.class);

    @Override
    public void sayHello() {
        LOGGER.info(LogMessage.INFO.getMessage());
        LOGGER.warn(LogMessage.WARN.getMessage());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(LogMessage.DEBUG.getMessage());
        }
    }

}
