package com.adanac.framework.log;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adanac.framework.lang.dto.ExceptionEventMessage;
import com.adanac.framework.lang.dto.TraceEventMessage;
import com.adanac.framework.lang.dto.TraceEventMessage.LOG_LEVEL;
import com.adanac.framework.lang.utils.EventUtils;

public class MyLogger {
	private Logger logger = null;

	private static boolean isTrace = false;

	static {
		String isTraceString = System.getProperty("isTrace");
		if (isTraceString != null) {
			isTrace = Boolean.valueOf(isTraceString);
		}
	}

	/**
	 * 构造方法
	 * 
	 * @param clazz
	 */
	public MyLogger(Class<?> clazz) {
		logger = LoggerFactory.getLogger(clazz);
	}

	/**
	 * 构造方法
	 * 
	 * @param loggerName
	 */
	public MyLogger(String loggerName) {
		logger = LoggerFactory.getLogger(loggerName);
	}

	public String getLoggerName() {
		return logger.getName();
	}

	/**
	 * Is the logger instance enabled for the INFO level?
	 * 
	 * @return True if this Logger is enabled for the INFO level, false otherwise.
	 */
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	/**
	 * Is the logger instance enabled for the DEBUG level?
	 * 
	 * @return True if this Logger is enabled for the DEBUG level, false otherwise.
	 * 
	 */
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	/**
	 * Is the logger instance enabled for the WARN level?
	 * 
	 * @return True if this Logger is enabled for the WARN level, false otherwise.
	 */
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	/**
	 * Is the logger instance enabled for the TRACE level?
	 * 
	 * @return True if this Logger is enabled for the TRACE level, false otherwise.
	 * 
	 */
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	/**
	 * 功能描述: <br>
	 * 异常日志记录
	 * 
	 * @param message
	 * @param ex
	 */
	public void logException(Exception ex) {
		ExceptionEventMessage exEventMsg = new ExceptionEventMessage(EventUtils.getCaller());
		if (null != ex) {
			exEventMsg.generateMsgFromException(ex);
		}
		logger.error(EventUtils.EVENT_GSON.toJson(exEventMsg));
	}

	/**
	 * 
	 * 记录异常日志，该方法用于兼容旧版本，将来可能废弃
	 * 
	 * @param ex 异常
	 * @param bizzContext 由业务系统定义及传入的与异常相关的业务上下文
	 */
	@Deprecated
	public void logException(Exception ex, Map<String, Object> bizzContext) {
		ExceptionEventMessage exEventMsg = new ExceptionEventMessage(EventUtils.getCaller());
		if (null != ex) {
			exEventMsg.generateMsgFromException(ex);
		}
		exEventMsg.setBizzContext(bizzContext);
		logger.error(EventUtils.EVENT_GSON.toJson(exEventMsg));
	}

	/**
	 * Log a message at the DEBUG level.
	 * 
	 * @param msg the message string to be logged
	 */
	public void debug(String msg) {
		if (isTrace) {
			TraceEventMessage traceEventMessage = new TraceEventMessage(EventUtils.getCaller(), msg, LOG_LEVEL.DEBUG);
			logger.debug(EventUtils.EVENT_GSON.toJson(traceEventMessage));
		} else {
			logger.debug(msg);
		}
	}

	public void debug(String format, Object arg) {
		logger.debug(format, arg);
	}

	public void debug(String format, Object[] argArray) {
		logger.debug(format, argArray);
	}

	/**
	 * Log an exception (throwable) at the DEBUG level with an accompanying message.
	 * 
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	public void debug(String msg, Throwable t) {
		logger.debug(msg, t);
	}

	/**
	 * Log a message at the DEBUG level according to the specified format and arguments.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled for the DEBUG level.
	 * </p>
	 * 
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 */
	public void debug(String format, Object arg1, Object arg2) {
		logger.debug(format, arg1, arg2);
	}

	/**
	 * Log a message at the INFO level.
	 * 
	 * @param msg the message string to be logged
	 */
	public void info(String msg) {
		if (isTrace) {
			TraceEventMessage traceEventMessage = new TraceEventMessage(EventUtils.getCaller(), msg, LOG_LEVEL.INFO);
			logger.info(EventUtils.EVENT_GSON.toJson(traceEventMessage));
		} else {
			logger.info(msg);
		}
	}

	public void info(String format, Object arg) {
		logger.info(format, arg);
	}

	/**
	 * Log a message at the INFO level according to the specified format and arguments.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled for the INFO level.
	 * </p>
	 * 
	 * @param format the format string
	 * @param argArray an array of arguments
	 */
	public void info(String format, Object[] argArray) {
		logger.info(format, argArray);
	}

