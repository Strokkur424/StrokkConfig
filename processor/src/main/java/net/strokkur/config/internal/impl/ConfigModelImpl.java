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
package net.strokkur.config.internal.impl;

import net.strokkur.config.internal.intermediate.ConfigField;
import net.strokkur.config.internal.intermediate.ConfigMetadata;
import net.strokkur.config.internal.intermediate.ConfigModel;
import net.strokkur.config.internal.intermediate.ConfigSection;
import net.strokkur.config.internal.intermediate.CustomType;

import java.util.ArrayList;
import java.util.List;

public class ConfigModelImpl implements ConfigModel {

  private final ConfigMetadata metadata;
  private final List<ConfigField> fields;
  private final List<CustomType> customTypes;
  private final List<ConfigSection> sections;

  private ConfigModelImpl(ConfigMetadata metadata, List<ConfigField> fields, List<CustomType> customTypes, List<ConfigSection> sections) {
    this.metadata = metadata;
    this.fields = fields;
    this.customTypes = customTypes;
    this.sections = sections;
  }

  public static ConfigModel.Builder builder(ConfigMetadata metadata) {
    return new ConfigModelBuilderImpl(metadata);
  }

  @Override
  public ConfigMetadata getMetadata() {
    return metadata;
  }

  @Override
  public List<ConfigField> getFields() {
    return fields;
  }

  @Override
  public List<CustomType> getCustomTypes() {
    return customTypes;
  }

  @Override
  public List<ConfigSection> getSections() {
    return sections;
  }

  private static class ConfigModelBuilderImpl implements ConfigModel.Builder {

    private final List<ConfigField> fields = new ArrayList<>();
    private final List<CustomType> types = new ArrayList<>();
    private final List<ConfigSection> sections = new ArrayList<>();
    private ConfigMetadata metadata;

    public ConfigModelBuilderImpl(ConfigMetadata metadata) {
      this.metadata = metadata;
    }

    @Override
    public Builder setMetadata(ConfigMetadata metadata) {
      this.metadata = metadata;
      return this;
    }

    @Override
    public Builder addField(ConfigField field) {
      fields.add(field);
      return this;
    }

    @Override
    public Builder addCustomType(CustomType type) {
      types.add(type);
      return this;
    }

    @Override
    public Builder addSection(ConfigSection section) {
      sections.add(section);
      return this;
    }

    @Override
    public ConfigModel build() {
      return new ConfigModelImpl(metadata, fields, types, sections);
    }
  }
}
