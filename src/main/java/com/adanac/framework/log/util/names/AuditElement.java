package com.adanac.framework.log.util.names;

/**
 * 审计日志元素定义,各开发组根据需要,可自行定义
 * @author adanac
 * @version 1.0
 */
public enum AuditElement {
	/* 促销中心相关审计元素 */
	PROMO_VOUCHER_ID, PROMO_COUPON_ID, PROMO_MBGRP_ID, PROMO_VOUCHER_CREATED_DATE,

	/* 订单中心相关审计元素 */
	ORDER_ORDER_ID, ORDER_PRD_ID, ORDER_SUBMIT_DATE, ORDER_CANCLE_DATE,

	/* 会员中心相关审计元素 */
	MEMBER_MBR_ID, MEMBER_REGISTER_DATE
}
