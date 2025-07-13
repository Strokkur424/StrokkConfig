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
