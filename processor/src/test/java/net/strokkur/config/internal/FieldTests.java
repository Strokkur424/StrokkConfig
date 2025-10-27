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
package net.strokkur.config.internal;

import net.strokkur.config.internal.util.FieldNameContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FieldTests {

  @Test
  public void nameParseSimple() {
    test("name", "name");
  }

  @Test
  public void nameParse() {
    test("complexName", "complex-name");
  }

  @Test
  public void nameParseFirstLetterUppercase() {
    test("FirstUppercase", "first-uppercase");
  }

  @Test
  public void nameParseChained() {
    test("manyChainedTogetherNameParts", "many-chained-together-name-parts");
  }

  @Test
  public void nameParseChainedFirstUppercase() {
    test("ThereAreEvenMoreWhereThisCameFrom", "there-are-even-more-where-this-came-from");
  }

  @Test
  public void nameParseUnderscores() {
    test("this_contains_underscores", "this-contains-underscores");
  }

  @Test
  public void nameParseDashes() {
    test("this-already-contains-dashes", "this-already-contains-dashes");
  }

  @Test
  public void nameParseNumbers() {
    test("2363isVeryCool", "2363is-very-cool");
    test("2363IsVeryCool", "2363-is-very-cool");
  }

  private void test(String input, String expected) {
    FieldNameContainer field = new FieldNameContainerTestImpl(input);

    String dashed = field.getFieldNameDashed();
    Assertions.assertEquals(expected, dashed);
  }

  public record FieldNameContainerTestImpl(String getFieldName) implements FieldNameContainer {}
}
