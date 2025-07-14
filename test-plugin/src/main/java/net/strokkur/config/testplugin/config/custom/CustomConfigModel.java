package net.strokkur.config.testplugin.config.custom;

import net.kyori.adventure.key.InvalidKeyException;
import net.kyori.adventure.key.Key;
import net.strokkur.config.Format;
import net.strokkur.config.annotations.ConfigFilePath;
import net.strokkur.config.annotations.ConfigFormat;
import net.strokkur.config.annotations.CustomDeserializer;
import net.strokkur.config.annotations.CustomParse;
import net.strokkur.config.annotations.CustomSerializer;
import net.strokkur.config.annotations.GenerateConfig;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;

@GenerateConfig("CustomConfig")
@ConfigFormat(Format.CUSTOM)
@ConfigFilePath("custom/stuff.properties")
@NullMarked
class CustomConfigModel {

    public String firstConfigValue = "Some cool value";

    public String secondConfigValue = "Another cool value";

    @CustomParse("parseItemType")
    public String andAMaterialForGoodMeasure = ItemType.FLINT_AND_STEEL.key().value();

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
}
