import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PagerTest {
    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger("org.slf4j.Logger.ROOT_LOGGER_NAME");
        logger.trace("Hello world.");
        String s = "Petro";
        logger.debug("Your name is {}", s);
    }
}
