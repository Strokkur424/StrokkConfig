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
import net.strokkur.config.internal.util.MessagerWrapper;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

public class SourcePrinterYamlConfigurate extends AbstractImplementationSourcePrinter {

    public SourcePrinterYamlConfigurate(@Nullable Writer writer, ConfigModel model, MessagerWrapper messager) {
        super(writer, model, messager);
    }

    @Override
    protected Set<String> getImplementationImports() {
        return Set.of(
            "org.spongepowered.configurate.CommentedConfigurationNode",
            "org.spongepowered.configurate.ConfigurationOptions",
            "org.spongepowered.configurate.yaml.YamlConfigurationLoader",
            "org.spongepowered.configurate.yaml.NodeStyle"
        );
    }

    @Override
    protected void printImplementationDependantReloadImpl() throws IOException {
        printBlock("""
                YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                    .path(path)
                    .indent(2)
                    .nodeStyle(NodeStyle.BLOCK)
                    .build();
                
                CommentedConfigurationNode node = loader.load(ConfigurationOptions.defaults());
                model = node.get({}.class);
                
                if (!Files.exists(path)) {
                    // If the file doesn't exist, create it
                    model = new {}();
                    node.set(model);
                    loader.save(node);
                }""",
            model.getMetadata().getOriginalClass(),
            model.getMetadata().getOriginalClass(),
            model.getMetadata().getOriginalClass()
        );
    }

    @Override
    protected void printExtra() throws IOException {


    }
}
