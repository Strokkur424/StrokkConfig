package net.strokkur.config.internal.impl.printer;

import net.strokkur.config.internal.intermediate.ConfigModel;
import net.strokkur.config.internal.util.MessagerWrapper;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

public class SourcePrinterGson extends AbstractImplementationSourcePrinter {

    public SourcePrinterGson(@Nullable Writer writer, ConfigModel model, MessagerWrapper messager) {
        super(writer, model, messager);
    }

    @Override
    Set<String> getImplementationImports() {
        return Set.of(
            "com.google.gson.Gson",
            "com.google.gson.GsonBuilder",
            "com.google.gson.Strictness"
        );
    }

    @Override
    protected void printImplementationDependantReloadImpl() throws IOException {
        printBlock("""
                Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .disableHtmlEscaping()
                    .setStrictness(Strictness.LENIENT)
                    .create();
                
                if (!Files.exists(path)) {
                    // If the file doesn't exist, create it
                    model = new {}();
                    String json = gson.toJson(model);
                    Files.createDirectories(path.getParent());
                    Files.writeString(path, json);
                } else {
                    // If the file exists, load it
                    String json = Files.readString(path);
                    model = gson.fromJson(json, {}.class);
                }""",
            model.getMetadata().getOriginalClass(),
            model.getMetadata().getOriginalClass()
        );
    }
}
