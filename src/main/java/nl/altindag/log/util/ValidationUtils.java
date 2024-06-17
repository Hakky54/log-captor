/*
 * Copyright 2019 Thunderberry.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.altindag.log.util;

import nl.altindag.log.exception.LogCaptorException;
import org.slf4j.Logger;

/**
 * <strong>NOTE:</strong>
 * Please don't use this class directly as it is part of the internal API. Class name and methods can be changed any time.
 *
 * @author Hakan Altindag
 */
public final class ValidationUtils {

    private ValidationUtils() {}

    public static <T extends Logger, U extends Logger> void requireLoggerOfType(T actualLogger, Class<U> requiredLogger) {
        if (actualLogger == null || !requiredLogger.getCanonicalName().equals(actualLogger.getClass().getCanonicalName())) {
            String actualLoggerType = actualLogger != null ? actualLogger.getClass().getName() : "nothing";

            throw new LogCaptorException(
                    String.format("SLF4J Logger implementation should be of the type [%s] but found [%s].", requiredLogger.getName(), actualLoggerType)
            );
        } else if (!(requiredLogger.isInstance(actualLogger))) {
            throw new LogCaptorException(
                    String.format(
                            "Multiple classloaders are being used. The Logging API is created by the following classloader: [%s], " +
                            "while it should have been created by the following classloader: [%s].",
                            actualLogger.getClass().getClassLoader().getClass().getName(),
                            requiredLogger.getClassLoader().getClass().getName()
                    )
            );
        }
    }

}
