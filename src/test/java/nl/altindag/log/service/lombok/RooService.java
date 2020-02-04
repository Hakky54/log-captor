package nl.altindag.log.service.lombok;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.slf4j.bridge.SLF4JBridgeHandler;

import lombok.extern.java.Log;
import nl.altindag.log.service.LogMessage;
import nl.altindag.log.service.Service;

@Log
public class RooService implements Service {

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        Logger.getLogger(RooService.class.getName()).setLevel(Level.FINEST);
    }

    @Override
    public void sayHello() {
        log.log(Level.INFO, LogMessage.INFO.getMessage());
        log.log(Level.WARNING, LogMessage.WARN.getMessage());
    }

}
