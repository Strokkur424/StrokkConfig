package net.strokkur.config.internal.printer;

import net.strokkur.config.internal.intermediate.ConfigField;
import net.strokkur.config.internal.intermediate.FieldType;

import java.util.Set;

public interface InterfaceSourcePrinter extends SourcePrinter {

    Set<String> STANDARD_INTERFACE_IMPORTS = Set.of(
        "org.bukkit.plugin.java.JavaPlugin",
        "org.jspecify.annotations.NullMarked",
        "java.io.IOException"
    );

    Set<String> getAllImports();

    void printPackage();
    void printImports();
    void printInterfaceJavaDoc();
    void printInterfaceDeclaration();

    void printFilePath();
    void printReloading();
    void printAccessMethods();
    void printNestedClasses();

    void printNestedClassesAccessMethods();
    void printNestedClassesDefinitions();

    void printAccessMethod(ConfigField field);
    void printNestedClass(FieldType type);
}
