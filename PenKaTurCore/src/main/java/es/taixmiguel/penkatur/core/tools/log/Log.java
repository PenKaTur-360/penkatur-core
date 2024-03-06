package es.taixmiguel.penkatur.core.tools.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author TaixMiguel
 */
public class Log {

	private final Logger logger;

	private Log(Class<?> clase) {
		this.logger = LoggerFactory.getLogger(clase);
	}

	public static void trace(Class<?> clase, String mensaje, Object... argumentos) {
		new Log(clase).logger.trace(mensaje, argumentos);
	}

	public static void trace(Class<?> clase, String mensaje, Throwable excepcion) {
		new Log(clase).logger.trace(mensaje, excepcion);
	}

	public static void debug(Class<?> clase, String mensaje, Object... argumentos) {
		new Log(clase).logger.debug(mensaje, argumentos);
	}

	public static void debug(Class<?> clase, String mensaje, Throwable excepcion) {
		new Log(clase).logger.debug(mensaje, excepcion);
	}

	public static void info(Class<?> clase, String mensaje, Object... argumentos) {
		new Log(clase).logger.info(mensaje, argumentos);
	}

	public static void info(Class<?> clase, String mensaje, Throwable excepcion) {
		new Log(clase).logger.info(mensaje, excepcion);
	}

	public static void warn(Class<?> clase, String mensaje, Object... argumentos) {
		new Log(clase).logger.warn(mensaje, argumentos);
	}

	public static void warn(Class<?> clase, String mensaje, Throwable excepcion) {
		new Log(clase).logger.warn(mensaje, excepcion);
	}

	public static void error(Class<?> clase, String mensaje, Object... argumentos) {
		new Log(clase).logger.error(mensaje, argumentos);
	}

	public static void error(Class<?> clase, String mensaje, Throwable excepcion) {
		new Log(clase).logger.error(mensaje, excepcion);
	}
}
