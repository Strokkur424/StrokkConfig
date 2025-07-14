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
