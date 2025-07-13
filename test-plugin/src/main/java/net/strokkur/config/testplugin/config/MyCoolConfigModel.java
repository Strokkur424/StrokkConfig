package net.strokkur.config.testplugin.config;

import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.InvalidKeyException;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.strokkur.config.Format;
import net.strokkur.config.annotations.ConfigFilePath;
import net.strokkur.config.annotations.ConfigFormat;
import net.strokkur.config.annotations.ConfigNonNull;
import net.strokkur.config.annotations.CustomParse;
import net.strokkur.config.annotations.CustomType;
import net.strokkur.config.annotations.CustomTypeReturn;
import net.strokkur.config.annotations.GenerateConfig;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

@ConfigSerializable // Needed by Configurate
@GenerateConfig("MyCoolConfig")
@ConfigFilePath("config.conf")
@ConfigFormat(Format.HOCON)
@ConfigNonNull
class MyCoolConfigModel {

    public String name = "mpty";
    public List<String> aliases = List.of("pt");
    public int amountOfExpPerStuff = 200;
    public ItemDefinition itemDefinition = new ItemDefinition("diamond_sword", "<red><b>Destroyer 9000");
    
    public Messages messages = new Messages();

    @ConfigSerializable
    public static class Messages {
        
        @CustomParse("parseToMiniMessage")
        public String runCommand;
        
        public Component parseToMiniMessage(String message, MiniMessage mm, TagResolver... resolvers) {
            return mm.deserialize(message, resolvers);
        }
    }
    
    @SuppressWarnings("UnstableApiUsage")
    @CustomType
    @ConfigSerializable
    public static class ItemDefinition {

        public String type;
        public String name;

        public ItemDefinition() {
            this(null, null);
        }

        public ItemDefinition(String type, String name) {
            this.type = type;
            this.name = name;
        }

        @CustomTypeReturn
        public ItemStack construct(TagResolver... resolvers) {
            if (type == null) {
                return ItemType.AIR.createItemStack();
            }

            ItemStack stack;
            try {
                Key key = Key.key(type);
                ItemType type = Registry.ITEM.get(key);
                if (type == null) {
                    return ItemType.AIR.createItemStack();
                }

                stack = type.createItemStack();
            } catch (InvalidKeyException e) {
                // This key is not valid, therefor we return air.
                return ItemType.AIR.createItemStack();
            }

            if (name != null) {
                stack.setData(DataComponentTypes.ITEM_NAME, miniMessage().deserialize(name, resolvers));
            }

            return stack;
        }
    }
}
