package nl.altindag.log.service.lombok;

import lombok.extern.log4j.Log4j;
import nl.altindag.log.service.LogMessage;
import nl.altindag.log.service.Service;

@Log4j
public class WooService implements Service {

    @Override
    public void sayHello() {
        log.info(LogMessage.INFO.getMessage());
        log.warn(LogMessage.WARN.getMessage());
        if (log.isDebugEnabled()) {
            log.debug(LogMessage.DEBUG.getMessage());
        }
    }

}
