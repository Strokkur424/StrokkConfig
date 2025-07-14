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

import net.strokkur.config.internal.intermediate.ConfigField;
import net.strokkur.config.internal.intermediate.ConfigSection;

import java.io.IOException;
import java.util.Set;

public interface InterfaceSourcePrinter extends SharedSourcePrinter {

    Set<String> STANDARD_INTERFACE_IMPORTS = Set.of(
        "org.bukkit.plugin.java.JavaPlugin",
        "org.jspecify.annotations.NullMarked",
        "java.io.IOException"
    );

    @Override
    default Set<String> getStandardImports() {
        return STANDARD_INTERFACE_IMPORTS;
    }

    void printClassJavaDoc() throws IOException;
    void printClassDeclaration() throws IOException;

    void printFilePath() throws IOException;
    void printReloading() throws IOException;
    void printAccessMethods() throws IOException;
    void printNestedInterfaces() throws IOException;

    void printAccessMethod(ConfigField field) throws IOException;
    void printSectionInterface(ConfigSection type) throws IOException;
}
