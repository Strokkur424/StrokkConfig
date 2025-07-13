package net.strokkur.config.internal.intermediate;

import org.jspecify.annotations.Nullable;

import javax.lang.model.element.ExecutableElement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Internal representation of a configuration field.
 */
public interface ConfigField {

    Pattern NAME_CONVERSION_PATTERN = Pattern.compile("[A-Z][^A-Z]+");

    ConfigType getFieldType();

    String getFieldName();

    @Nullable
    ExecutableElement getCustomParseMethod();

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