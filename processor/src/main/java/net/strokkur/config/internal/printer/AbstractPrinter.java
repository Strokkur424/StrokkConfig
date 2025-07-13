package net.strokkur.config.internal.printer;

import org.intellij.lang.annotations.Language;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.Writer;

@SuppressWarnings("UnusedReturnValue")
public abstract class AbstractPrinter implements SourcePrinter {

    protected @Nullable Writer writer;
    protected String indentString;
    protected int indent;

    public AbstractPrinter(int indent, @Nullable Writer writer) {
        this.writer = writer;
        this.indent = indent;
        this.indentString = INDENTATION.repeat(this.indent);
    }

    public AbstractPrinter(@Nullable Writer writer) {
        this(0, writer);
    }

    @Override
    @Nullable
    public Writer getWriter() {
        return this.writer;
    }

    @Override
    public void setWriter(@Nullable Writer writer) {
        this.writer = writer;
    }

    @Override
    public void incrementIndent() {
        this.indent++;
        this.indentString = INDENTATION.repeat(this.indent);
    }

    @Override
    public void decrementIndent() {
        if (indent == 0) {
            return;
        }

        this.indent--;
        this.indentString = INDENTATION.repeat(this.indent);
    }
    
    

    @Override
    public SourcePrinter print(String message, Object... format) throws IOException {
        if (writer == null) {
            throw new IOException("No writer set.");
        }

        writer.append(message.replace("{}", "%s").formatted(format));
        return this;
    }

    @Override
    public SourcePrinter println(String message, Object... format) throws IOException {
        if (writer == null) {
            throw new IOException("No writer set.");
        }

        writer.append(indentString);
        writer.append(message.replace("{}", "%s").formatted(format));
        writer.append("\n");
        return this;
    }

    @Override
    public SourcePrinter println() throws IOException {
        if (writer == null) {
            throw new IOException("No writer set.");
        }

        writer.append("\n");
        return this;
    }

    @Override
    public SourcePrinter printBlock(String block, Object... format) throws IOException {
        if (writer == null) {
            throw new IOException("No writer set.");
        }
        
        String parsedBlock = block.replace("{}", "%s").formatted(format);
        for (@Language("JAVA") String line : parsedBlock.split("\n")) {
            println(line);
        }
        
        return this;
    }
}
