
package com.adanac.framework.log.appender.rolling;

import com.adanac.framework.log.dto.LogRecord;
import com.adanac.framework.log.policy.ContentFormattingPolicy;
import com.adanac.framework.log.util.LogRecordFactory;
import com.adanac.framework.log.util.RecordType;

/**
 * 
 * 功能描述： 日志appender,继承于RollingFileAppender<E>,用于实现对
 * 要求格式日志的写文件操作,与JsonFormattingPolicy联合使用
 * @author
 * @created
 * @version 1.0.0
 * @date 
 */
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;

public class BnfRollingFileAppender<E> extends RollingFileAppender<E> {
	private ContentFormattingPolicy formattingPolicy;

	/**
	 * Appender初始化函数start(),判断formattingPolicy是否为空 调用父类start()函数 {@inheritDoc}
	 */
	public void start() {
		if (formattingPolicy == null) {
			addWarn("No FormattingPolicy was set for the SnRollingFileAppender named " + getName());
			addWarn("Every log will be logged like the logback default logger does");
		}
		super.start();
	}

	/**
	 * 日志事件记录函数,判断formattingPolicy是否为空,若非空,则 执行formattingPolicy.format()方法
	 * 若为空,则直接调用父类方法 {@inheritDoc}
	 */
	@Override
	protected void subAppend(E event) {
		LoggingEvent finalEvent = null;

		if (event instanceof LoggingEvent) {

			if (formattingPolicy != null) {
				LoggingEvent origEvent = (LoggingEvent) event;

				LogRecord record = LogRecordFactory.createLogRecord(origEvent, RecordType.PRINTABLE_RECORD);

				String formattedMsg = formattingPolicy.format(record);

				finalEvent = new LoggingEvent();
				finalEvent.setMessage(formattedMsg);
				finalEvent.setArgumentArray(origEvent.getArgumentArray());

				finalEvent.setCallerData(origEvent.getCallerData());
				finalEvent.setLevel(origEvent.getLevel());
				finalEvent.setMarker(origEvent.getMarker());

				finalEvent.setTimeStamp(origEvent.getTimeStamp());

				// finalEvent.setThrowableProxy((ThrowableProxy)origEvent.getThrowableProxy());
				finalEvent.setLoggerContextRemoteView(origEvent.getLoggerContextVO());
				finalEvent.setLoggerName(origEvent.getLoggerName());

			}

		}
		if (finalEvent != null)
			event = (E) finalEvent;
		super.subAppend(event);
	}

	@Override
	/**
	 * 功能描述：关闭formattingPolicy
	 * @param
	 * @return void
	 * @throw
	 * @see
	 */
	public void stop() {
		if (formattingPolicy != null)
			formattingPolicy.stop();
		super.stop();
	}

	/**
	 * 功能描述：设置ContentFormattingPolicy
	 * @param ContentFormattingPolicy formattingPolicy
	 * @return void
	 * @throw
	 * @see
	 */
	public void setFormattingPolicy(ContentFormattingPolicy formattingPolicy) {
		this.formattingPolicy = formattingPolicy;
	}

	/**
	 * 功能描述：获取ContentFormattingPolicy
	 * @param
	 * @return void
	 * @throw
	 * @see
	 */
	public ContentFormattingPolicy getFormattingPolicy() {
		return formattingPolicy;
	}
}
