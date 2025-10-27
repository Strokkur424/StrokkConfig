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
package net.strokkur.config.internal.exceptions;

import net.strokkur.config.internal.util.MessagerWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

import javax.lang.model.element.Element;

@ApiStatus.Internal
public class ProcessorException extends Exception {

  private final @Nullable Element element;

  public ProcessorException(String message, @Nullable Element element) {
    super(message);
    this.element = element;
  }

  public ProcessorException(String message) {
    this(message, null);
  }

  public @Nullable Element getElement() {
    return element;
  }

  public void warn(MessagerWrapper messager) {
    if (element != null) {
      messager.warnElement(this.getMessage(), this.getElement());
    } else {
      messager.warn(this.getMessage());
    }
  }

  public void error(MessagerWrapper messager) {
    if (element != null) {
      messager.errorElement(this.getMessage(), this.getElement());
    } else {
      messager.error(this.getMessage());
    }
  }
}
