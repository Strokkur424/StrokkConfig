# StrokkConfig

*The ultimate wrapper config wrapper library for Paper-based Minecraft servers*.

## What is this

StrokkConfig is a fully annotation-based zero-shade wrapper library for easier
config management.

You can define a config very easily by creating a **model class**:

```java
package net.strokkur.config.testplugin.config.simple;

import net.strokkur.config.Format;
import net.strokkur.config.annotations.ConfigFormat;
import net.strokkur.config.annotations.GenerateConfig;

@GenerateConfig("SimpleConfig")
@ConfigFormat(Format.YAML_SNAKEYAML)
class SimpleConfigModel {
    String helloWorld = "Hello, Config!";
}
```

This model class can contain an arbitrary number of fields. It even supports custom
types, sections, and custom parse methods for preprocessing your config field.

When the annotation processor runs, it will generate two files:

- `SimpleConfig.java`
- `SimpleConfigImpl.java`

The `SimpleConfig` interface just contains the method definitions for accessing your fields
and reloading the config. For our small example, it would look like this
(this file was actually generated):

```java
package net.strokkur.config.testplugin.config.simple;

import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;

/**
 * A config access interface generated from {@link SimpleConfigModel}.
 *
 * @author Strokkur24 - StrokkConfig
 * @version 1.0.0
 * @see SimpleConfigModel
 * @see SimpleConfigImpl
 */
@NullMarked
public interface SimpleConfig {

    //
    // Declared in the @ConfigFileName annotation.
    //

    String FILE_PATH = "config.yaml";

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

    String helloWorld();
}
```

It contains a read-only way to access the field on the config model.
The implementation is generated the same.

To access your config, you can, either via singleton pattern, by passing
the instance around, or with dependency injection access the `SimpleConfig`
interface, which should be backed by the `SimpleConfigImpl`. You may also
choose to extend the `SimpleConfigImpl` or flat out write your own implementation
for the `SimpleConfig` interface.

Once you got your instance, you can store it in your main class with a simple getter:

```java

@NullMarked
public class TestPlugin extends JavaPlugin implements Listener {

    @MonotonicNonNull
    private SimpleConfig simpleConfig;

    @Override
    public void onLoad() {
        simpleConfig = new SimpleConfigImpl();
        try {
            simpleConfig.reload(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SimpleConfig getSimpleConfig() {
        return simpleConfig;
    }
}
```

## Declaring the dependency

```kts
repositories {
    maven("https://eldonexus.de/repository/maven-public/")
}

dependencies {
    compileOnly("net.strokkur:strokk-config-annotations:1.0.1")
    annotationProcessor("net.strokkur:strokk-config-processor:1.0.1")
}
```

## Custom field parsers

StrokkCommand provides you with the option to parse fields into a different type
inside your config model before you return it. This can be useful for message
configs, which might have to do placeholder replacing or MiniMessage parsing.

For example, you can declare your config model like this:

```java

@GenerateConfig("MessagesConfig")
@NullMarked
class MessagesConfigModel {

    @CustomParse("parseToMiniMessage")
    public String pluginStartup = "<#ab79ba>The plugin has been started!";

    @CustomParse("parseToMiniMessage")
    public String pluginShutdown = "<#ab79ba>The plugin has been shut down!";

    @CustomParse("parseToMiniMessage")
    public String reload = "<#ab79ba>Successfully reloaded the <config> config!";

    @CustomParse("parseToMiniMessage")
    public String reloadAll = "<#ab79ba>Successfully reloaded all configs!";

    @CustomParse("parseToMiniMessage")
    public String joinMessage = "<white>[<green>+</green>]</white> <gray>Welcome <color:#ab79ba><player></color> to the server!";

    public Component parseToMiniMessage(String message, MiniMessage mm, TagResolver... resolvers) {
        return mm.deserialize(message, resolvers);
    }
}
```

Now, whenever you want to access a field, it's as simple as passing in a MiniMessage instance and
optional placeholders:

```java
MessageConfig config = ...;
Player player = ...;

Component joinMessage = config.joinMessage(
    MiniMessage.miniMessage(),
    Placeholder.component("player", player.displayName())
);
```

## Custom types

Another very useful feature are reusable custom types. These are specially treated classes
which allow you to declare a method with `@ReturnType` in order to parse a serialized
object into a different type. This can be used for example when declaring items
in configs. The custom type can be declared as follows:

```java

@CustomType
public static class ItemDefinition {

    public String type;
    public String name;

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
```

A config can then declare a field of the same type:

```java
public ItemDefinition someItemDef = new ItemDefinition("diamond_sword", "<name>");
```

And it will use the declared `construct(TagResolver...)` method on the actual config interface:

```java
Config config = ...;
ItemStack is = config.someItemDef(
    Placeholder.unparsed("name", "A very cool name!");
);
```