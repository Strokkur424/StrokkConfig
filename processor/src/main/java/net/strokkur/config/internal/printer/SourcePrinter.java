package net.strokkur.config.internal.printer;

import org.intellij.lang.annotations.Language;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.Writer;

public interface SourcePrinter {

    String INDENTATION = "\s\s\s\s";

    void print() throws IOException;
    @Nullable
    Writer getWriter();
    void setWriter(@Nullable Writer writer);
    void incrementIndent();
    void decrementIndent();

    SourcePrinter print(@Language("JAVA") String message, Object... format) throws IOException;
    SourcePrinter println(@Language("JAVA") String message, Object... format) throws IOException;
    SourcePrinter println() throws IOException;
    
    SourcePrinter printBlock(@Language("JAVA") String block, Object... format) throws IOException;
}