	/**
	 * Log a message at the INFO level according to the specified format and arguments.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled for the INFO level.
	 * </p>
	 * 
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 */
	public void info(String format, Object arg1, Object arg2) {
		logger.info(format, arg1, arg2);
	}

	/**
	 * Log an exception (throwable) at the INFO level with an accompanying message.
	 * 
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	public void info(String msg, Throwable t) {
		logger.info(msg, t);
	}

	/**
	 * Log a message at the TRACE level.
	 * 
	 * @param msg the message string to be logged
	 * @since 1.4
	 */
	public void trace(String msg) {
		if (isTrace) {
			TraceEventMessage traceEventMessage = new TraceEventMessage(EventUtils.getCaller(), msg, LOG_LEVEL.TRACE);
			logger.trace(EventUtils.EVENT_GSON.toJson(traceEventMessage));
		} else {
			logger.trace(msg);
		}
	}

	public void trace(String format, Object arg) {
		logger.trace(format, arg);
	}

	/**
	 * Log a message at the TRACE level according to the specified format and arguments.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled for the TRACE level.
	 * </p>
	 * 
	 * @param format the format string
	 * @param argArray an array of arguments
	 * 
	 */
	public void trace(String format, Object[] argArray) {
		logger.trace(format, argArray);
	}

	/**
	 * Log an exception (throwable) at the TRACE level with an accompanying message.
	 * 
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 * 
	 */
	public void trace(String msg, Throwable t) {
		logger.trace(msg, t);
	}

	/**
	 * Log a message at the TRACE level according to the specified format and arguments.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled for the TRACE level.
	 * </p>
	 * 
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 * 
	 */
	public void trace(String format, Object arg1, Object arg2) {
		logger.trace(format, arg1, arg2);
	}

	/**
	 * Log a message at the WARN level.
	 * 
	 * @param msg the message string to be logged
	 */
	public void warn(String msg) {
		if (isTrace) {
			TraceEventMessage traceEventMessage = new TraceEventMessage(EventUtils.getCaller(), msg, LOG_LEVEL.WARN);
			logger.warn(EventUtils.EVENT_GSON.toJson(traceEventMessage));
		} else {
			logger.warn(msg);
		}
	}

	public void warn(String format, Object arg) {
		logger.warn(format, arg);
	}

	/**
	 * Log a message at the WARN level according to the specified format and arguments.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled for the WARN level.
	 * </p>
	 * 
	 * @param format the format string
	 * @param argArray an array of arguments
	 */
	public void warn(String format, Object[] argArray) {
		logger.warn(format, argArray);
	}

	/**
	 * Log a message at the WARN level according to the specified format and arguments.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled for the WARN level.
	 * </p>
	 * 
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 */
	public void warn(String format, Object arg1, Object arg2) {
		logger.warn(format, arg1, arg2);
	}

	/**
	 * Log an exception (throwable) at the WARN level with an accompanying message.
	 * 
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	public void warn(String msg, Throwable t) {
		logger.warn(msg, t);
	}

	/**
	 * Log a message at the ERROR level.
	 *
	 * @param msg the message string to be logged
	 */
	public void error(String msg) {
		if (isTrace) {
			TraceEventMessage traceEventMessage = new TraceEventMessage(EventUtils.getCaller(), msg, LOG_LEVEL.WARN);
			logger.error(EventUtils.EVENT_GSON.toJson(traceEventMessage));
		} else {
			logger.error(msg);
		}
	}

	/**
	 * Log a message at the ERROR level according to the specified format
	 * and arguments.
	 * 
	 * <p>This form avoids superfluous object creation when the logger
	 * is disabled for the ERROR level. </p>
	 *
	 * @param format the format string
	 * @param argArray an array of arguments
	 */
	public void error(String format, Object[] argArray) {
		logger.error(format, argArray);
	}

	/**
	 * Log a message at the ERROR level according to the specified format
	 * and argument.
	 * 
	 * <p>This form avoids superfluous object creation when the logger
	 * is disabled for the ERROR level. </p>
	 *
	 * @param format the format string 
	 * @param arg  the argument
	 */
	public void error(String format, Object arg) {
		logger.error(format, arg);
	}

	/**
	 * Log a message at the ERROR level according to the specified format
	 * and arguments.
	 * 
	 * <p>This form avoids superfluous object creation when the logger
	 * is disabled for the ERROR level. </p>
	 *
	 * @param format the format string
	 * @param arg1  the first argument
	 * @param arg2  the second argument
	 */
	public void error(String format, Object arg1, Object arg2) {
		logger.error(format, arg1, arg2);
	}
}
