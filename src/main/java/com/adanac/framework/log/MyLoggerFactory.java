package com.adanac.framework.log;

import com.adanac.framework.statistics.VersionStatistics;

/**
 * MyLogger构造工厂
 * @author adanac
 * @version 1.0
 */
public class MyLoggerFactory {
	static {
		VersionStatistics.reportVersion(MyLoggerFactory.class);
	}

	/**
	 * 根据class获取logger
	 */
	public static MyLogger getLogger(Class<?> clazz) {
		return new MyLogger(clazz);
	}

	/**
	 * 根据loggerName获取logger
	 */
	public static MyLogger getLogger(String loggerName) {
		return new MyLogger(loggerName);
	}
}
