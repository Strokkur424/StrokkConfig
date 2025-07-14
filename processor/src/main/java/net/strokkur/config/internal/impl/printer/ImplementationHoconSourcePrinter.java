package net.strokkur.config.internal.impl.printer;

import net.strokkur.config.internal.intermediate.ConfigModel;
import net.strokkur.config.internal.util.MessagerWrapper;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

public class ImplementationHoconSourcePrinter extends AbstractImplementationSourcePrinter {

    public ImplementationHoconSourcePrinter(@Nullable Writer writer, ConfigModel model, MessagerWrapper messager) {
        super(writer, model, messager);
    }

    @Override
    Set<String> getImplementationImports() {
        return Set.of(
            "org.spongepowered.configurate.CommentedConfigurationNode",
            "org.spongepowered.configurate.ConfigurationOptions",
            "org.spongepowered.configurate.hocon.HoconConfigurationLoader"
        );
    }

    @Override
    protected void printImplementationDependantReloadImpl() throws IOException {
        printBlock("""
            HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                        .path(path)
                        .emitComments(true)
                        .prettyPrinting(true)
                        .indent(2)
                        .build();
            
            CommentedConfigurationNode node = loader.createNode(ConfigurationOptions.defaults());
            model = node.get(MyCoolConfigModel.class);
            
            if (model == null && !Files.exists(path)) {
                // If the file doesn't exist, even after an attempted copy, create it anew and save it to disk
                model = new MyCoolConfigModel();
                node.set(model);
                loader.save(node);
            }""");
    }
}
