package com.adanac.framework.log.dto;

import java.io.Serializable;
import java.util.HashMap;

import com.adanac.framework.log.util.names.AuditElement;
import com.adanac.framework.log.util.names.AuditType;

/**
 * 审计日志数据对象类
 * @author adanac
 * @version 1.0
 */
public class LogRecord4AuditCall implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 854855108682020163L;

	private static final String DEFAULT_AUDIT_TYPE_KEY = "AUDIT_TYPE_KEY";

	/* 审计日志所包含纬度 */
	private HashMap<String, String> auditElements = new HashMap<String, String>(4);
	/* 审计类型标识,用于表征被审计的业务类型 */
	private AuditType auditType;
	/* 审计日志主键标识,如订单ID */
	private AuditElement auditPrimaryKey;

	private LogRecord4AuditCall() {
	}

	/**
	 * 实例化审计日志对象,必须指定其审计业务类型,
	 * 审计日志主键及审计日志主键所对应的值
	 * @param _auditType
	 * @param _primaryKey
	 * @param _primaryValue
	 */
	public LogRecord4AuditCall(AuditType _auditType, AuditElement _primaryKey, String _primaryValue) {
		auditType = _auditType;
		auditPrimaryKey = _primaryKey;
		auditElements.put(DEFAULT_AUDIT_TYPE_KEY, _auditType.name().toUpperCase());
		auditElements.put(_primaryKey.name(), _primaryValue);
	}

	/**
	 * 功能描述：为审计日志添加元素,元素名称为Enum类型,元素值为String
	 * @param 参数说明 枚举类型_key 元素值_value
	 * 返回值:  类型 <说明> 
	 * @return 返回值
	 * @throw 异常描述
	 * @see com.suning.framework.log.util.names.AuditElement,com.suning.framework.log.util.names.AuditType
	 */
	public void addElement(AuditElement _key, String _value) {
		auditElements.put(_key.name(), _value);
	}

	/**
	 * 
	 * 功能描述：获取该审计日志的审计业务类型
	 * @param 参数说明
	 * 返回值:  类型 <说明> 
	 * @return 返回值
	 * @throw 异常描述
	 * @see com.bn.framework.log.util.names.AuditType
	 */
	public String queryAuditType() {
		return auditElements.get(DEFAULT_AUDIT_TYPE_KEY);
	}

	/**
	 * 
	 * 功能描述：获取该审计日志的审计日志主键值
	 * 输入参数：<按照参数定义顺序> 
	 * @param 参数说明
	 * 返回值:  类型 <说明> 
	 * @return 返回值
	 * @throw 异常描述
	 * @see 需要参见的其它内容
	 */
	public String queryAuditIndex() {
		return auditElements.get(auditPrimaryKey.name());
	}

	/**
	 * @return the auditElements
	 */
	public HashMap<String, String> getAuditElements() {
		return auditElements;
	}

	/**
	 * @param auditElements the auditElements to set
	 */
	public void setAuditElements(HashMap<String, String> auditElements) {
		this.auditElements = auditElements;
	}
}
