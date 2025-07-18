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

@ConfigSerializable
@GenerateConfig
@ConfigFilePath("cool.yaml")
@ConfigFormat(Format.YAML_CONFIGURATE)
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
        public String runCommand = "Successfully ran the command!";

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
