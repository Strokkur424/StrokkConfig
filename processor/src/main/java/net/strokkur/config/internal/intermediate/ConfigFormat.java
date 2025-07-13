package net.strokkur.config.internal.intermediate;

import net.strokkur.config.internal.printer.SourcesPrinter;

public interface ConfigFormat {
    SourcesPrinter getSourcesPrinter();
}
