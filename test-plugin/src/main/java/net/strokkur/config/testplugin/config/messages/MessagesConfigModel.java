package net.strokkur.config.testplugin.config.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.strokkur.config.annotations.ConfigFilePath;
import net.strokkur.config.annotations.CustomParse;
import net.strokkur.config.annotations.GenerateConfig;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@GenerateConfig("MessagesConfig")
@ConfigFilePath("messages.conf")
@ConfigSerializable
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
