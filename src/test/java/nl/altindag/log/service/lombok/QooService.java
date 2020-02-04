package nl.altindag.log.service.lombok;

import lombok.extern.slf4j.Slf4j;
import nl.altindag.log.service.LogMessage;
import nl.altindag.log.service.Service;

@Slf4j
public class QooService implements Service {

    @Override
    public void sayHello() {
        log.info(LogMessage.INFO.getMessage());
        log.warn(LogMessage.WARN.getMessage());
        log.debug(LogMessage.DEBUG.getMessage());
    }

}
