package net.strokkur.config.internal.printer;

import net.strokkur.config.internal.intermediate.ConfigModel;
import net.strokkur.config.internal.util.MessagerWrapper;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.function.Function;

public class JavaFilePrinter {
    
    private final ConfigModel model;
    private final Function<Writer, SourcePrinter> sourcePrinter;
    private final String sourceName;
    private final Filer filer;
    private final MessagerWrapper messagerWrapper;

    public JavaFilePrinter(ConfigModel model, Function<Writer, SourcePrinter> sourcePrinter, String sourcePath, Filer filer, MessagerWrapper messagerWrapper) {
        this.model = model;
        this.sourcePrinter = sourcePrinter;
        this.sourceName = sourcePath;
        this.filer = filer;
        this.messagerWrapper = messagerWrapper;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void print() {
        try {
            String targetImplementationFile = model.getMetadata().getPackage() + "." + sourceName;
            JavaFileObject interfaceObj = filer.createSourceFile(targetImplementationFile);

            try (PrintWriter writer = new PrintWriter(interfaceObj.openWriter())) {
                sourcePrinter.apply(writer).print();
            }
        } catch (Exception e) {
            messagerWrapper.error("Fatal exception occurred while printing source file for {}: {}", sourceName, e.getMessage());
            e.printStackTrace();
        }
    }
}
