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

import net.kyori.adventure.key.InvalidKeyException;
import net.kyori.adventure.key.Key;
import net.strokkur.config.ConfigFilePath;
import net.strokkur.config.ConfigFormat;
import net.strokkur.config.CustomDeserializer;
import net.strokkur.config.CustomParse;
import net.strokkur.config.CustomSerializer;
import net.strokkur.config.GenerateConfig;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;

@GenerateConfig
@ConfigFormat(ConfigFormat.Format.CUSTOM)
@ConfigFilePath("custom/stuff.properties")
@NullMarked
class CustomConfigModel {

  public String firstConfigValue = "Some cool value";

  public String secondConfigValue = "Another cool value";

  @CustomParse("parseItemType")
  public String andAMaterialForGoodMeasure = ItemType.FLINT_AND_STEEL.key().value();

  @CustomSerializer
  static String serialize(CustomConfigModel model) throws IOException {
    Properties properties = new Properties(3);
    properties.put("first", model.firstConfigValue);
    properties.put("second", model.secondConfigValue);
    properties.put("material", model.andAMaterialForGoodMeasure);

    StringWriter writer = new StringWriter();
    properties.store(writer, "A very cool properties file.");

    return writer.toString();
  }

  @CustomDeserializer
  static CustomConfigModel deserialize(String serialized) throws IOException {
    Properties properties = new Properties(3);
    properties.load(new StringReader(serialized));

    CustomConfigModel model = new CustomConfigModel();
    model.firstConfigValue = properties.getProperty("first", model.firstConfigValue);
    model.secondConfigValue = properties.getProperty("second", model.secondConfigValue);
    model.andAMaterialForGoodMeasure = properties.getProperty("material", model.andAMaterialForGoodMeasure);

    return model;
  }

  public ItemType parseItemType(String typeString) {
    Key key;
    try {
      key = Key.key(typeString);
    } catch (InvalidKeyException keyFormatException) {
      throw new IllegalStateException("The input string '" + typeString + "' is not a valid item type.");
    }

    ItemType type = Registry.ITEM.get(key);
    if (type == null) {
      throw new IllegalStateException("The input string '" + typeString + "' is not a valid item type.");
    }

    return type;
  }
}
