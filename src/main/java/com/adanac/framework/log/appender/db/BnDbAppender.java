package com.adanac.framework.log.appender.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.adanac.framework.log.appender.db.names.ColumnIndex;
import com.adanac.framework.log.dto.DbWritableRecord;
import com.adanac.framework.log.dto.LogRecord;
import com.adanac.framework.log.dto.LogRecord4AuditCall;
import com.adanac.framework.log.dto.LogRecord4ServiceCall;
import com.adanac.framework.log.util.LogRecordFactory;
import com.adanac.framework.log.util.RecordType;
import com.adanac.framework.log.util.SQLBuilder;
import com.google.gson.Gson;

import ch.qos.logback.classic.db.names.DBNameResolver;
import ch.qos.logback.classic.db.names.DefaultDBNameResolver;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;

/**
 * adanac-log 框架,日志入库appender,负责日志入库操作
 * @author adanac
 * @version 1.0
 */
public class BnDbAppender extends BnDbAppenderBase<ILoggingEvent> {
	protected String insertPerformanceSQL;

	protected String insertSQL;

	private boolean auditFlag = false;

	private DBNameResolver dbNameResolver;

	@Override
	public void start() {
		if (dbNameResolver == null)
			dbNameResolver = new DefaultDBNameResolver();
		insertPerformanceSQL = SQLBuilder.buildInsertPerformanceSQL(dbNameResolver);
		insertSQL = SQLBuilder.buildInsertSQL(dbNameResolver);
		super.start();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getInsertSQL() {
		return insertSQL;
	}

	/**
	 * @return the auditFlag
	 */
	public boolean isAuditFlag() {
		return auditFlag;
	}

	/**
	 * @param auditFlag the auditFlag to set
	 */
	public void setAuditFlag(boolean auditFlag) {
		this.auditFlag = auditFlag;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void subAppend(ILoggingEvent eventObject, Connection connection, PreparedStatement statement)
			throws Throwable {

		LogRecord record = LogRecordFactory.createLogRecord((LoggingEvent) eventObject, RecordType.DB_RECORD);

		if (record instanceof DbWritableRecord) {
			bindLoggingEventWithInsertStatement(statement, (DbWritableRecord) record);
			int updateCount = statement.executeUpdate();
			if (updateCount != 1) {
				addWarn("Failed to insert loggingEvent");
			}
		}

		if (isPerformanceFlag()) {
			secondarySubAppend((DbWritableRecord) record, connection);
		}
	}

	/**
	 * 
	 * 功能描述： 记录日志数据到数据库中
	 * @param 参数说明 入库日志数据DbWritableRecord ，数据库的连接Connection
	 * @return 返回值
	 * @throw 异常描述
	 * @see 需要参见的其它内容
	 */
	protected void secondarySubAppend(DbWritableRecord record, Connection connection) throws Throwable {

		PreparedStatement pstmt = null;
		LogRecord4ServiceCall serviceCall = record.getCallPerormance();
		if (serviceCall == null) {
			addWarn("Failed to insert snf-logging-perf cause:PerformanceCallRecord is null");
			return;
		}

		pstmt = connection.prepareStatement(insertPerformanceSQL);
		bindLoggingEventWithInsertPerfStatement(pstmt, serviceCall, record.getRequestId());
		int updateCount = pstmt.executeUpdate();
		if (updateCount != 1) {
			addWarn("Failed to insert loggingEvent");
		}
		pstmt.close();
		pstmt = null;
	}

	void bindLoggingEventWithInsertStatement(PreparedStatement stmt, DbWritableRecord record) throws SQLException {
		LogRecord4AuditCall auditCall = null;
		stmt.setString(ColumnIndex.REQUEST_ID_INDEX, record.getRequestId());
		stmt.setString(ColumnIndex.CATEGORY_INDEX, record.getCategory());
		stmt.setString(ColumnIndex.LEVEL_STRING_INDEX, record.getLevel());
		stmt.setString(ColumnIndex.FORMATTED_MESSAGE_INDEX, record.getFormattedMessage());
		stmt.setString(ColumnIndex.LOGGER_NAME_INDEX, record.getLoggerName());
		stmt.setString(ColumnIndex.CALLER_STACK_INDEX, record.getCallerStack());
		stmt.setString(ColumnIndex.DETAIL_MSG_INDEX, record.getDetailMessage());
		stmt.setTimestamp(ColumnIndex.LOGGER_TIME_INDEX, record.getLoggerTime());

		if (isAuditFlag()) {
			auditCall = record.getAuditElements();
		}
		if (auditCall == null) {
			stmt.setString(ColumnIndex.AUDIT_ELE_IDX1, null);
			stmt.setString(ColumnIndex.AUDIT_ELE_IDX2, null);
			stmt.setString(ColumnIndex.AUDIT_ELE_IDX3, null);
			return;
		}

		stmt.setString(ColumnIndex.AUDIT_ELE_IDX1, auditCall.queryAuditType());
		stmt.setString(ColumnIndex.AUDIT_ELE_IDX2, auditCall.queryAuditIndex());
		Gson gson = new Gson();
		stmt.setString(ColumnIndex.AUDIT_ELE_IDX3, gson.toJson(auditCall));
	}

	void bindLoggingEventWithInsertPerfStatement(PreparedStatement stmt, LogRecord4ServiceCall serviceCall,
			String requestId) throws SQLException {
		stmt.setString(ColumnIndex.REQUEST_ID_INDEX, requestId);
		stmt.setString(ColumnIndex.CLAZZ_NAME_INDEX, serviceCall.getClazzName());
		stmt.setString(ColumnIndex.METHOD_NAME_INDEX, serviceCall.getMethodName());
		Gson gson = new Gson();
		stmt.setString(ColumnIndex.ARGUMENTS_INDEX, gson.toJson(serviceCall.getArguments()));
		stmt.setString(ColumnIndex.RESULT_INDEX, gson.toJson(serviceCall.getResult()));
		stmt.setLong(ColumnIndex.DURATION_INDEX, serviceCall.getDuration());
		stmt.setTimestamp(ColumnIndex.START_TIME_INDEX, serviceCall.getStartTime());
		stmt.setTimestamp(ColumnIndex.END_TIME_INDEX, serviceCall.getEndTime());
	}
}
