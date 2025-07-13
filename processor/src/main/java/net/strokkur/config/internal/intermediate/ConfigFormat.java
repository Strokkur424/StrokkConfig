package net.strokkur.config.internal.intermediate;

import net.strokkur.config.Format;
import net.strokkur.config.internal.exceptions.ProcessorException;
import net.strokkur.config.internal.printer.SourcePrinter;

public interface ConfigFormat {
    static ConfigFormat getFromEnum(Format format) throws ProcessorException {
        return switch (format) {
            case HOCON -> null;
            default -> throw new ProcessorException("Failed to find implementation for format '" + format + '.');
        };
    }
    SourcePrinter getSourcesPrinter();
    String defaultExtension();
}
