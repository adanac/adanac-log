package com.adanac.framework.log.appender.db.names;

/**
 * 列索引
 * @author adanac
 * @version 1.0
 */
public interface ColumnIndex {
	static final int REQUEST_ID_INDEX = 1;

	static final int CATEGORY_INDEX = 2;

	static final int LEVEL_STRING_INDEX = 3;

	static final int FORMATTED_MESSAGE_INDEX = 4;

	static final int LOGGER_NAME_INDEX = 5;

	static final int CALLER_STACK_INDEX = 6;

	static final int DETAIL_MSG_INDEX = 7;

	static final int AUDIT_ELE_IDX1 = 8;

	static final int AUDIT_ELE_IDX2 = 9;

	static final int AUDIT_ELE_IDX3 = 10;

	static final int AUDIT_ELE_IDX4 = 11;

	static final int AUDIT_ELE_IDX5 = 12;

	static final int LOGGER_TIME_INDEX = 11;/* 当前版本11,12两列无填充值 */

	static final int CALLER_FILENAME_INDEX = 11;

	static final int CALLER_CLASS_INDEX = 12;

	static final int CALLER_METHOD_INDEX = 13;

	static final int CALLER_LINE_INDEX = 14;

	static final int EVENT_ID_INDEX = 15;

	static final int CLAZZ_NAME_INDEX = 2;

	static final int METHOD_NAME_INDEX = 3;

	static final int ARGUMENTS_INDEX = 4;

	static final int RESULT_INDEX = 5;

	static final int DURATION_INDEX = 6;

	static final int START_TIME_INDEX = 7;

	static final int END_TIME_INDEX = 8;
}
