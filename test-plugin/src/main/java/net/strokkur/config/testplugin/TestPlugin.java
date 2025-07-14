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
