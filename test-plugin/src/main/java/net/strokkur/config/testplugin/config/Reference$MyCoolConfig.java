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

import java.io.IOException;
import java.util.List;

/**
 * A config access interface generated from {@link MyCoolConfigModel}.
 *
 * @author Strokkur24 - StrokkConfig
 * @version 1.0.0
 * @see MyCoolConfigModel
 * @see Reference$MyCoolConfigImpl
 */
@NullMarked
public interface Reference$MyCoolConfig {

    //
    // Declared in the @ConfigFileName annotation.
    //

    String FILE_PATH = "config.conf";

    //
    // Reloading
    //

    /**
     * Reload the config file. This method uses the plugin's data path
     * as the target directory and resolves that with the {@link #FILE_PATH}.
     *
     * @param plugin plugin
     */
    default void reload(JavaPlugin plugin) throws IOException {
        reload(plugin, FILE_PATH);
    }

    /**
     * Reload the config file. This method uses the plugin's data path
     * as the target directory and resolves that with the provided file path.
     *
     * @param plugin   plugin
     * @param filePath path to resolve the data path with
     */
    void reload(JavaPlugin plugin, String filePath) throws IOException;

    //
    // Access methods
    //    

    String name();

    List<String> aliases();

    int numberOfExpPerStuff();

    ItemStack itemDefinition(TagResolver... resolvers);

    //
    // Nested classes
    //

    Messages messages();

    interface Messages {
        Component runCommand(MiniMessage mm, TagResolver... resolvers);
    }
}
