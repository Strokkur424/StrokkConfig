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
package net.strokkur.config.testplugin.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
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

    @Nullable
    private Messages messagesModel = null;

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

        if (model.messages == null) {
            throw new IOException("Failed to load 'messages' section for '" + path + "'.");
        }

        messagesModel = new MessagesImpl(model.messages);
    }

    //
    // Access methods
    //

    @Override
    public String name() {
        return getNonNull(validateLoaded(this.model), m -> m.name, "name");
    }

    @Override
    public List<String> aliases() {
        return getNonNull(validateLoaded(this.model), m -> Collections.unmodifiableList(m.aliases), "aliases");
    }

    @Override
    public int numberOfExpPerStuff() {
        return validateLoaded(this.model).amountOfExpPerStuff;
    }

    @Override
    public ItemStack itemDefinition(TagResolver... resolvers) {
        return getNonNull(validateLoaded(this.model), m -> m.itemDefinition.construct(resolvers), "item-definition");
    }

    //
    // Utility methods
    //

    private static <M> M validateLoaded(@Nullable M model) {
        if (model == null) {
            throw new IllegalStateException("The config file '" + FILE_PATH + "' is not fully loaded.");
        }
        return model;
    }

    private static <M, T> T getNonNull(M model, Function<M, T> function, String name) {
        T result = function.apply(model);
        if (result == null) {
            throw new IllegalStateException("The value for '" + name + "' in the config file '" + FILE_PATH + "' is not declared.");
        }
        return result;
    }

    //
    // Nested classes
    //

    @Override
    public Messages messages() {
        return validateLoaded(this.messagesModel);
    }

    private record MessagesImpl(MyCoolConfigModel.Messages handle) implements Messages {

        @Override
        public Component runCommand(MiniMessage mm, TagResolver... resolvers) {
            return getNonNull(handle(), m -> m.parseToMiniMessage(m.runCommand, mm, resolvers), "messages.run-command");
        }
    }
}
