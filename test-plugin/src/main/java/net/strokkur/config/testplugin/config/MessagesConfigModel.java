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
import net.strokkur.config.Format;
import net.strokkur.config.annotations.ConfigFormat;
import net.strokkur.config.annotations.CustomParse;
import net.strokkur.config.annotations.GenerateConfig;
import org.jspecify.annotations.NullMarked;

@GenerateConfig
@ConfigFormat(Format.YAML_SNAKEYAML)
@NullMarked
public class MessagesConfigModel {

    @CustomParse("parseToMiniMessage")
    public String pluginStartup = "The plugin has been started!";

    @CustomParse("parseToMiniMessage")
    public String pluginShutdown = "The plugin has been shut down!";

    @CustomParse("parseToMiniMessage")
    public String reload = "Successfully reloaded the <config> config!";

    @CustomParse("parseToMiniMessage")
    public String reloadAll = "Successfully reloaded all configs!";

    @CustomParse("parseToMiniMessage")
    public String joinMessage = "<white><b>[<green>+</green>] </white><gray>Welcome <color:#ab79ba><playername></color> to the server!";

    public Component parseToMiniMessage(String message, MiniMessage mm, TagResolver... resolvers) {
        return mm.deserialize(message, resolvers);
    }
}
