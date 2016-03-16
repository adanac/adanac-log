package com.adanac.framework.log.util;

import com.adanac.framework.log.appender.db.names.ColumnName;
import com.adanac.framework.log.appender.db.names.TableName;

import ch.qos.logback.classic.db.names.DBNameResolver;

public class SQLBuilder {
	public static String buildInsertPerformanceSQL(DBNameResolver dbNameResolver) {
		StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ");
		sqlBuilder.append(dbNameResolver.getTableName(TableName.SNF_LOGGING_PERF)).append(" (");
		sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.REQUEST_ID)).append(", ");
		sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.CLAZZ_NAME)).append(", ");
		sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.METHOD_NAME)).append(", ");
		sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.ARGUMENTS)).append(", ");
		sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.RESULT)).append(", ");
		sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.DURATION)).append(", ");
		sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.START_TIME)).append(", ");
		sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.END_TIME)).append(") ");
		sqlBuilder.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
		return sqlBuilder.toString();
	}

	public static String buildInsertSQL(DBNameResolver dbNameResolver) {
		StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ");
		sqlBuilder.append(dbNameResolver.getTableName(TableName.SNF_LOGGING_EVENT)).append(" (");
		sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.REQUEST_ID)).append(", ");
		sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.CATEGORY)).append(", ");
		sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.LEVEL_STRING)).append(", ");
		sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.FORMATTED_MESSAGE)).append(", ");
		sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.LOGGER_NAME)).append(", ");
		sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.CALLER_STACK)).append(", ");
		sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.DETAIL_MESSAGE)).append(", ");
		sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.AUDIT_ELE_IDX1)).append(", ");
		sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.AUDIT_ELE_IDX2)).append(", ");
		sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.AUDIT_ELE_IDX3)).append(", ");
		sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.LOGGER_TIME)).append(") ");
		sqlBuilder.append("VALUES (?, ?, ? ,?, ?, ?, ?, ?, ?, ?, ?)");
		return sqlBuilder.toString();
	}
}
