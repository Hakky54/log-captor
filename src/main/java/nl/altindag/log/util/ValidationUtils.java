/*
 * Copyright 2019-2021 the original author or authors.
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
        if (!(requiredLogger.isInstance(actualLogger))) {
            String actualLoggerType = actualLogger != null ? actualLogger.getClass().getName() : "nothing";

            throw new IllegalArgumentException(
                    String.format("SLF4J Logger implementation should be of the type [%s] but found [%s].", requiredLogger.getName(), actualLoggerType)
            );
        }
    }

}
