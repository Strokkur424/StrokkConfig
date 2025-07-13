package net.strokkur.config.testplugin.config;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * The generated implementation for the {@link Reference$MyCoolConfig} interface.
 *
 * @author Strokkur24 - StrokkConfig
 * @version 1.0.0
 * @see MyCoolConfigModel
 * @see Reference$MyCoolConfig generated interface
 */
@NullMarked
public class Reference$MyCoolConfigImpl implements Reference$MyCoolConfig {

    @Nullable
    private MyCoolConfigModel model = null;

    //
    // Reloading
    //

    @Override
    public void reload(JavaPlugin plugin, String filePath) throws IOException {
        final Path path = plugin.getDataPath().resolve(filePath);

        if (!Files.exists(path)) {
            plugin.saveResource(filePath, false);
        }

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
        }

        if (model == null) {
            // If the model is still null, throw an exception
            throw new IOException("Failed to load configuration model for '" + path + "'.");
        }
    }

    //
    // Access methods
    //

    @Override
    public String name() {
        return getNonNull(m -> m.name, "name");
    }

    @Override
    public List<String> aliases() {
        return getNonNull(m -> Collections.unmodifiableList(m.aliases), "aliases");
    }

    @Override
    public int numberOfExpPerStuff() {
        return validateLoaded().amountOfExpPerStuff;
    }

    @Override
    public ItemStack itemDefinition(TagResolver... resolvers) {
        return getNonNull(m -> m.itemDefinition.construct(resolvers), "item-definition");
    }

    //
    // Utility methods
    //

    private MyCoolConfigModel validateLoaded() {
        MyCoolConfigModel model = this.model;
        if (model == null) {
            throw new IllegalStateException("The config file '" + FILE_PATH + "' is not loaded.");
        }
        return model;
    }

    private <T> T getNonNull(Function<MyCoolConfigModel, T> function, String name) {
        T result = function.apply(validateLoaded());
        if (result == null) {
            throw new IllegalStateException("The value for '" + name + "' in the config file '" + FILE_PATH + "' is not declared.");
        }
        return result;
    }
}
