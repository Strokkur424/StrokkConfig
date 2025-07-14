package net.strokkur.config.internal.impl.fields;

import net.strokkur.config.internal.intermediate.ConfigField;
import net.strokkur.config.internal.intermediate.ConfigSection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConfigSectionImpl implements ConfigSection {

    private final String sectionName;
    private final List<ConfigField> fields = new ArrayList<>();
    private boolean nonNull = true;

    public ConfigSectionImpl(String sectionName) {
        this.sectionName = sectionName;
    }

    @Override
    public void addField(ConfigField field) {
        fields.add(field);
    }

    @Override
    public String getSectionName() {
        return sectionName;
    }

    @Override
    public boolean isDefaultNonNull() {
        return nonNull;
    }

    public void setDefaultNonNull(boolean value) {
        this.nonNull = value;
    }

    @Override
    public List<ConfigField> getFields() {
        return Collections.unmodifiableList(fields);
    }
}
