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
package net.strokkur.config.internal.intermediate;

import java.util.List;

/**
 * Internal representation of the annotated config model class.
 */
public interface ConfigModel {

    ConfigMetadata getMetadata();

    List<ConfigField> getFields();

    List<CustomType> getCustomTypes();

    List<ConfigSection> getSections();

    interface Builder {
        Builder setMetadata(ConfigMetadata metadata);
        Builder addField(ConfigField field);
        Builder addCustomType(CustomType type);
        Builder addSection(ConfigSection section);

        ConfigModel build();
    }
}
