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

import java.util.Set;

public interface ImplementationSourcePrinter extends SourcePrinter {

    Set<String> STANDARD_IMPL_IMPORTS = Set.of(
        "org.bukkit.plugin.java.JavaPlugin",
        "org.jspecify.annotations.NullMarked",
        "org.jspecify.annotations.Nullable",
        "java.io.IOException",
        "java.nio.file.Files",
        "java.nio.file.Path",
        "java.util.Collections",
        "java.util.List",
        "java.util.function.Function"
    );

    Set<String> getAllImports();

    void printPackage();
    void printImports();
    void printClassJavaDoc();
    void printClassDeclaration();

    void printModelVariable();
    void printNestedSectionsVariables();

    void printReloadMethodImplementation();

    void printAccessMethods();
    void printUtilityMethods();
    void printNestedClasses();
}
