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
package net.strokkur.config.internal.impl.printer;

import net.strokkur.config.internal.intermediate.ConfigModel;
import net.strokkur.config.internal.intermediate.CustomSerializers;
import net.strokkur.config.internal.util.MessagerWrapper;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

public class SourcePrinterCustom extends AbstractImplementationSourcePrinter {

    public SourcePrinterCustom(@Nullable Writer writer, ConfigModel model, MessagerWrapper messager) {
        super(writer, model, messager);
    }

    @Override
    Set<String> getImplementationImports() {
        return Set.of();
    }

    @Override
    protected void printImplementationDependantReloadImpl() throws IOException {
        CustomSerializers serializers = model.getMetadata().getCustomSerializers();
        if (serializers == null) {
            throw new IllegalStateException("Tried to print custom serialization without custom serializers declared.");
        }

        printBlock("""
                if (!Files.exists(path)) {
                    // If the file doesn't exist, create it
                    model = new {}();
                    String serialized = {}.{}(model);
                    Files.createDirectories(path.getParent());
                    Files.writeString(path, serialized);
                } else {
                    // If the file exists, load it
                    String serialized = Files.readString(path);
                    model = {}.{}(serialized);
                }""",
            model.getMetadata().getOriginalClass(),
            model.getMetadata().getOriginalClass(),
            serializers.getSerializationMethod().getSimpleName().toString(),
            model.getMetadata().getOriginalClass(),
            serializers.getDeserializationMethod().getSimpleName().toString()
        );
    }
}
