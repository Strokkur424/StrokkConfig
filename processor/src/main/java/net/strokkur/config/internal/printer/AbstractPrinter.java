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
    public SourcePrinter println(String message, Object... format) throws IOException {
        if (writer == null) {
            throw new IOException("No writer set.");
        }

        message = message.stripTrailing();
        if (!message.isBlank()) {
            writer.append(indentString);
            writer.append(message.replace("{}", "%s").formatted(format));
        }
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
