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

import net.strokkur.config.internal.intermediate.ConfigFormat;
import net.strokkur.config.internal.intermediate.ConfigMetadata;
import net.strokkur.config.internal.intermediate.CustomSerializers;
import org.jspecify.annotations.Nullable;

public class ConfigMetadataImpl implements ConfigMetadata {

  private final String originalClass;
  private final String packageName;
  private final String interfaceClassName;
  private final ConfigFormat configFormat;
  private final @Nullable String filePath;
  private final boolean defaultNonNull;
  private final CustomSerializers customSerializers;

  public ConfigMetadataImpl(String originalClass, String packageName, String interfaceClassName, ConfigFormat configFormat,
                            @Nullable String filePath, boolean defaultNonNull, @Nullable CustomSerializers customSerializers) {
    this.originalClass = originalClass;
    this.packageName = packageName;
    this.interfaceClassName = interfaceClassName;
    this.configFormat = configFormat;
    this.filePath = filePath;
    this.defaultNonNull = defaultNonNull;
    this.customSerializers = customSerializers;
  }

  @Override
  public String getFilePath() {
    return filePath == null ? "config" + configFormat.defaultExtension() : filePath;
  }

  @Override
  public String getPackage() {
    return packageName;
  }

  @Override
  public String getOriginalClass() {
    return originalClass;
  }

  @Override
  public String getInterfaceClass() {
    return interfaceClassName;
  }

  @Override
  public String getImplementationClass() {
    return interfaceClassName + "Impl";
  }

  @Override
  public ConfigFormat getFormat() {
    return configFormat;
  }

  @Override
  public boolean isDefaultNonNull() {
    return defaultNonNull;
  }

  @Override
  public @Nullable CustomSerializers getCustomSerializers() {
    return customSerializers;
  }
}
