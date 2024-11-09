import java.util.logging.Level;
import java.util.logging.Logger;

public class EnvUtil {
    private static final Logger logger = Logger.getLogger(EnvUtil.class.getName());

    // Fetch the environment variable, if not set return the default value
    public static int getEnvVarOrDefault(String envVar, int defaultValue) {
        String value = System.getenv(envVar);
        if (value == null || value.isEmpty()) {
            logger.log(Level.WARNING, "Environment variable \"" + envVar + "\" not set, using default value: " + defaultValue);
            return defaultValue;
        }
        return Integer.parseInt(value);
    }
}
