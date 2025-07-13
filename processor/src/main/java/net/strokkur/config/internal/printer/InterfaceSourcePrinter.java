package net.strokkur.config.internal.printer;

import net.strokkur.config.internal.intermediate.ConfigField;
import net.strokkur.config.internal.intermediate.ConfigSection;

import java.io.IOException;
import java.util.Set;

public interface InterfaceSourcePrinter extends SourcePrinter {

    Set<String> STANDARD_INTERFACE_IMPORTS = Set.of(
        "org.bukkit.plugin.java.JavaPlugin",
        "org.jspecify.annotations.NullMarked",
        "java.io.IOException"
    );

    Set<String> getAllImports();

    void printPackage() throws IOException;
    void printImports() throws IOException;
    void printInterfaceJavaDoc() throws IOException;
    void printInterfaceDeclaration() throws IOException;

    void printFilePath() throws IOException;
    void printReloading() throws IOException;
    void printAccessMethods() throws IOException;
    void printNestedInterfaces() throws IOException;

    void printAccessMethod(ConfigField field) throws IOException;
    void printSectionInterface(ConfigSection type) throws IOException;
}
