package com.adanac.framework.log.appender.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.db.ConnectionSource;
import ch.qos.logback.core.db.DBHelper;

/**
 * adanac-log日志框架,日志入库基础类
 * @author adanac
 * @version 1.0
 */
public abstract class BnDbAppenderBase<E> extends UnsynchronizedAppenderBase<E> {
	protected ConnectionSource connectionSource;

	protected boolean performanceFlag = false;

	protected abstract String getInsertSQL();

	/**
	 * 
	 * 功能描述： 启动数据库的连接
	 * @param 参数说明 
	 * @return 返回值
	 * @throw 异常描述
	 */
	@Override
	public void start() {
		if (connectionSource == null) {
			throw new IllegalStateException("DBAppender cannot function without a connection source");
		}
		super.start();
	}

	protected abstract void subAppend(E eventObject, Connection connection, PreparedStatement statement)
			throws Throwable;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void append(E eventObject) {
		Connection connection = null;
		try {
			connection = connectionSource.getConnection();
			connection.setAutoCommit(false);
			PreparedStatement insertStatement;
			insertStatement = connection.prepareStatement(getInsertSQL());

			subAppend(eventObject, connection, insertStatement);

			close(insertStatement);
			insertStatement = null;
			connection.commit();
		} catch (Throwable sqle) {
			sqle.printStackTrace();
			addError("problem appending event", sqle);
		} finally {
			DBHelper.closeConnection(connection);
		}
	}

	/**
	 * @return the connectionSource
	 */
	public ConnectionSource getConnectionSource() {
		return connectionSource;
	}

	/**
	 * @param connectionSource the connectionSource to set
	 */
	public void setConnectionSource(ConnectionSource connectionSource) {
		this.connectionSource = connectionSource;
	}

	/**
	 * @return the performanceFlag
	 */
	public boolean isPerformanceFlag() {
		return performanceFlag;
	}

	/**
	 * @param performanceFlag the performanceFlag to set
	 */
	public void setPerformanceFlag(boolean performanceFlag) {
		this.performanceFlag = performanceFlag;
	}

	void close(Statement statement) throws SQLException {
		if (statement != null) {
			statement.close();
		}
	}
}
