package com.adanac.framework.log.dto;

import java.sql.Timestamp;

import com.adanac.framework.contexts.RequestContext;

import ch.qos.logback.classic.spi.LoggingEvent;

/**
 * 日志入库数据结构体
 * @author adanac
 * @version 1.0
 */
public class DbWritableRecord implements LogRecord {
	/* 日志标识 */
	private String requestId = null;

	/* 日志记录 */
	private LogRecord4ServiceCall callPerormance = null;

	/* 审计日志 */
	private LogRecord4AuditCall auditElements = null;

	/* 日志类型 */
	private String category = "";

	/* 访问信息 */
	private String callerStack = "";

	/* 日志等级 */
	private String level = "";

	/* 详细的日志信息 */
	private String detailMessage = "";

	/* 规范化的日志信息 */
	private String formattedMessage = "";

	/* 记录名称 */
	private String loggerName = "";

	/* 记录时间 */
	private Timestamp loggerTime;

	public DbWritableRecord() {
	}

	public DbWritableRecord(LoggingEvent _loggingEvent) {
		init(_loggingEvent);
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
		loggerName = _loggingEvent.getLoggerName();
		loggerTime = new Timestamp(_loggingEvent.getTimeStamp());

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
				if (arguments[i] instanceof LogRecord4AuditCall)
					auditElements = (LogRecord4AuditCall) arguments[i];
			}
		}
	}

	/**
	 * @return the loggerTime
	 */
	public Timestamp getLoggerTime() {
		return loggerTime;
	}

	/**
	 * @param loggerTime the loggerTime to set
	 */
	public void setLoggerTime(Timestamp loggerTime) {
		this.loggerTime = loggerTime;
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
	 * @return the formattedMessage
	 */
	public String getFormattedMessage() {
		return formattedMessage;
	}

	/**
	 * @param formattedMessage the formattedMessage to set
	 */
	public void setFormattedMessage(String formattedMessage) {
		this.formattedMessage = formattedMessage;
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
	 * @return the auditElements
	 */
	public LogRecord4AuditCall getAuditElements() {
		return auditElements;
	}

	/**
	 * @param auditElements the auditElements to set
	 */
	public void setAuditElements(LogRecord4AuditCall auditElements) {
		this.auditElements = auditElements;
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

	/**
	 * @return the loggerName
	 */
	public String getLoggerName() {
		return loggerName;
	}

	/**
	 * @param loggerName the loggerName to set
	 */
	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}
}
