package bukkit.plugin;
import java.util.logging.Logger;

import common.core.ICLogger;

public class JavaUtilLogger extends ICLogger {
	private final Logger logger;

	public JavaUtilLogger(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void info(String message) {
		logger.info(message);
	}

	@Override
	public void warning(String message) {
		logger.warning(message);
	}

	@Override
	public void error(String message) {
		logger.severe(message);
	}
}
