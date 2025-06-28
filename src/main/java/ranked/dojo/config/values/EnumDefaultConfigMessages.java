package ranked.dojo.config.values;

import lombok.Getter;


@Getter
public enum EnumDefaultConfigMessages {
    TEST_MESSAGE("test.settings.config", "This is a test message to be saved in the messages config.")

    ;

    private final String path;
    private final String defaultValue;

    /**
     * Constructor for the MessagesConfigValues enum
     *
     * @param path the path to the value in the config
     * @param defaultValue the default value to set if the value is not found
     */
    EnumDefaultConfigMessages(String path, String defaultValue) {
        this.path = path;
        this.defaultValue = defaultValue;
    }
}