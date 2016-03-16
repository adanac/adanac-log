package com.adanac.framework.log.policy;

import ch.qos.logback.core.spi.LifeCycle;

/**
 * 日志内容规整策略接口
 * @author adanac
 * @version 1.0
 */
public interface ContentFormattingPolicy extends LifeCycle {
	public String format(Object _object);
}
