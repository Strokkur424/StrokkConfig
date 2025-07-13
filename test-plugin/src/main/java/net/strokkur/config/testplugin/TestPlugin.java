package net.strokkur.config.testplugin;

import net.strokkur.config.testplugin.config.Reference$MyCoolConfig;
import net.strokkur.config.testplugin.config.Reference$MyCoolConfigImpl;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;

@NullMarked
public class TestPlugin extends JavaPlugin {

    public void reload() throws IOException {
        Reference$MyCoolConfig config = new Reference$MyCoolConfigImpl();
        config.reload(this);
        
        ItemStack stack = config.itemDefinition();
    }
}
