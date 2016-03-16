package com.adanac.framework.log.util.names;

/**
 * 审计日志业务类型,根据开发组需要可自行在此文件内进行添加
 * @author adanac
 * @version 1.0
 */
public enum AuditType {
	/* 促销中心审计业务类型标识 */
	PROMO_AUDIT,
	/* 订单中心审计业务类型标识 */
	ORDER_AUDIT,
	/* 库存中心审计业务类型标识 */
	STORE_AUDIT,
	/* 会员中心审计业务类型标识 */
	MEMBER_AUDIT
}
