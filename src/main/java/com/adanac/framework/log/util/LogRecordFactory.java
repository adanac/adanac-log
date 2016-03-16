package com.adanac.framework.log.util;

import com.adanac.framework.log.dto.DbWritableRecord;
import com.adanac.framework.log.dto.LogRecord;
import com.adanac.framework.log.dto.PrintableRecord;

import ch.qos.logback.classic.spi.LoggingEvent;

/**
 * 
 * @author adanac
 * @version 1.0
 */
public class LogRecordFactory {
	public static final <N extends Enum<?>> LogRecord createLogRecord(LoggingEvent _event, N type) {
		if (type.name().toUpperCase().equals(RecordType.PRINTABLE_RECORD.name()))
			return new PrintableRecord(_event);
		if (type.name().toUpperCase().equals(RecordType.DB_RECORD.name()))
			return new DbWritableRecord(_event);
		return null;
	}
}
