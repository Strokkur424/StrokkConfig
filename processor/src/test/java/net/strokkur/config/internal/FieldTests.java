package net.strokkur.config.internal;

import net.strokkur.config.internal.intermediate.ConfigField;
import net.strokkur.config.internal.intermediate.ConfigType;
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
        ConfigField field = new NoopConfigFieldImpl(input);

        String dashed = field.getFieldNameDashed();
        Assertions.assertEquals(expected, dashed);
    }


    public class NoopConfigFieldImpl implements ConfigField {
        private final String fieldName;

        public NoopConfigFieldImpl(String fieldName) {
            this.fieldName = fieldName;
        }

        @Override
        public ConfigType getFieldType() {
            return null;
        }

        @Override
        public String getFieldName() {
            return fieldName;
        }
    }
}
