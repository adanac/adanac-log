package com.adanac.framework.log.dto;

import com.adanac.framework.context.RequestContext;

import ch.qos.logback.classic.spi.LoggingEvent;

/**
 * 控制台输出日志结构体
 * @author adanac
 * @version 1.0
 */
public class PrintableRecord implements LogRecord {
	/* 日志标识 */
	private String requestId = null;

	/* 日志记录 */
	private LogRecord4ServiceCall callPerormance = null;

	/* 日志类型 */
	private String category = "";

	/* 访问信息 */
	private String callerStack = "";

	/* 日志等级 */
	private String level = "";

	/* 详细的日志信息 */
	private String detailMessage = "";

	public PrintableRecord(LoggingEvent _loggingEvent) {
		init(_loggingEvent);
	}

	public PrintableRecord() {

	}

	/**
	 * 
	 * 功能描述：日志数据初始化
	 * @param 参数说明 LoggingEvent
	 */
	private void init(LoggingEvent _loggingEvent) {
		detailMessage = _loggingEvent.getFormattedMessage();
		category = _loggingEvent.getLoggerName();
		level = _loggingEvent.getLevel().levelStr;
		requestId = RequestContext.getRequestId();
		StackTraceElement[] callerElements = _loggingEvent.getCallerData();

		if (callerElements != null && callerElements.length != 0) {
			for (int k = 0; k < callerElements.length; k++) {
				callerStack += callerElements[k].toString();
				if (k < callerElements.length - 1)
					callerStack += "-->";
			}
		}

		Object[] arguments = _loggingEvent.getArgumentArray();
		if (arguments != null && arguments.length != 0) {
			for (int i = 0; i < arguments.length; i++) {
				if (arguments[i] instanceof LogRecord4ServiceCall)
					callPerormance = (LogRecord4ServiceCall) arguments[i];
			}
		}
	}

	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
	}

	/**
	 * @return the requestId
	 */
	public String getRequestId() {
		return requestId;
	}

	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the callPerormance
	 */
	public LogRecord4ServiceCall getCallPerormance() {
		return callPerormance;
	}

	/**
	 * @param callPerormance the callPerormance to set
	 */
	public void setCallPerormance(LogRecord4ServiceCall callPerormance) {
		this.callPerormance = callPerormance;
	}

	/**
	 * @return the callerStack
	 */
	public String getCallerStack() {
		return callerStack;
	}

	/**
	 * @param callerStack the callerStack to set
	 */
	public void setCallerStack(String callerStack) {
		this.callerStack = callerStack;
	}

	/**
	 * @return the detailMessage
	 */
	public String getDetailMessage() {
		return detailMessage;
	}

	/**
	 * @param detailMessage the detailMessage to set
	 */
	public void setDetailMessage(String detailMessage) {
		this.detailMessage = detailMessage;
	}
}
