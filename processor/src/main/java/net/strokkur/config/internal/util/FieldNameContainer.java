/*
 * StrokkCommands - A super simple annotation based zero-shade Paper configuration library.
 * Copyright (C) 2025 Strokkur24
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see <https://www.gnu.org/licenses/>.
 */
package net.strokkur.config.internal.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface FieldNameContainer {

    Pattern NAME_CONVERSION_PATTERN = Pattern.compile("[A-Z][^A-Z]+");

    String getFieldName();

    default String getFieldNameDashed() {
        String string = getFieldName();

        Matcher matcher = NAME_CONVERSION_PATTERN.matcher(string);

        final StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            int startIndex = matcher.start();
            int splitIndex;
            if (startIndex == 0) {
                splitIndex = matcher.end();
            } else {
                splitIndex = startIndex;
            }

            if (!builder.toString().isEmpty()) {
                builder.append("-");
            }

            builder.append(string.substring(0, splitIndex).toLowerCase().replaceAll("_", "-"));
            string = string.substring(splitIndex);
            matcher = NAME_CONVERSION_PATTERN.matcher(string);
        }

        String out = builder.toString();
        if (out.isBlank()) {
            // Nothing matched
            return getFieldName().replaceAll("_", "-");
        }

        return builder.toString();
    }
}