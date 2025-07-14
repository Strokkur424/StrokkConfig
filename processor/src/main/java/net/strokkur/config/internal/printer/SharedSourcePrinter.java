package net.strokkur.config.internal.printer;

import java.io.IOException;
import java.util.Set;

public interface SharedSourcePrinter extends SourcePrinter {

    Set<String> getAllImports();
    Set<String> getStandardImports();

    void printPackage() throws IOException;
    void printImports() throws IOException;
    void printClassJavaDoc() throws IOException;
    void printClassDeclaration() throws IOException;
}
