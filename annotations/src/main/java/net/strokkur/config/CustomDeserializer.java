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
package net.strokkur.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation given to a deserialization method for a {@link ConfigFormat.Format#CUSTOM} format.
 * <p>
 * The annotated method must be at least package-private and the first parameter
 * should be a {@link String} for accepting the serialized content. The method
 * should return the type of the config model class.
 * <p>
 * Example method:
 * <pre>{@code
 * @CustomDeserializer
 * static ConfigModel deserialize(String serialized) throws IOException {
 *     ConfigModel model = new ConfigModel();
 *     model.someValue = serialized;
 *     return model;
 * }
 * }</pre>
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface CustomDeserializer {}
