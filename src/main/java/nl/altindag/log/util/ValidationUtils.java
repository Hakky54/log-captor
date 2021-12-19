package nl.altindag.log.util;

import org.slf4j.Logger;

/**
 * <strong>NOTE:</strong>
 * Please don't use this class directly as it is part of the internal API. Class name and methods can be changed any time.
 *
 * @author Hakan Altindag
 */
public final class ValidationUtils {

    private ValidationUtils() {}

    public static <T extends Logger> void requireLoggerOfType(Logger actualLogger, Class<T> requiredLogger) {
        if (!(requiredLogger.isInstance(actualLogger))) {
            String actualLoggerType = actualLogger != null ? actualLogger.getClass().getName() : "nothing";

            throw new IllegalArgumentException(
                    String.format("SLF4J Logger implementation should be of the type [%s] but found [%s].", requiredLogger.getName(), actualLoggerType)
            );
        }
    }

}
