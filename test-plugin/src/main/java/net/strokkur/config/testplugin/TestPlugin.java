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
package net.strokkur.config.testplugin;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.strokkur.config.testplugin.config.MyCoolConfig;
import net.strokkur.config.testplugin.config.MyCoolConfigImpl;
import net.strokkur.config.testplugin.config.messages.MessagesConfig;
import net.strokkur.config.testplugin.config.messages.MessagesConfigImpl;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;

@NullMarked
public class TestPlugin extends JavaPlugin implements Listener {

    @MonotonicNonNull
    private MessagesConfig messagesConfig;
    
    @MonotonicNonNull
    private MyCoolConfig coolConfig;

    @Override
    public void onLoad() {
        messagesConfig = new MessagesConfigImpl();
        coolConfig = new MyCoolConfigImpl();
        try {
            messagesConfig.reload(this);
            coolConfig.reload(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(
            event -> event.registrar().register(Commands.literal("test")
                .then(Commands.literal("reload")
                    .then(Commands.literal("all")
                        .executes(ctx -> {
                            try {
                                messagesConfig.reload(this);
                                ctx.getSource().getSender().sendMessage(messagesConfig.reloadAll(MiniMessage.miniMessage()));
                            } catch (IOException ex) {
                                ctx.getSource().getSender().sendRichMessage("<red>An exception occurred. See the console for details.");
                                getComponentLogger().error("Failed to reload all configs", ex);
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                    )
                    .then(Commands.literal(MessagesConfig.FILE_PATH)
                        .executes(ctx -> {
                            try {
                                messagesConfig.reload(this);
                                ctx.getSource().getSender().sendMessage(messagesConfig.reload(MiniMessage.miniMessage()));
                            } catch (IOException ex) {
                                ctx.getSource().getSender().sendRichMessage("<red>An exception occurred. See the console for details.");
                                getComponentLogger().error("Failed to reload all configs", ex);
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                    )
                )
                .build()
            )
        ));
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getComponentLogger().info(messagesConfig.pluginStartup(MiniMessage.miniMessage()));
    }

    @Override
    public void onDisable() {
        this.getComponentLogger().info(messagesConfig.pluginShutdown(MiniMessage.miniMessage()));
    }

    @EventHandler
    void onJoin(PlayerJoinEvent event) {
        event.joinMessage(messagesConfig.joinMessage(
            MiniMessage.miniMessage(),
            Placeholder.component("player", event.getPlayer().displayName())
        ));
    }
}
