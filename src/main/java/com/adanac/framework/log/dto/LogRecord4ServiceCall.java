package com.adanac.framework.log.dto;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 日志数据对象类
 * @author adanac
 * @version 1.0
 */
public class LogRecord4ServiceCall implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -5784670167770825327L;

	/* 操作类的类名 */
	private String clazzName;

	/* 执行的方法名 */
	private String methodName;
	/* 方法的参数 */
	private Object[] arguments;
	/* 方法的执行结果 */
	private Object result;
	/* 执行方法持续的时间 */
	private long duration;
	/* 执行开始的时间 */
	private Timestamp startTime;
	/* 执行结束的时间 */
	private Timestamp endTime;

	public String getClazzName() {
		return clazzName;
	}

	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object[] getArguments() {
		return arguments;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
}
