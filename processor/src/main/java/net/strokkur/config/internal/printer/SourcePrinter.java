package net.strokkur.config.internal.printer;

import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.Writer;

public interface SourcePrinter {

    String INDENTATION = "\s\s\s\s";

    void print() throws IOException;

    void setWriter(@Nullable Writer writer);
    @Nullable Writer getWriter();

    void incrementIndent();
    void decrementIndent();

    SourcePrinter print(String message, Object... format) throws IOException;
    SourcePrinter println(String message, Object... format) throws IOException;
    SourcePrinter println() throws IOException;
}
