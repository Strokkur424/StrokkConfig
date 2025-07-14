package net.strokkur.config.testplugin.config.simple;

import net.strokkur.config.Format;
import net.strokkur.config.annotations.ConfigFormat;
import net.strokkur.config.annotations.GenerateConfig;

@GenerateConfig("SimpleConfig")
@ConfigFormat(Format.YAML_SNAKEYAML)
public class SimpleConfigModel {
    String helloWorld = "Hello, Config!";
}
