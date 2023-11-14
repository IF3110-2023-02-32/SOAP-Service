package utils;

public class ConfigHandler {
    private static ConfigHandler instance = null;
    private final PropertiesHandler prophandler;
    private static final String USE_DOCKER_CONFIG_KEY = "USE_DOCKER_CONFIG";
    private static final String CONFIG_FILE = "config.properties";
    private static final String CONFIG_FILE_DOCKER = "config.docker.properties";

    private ConfigHandler() {
        String useDockerConfig = System.getenv(USE_DOCKER_CONFIG_KEY);
        if (useDockerConfig == null || useDockerConfig.equals("false")) {
            this.prophandler = new PropertiesHandler(CONFIG_FILE);
        } else {
            this.prophandler = new PropertiesHandler(CONFIG_FILE_DOCKER);
        }
    }

    public static ConfigHandler getInstance() {
        if (instance == null) {
            instance = new ConfigHandler();
        }
        return instance;
    }

    public String get(String key) {
        return this.prophandler.get(key);
    }

    public PropertiesHandler getPropertyHandler() {
        return this.prophandler;
    }
}
