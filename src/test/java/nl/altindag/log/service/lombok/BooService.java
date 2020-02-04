package nl.altindag.log.service.lombok;

import lombok.extern.log4j.Log4j2;
import nl.altindag.log.service.LogMessage;
import nl.altindag.log.service.Service;

@Log4j2
public class BooService implements Service {

    @Override
    public void sayHello() {
        log.info(LogMessage.INFO.getMessage());
        log.warn(LogMessage.WARN.getMessage());
        log.debug(LogMessage.DEBUG.getMessage());
    }

}
