package com.adanac.framework.lang.performance;

import java.util.Stack;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;

import com.adanac.framework.exception.BaseException;
import com.adanac.framework.lang.dto.PerformanceEventMessage;
import com.adanac.framework.lang.util.EventUtils;
import com.adanac.framework.log.MyLogger;
import com.adanac.framework.log.MyLoggerFactory;

/**
 * 
 * @author adanac
 * @version 1.0
 */
public class PerformanceInterceptor {
	protected MyLogger logger;

	protected String loggerName;

	protected ThreadLocal<Stack<PerformanceEventMessage>> threadMsgStack = new ThreadLocal<Stack<PerformanceEventMessage>>();

	public PerformanceInterceptor() {
		super();
	}

	/**
	 * 
	 * 功能描述: <br>
	 * spring aop环绕事件，用于计算方法执行时间以及相应的性能事件的属性（包括方法名，父方法名，执行状态，相对于调用链中的第一个方法的执行时间）
	 * 
	 * @param pjp 待执行的方法切点
	 * @return
	 * @throws Throwable
	 */
	public Object aroundProcess(ProceedingJoinPoint pjp) throws Throwable {
		afterPropertiesSet();

		// 生成新的性能事件以及填充相应信息
		PerformanceEventMessage me = new PerformanceEventMessage(this.generateFunctionName(pjp));
		// 父性能事件
		PerformanceEventMessage parent = this.getTopStackElement();
		// 此次请求的第一个性能事件
		PerformanceEventMessage first = this.getFirstStackElement();
		me.setSequence(this.getStackLength() + 1);
		if (parent != null) {
			me.setFunCallerName(parent.getFunName());
		} else {
			me.setFunCallerName(PerformanceEventMessage.NAN_CALLER);
		}
		if (first != null) {
			me.setRelativeTime(me.getEventTime().getTime() - first.getEventTime().getTime());
		} else {
			me.setRelativeTime(0);
		}

		// 于方法调用之前，将本事件入栈
		this.pushToStack(me);

		// 开始执行方法
		// 执行结果存放容器
		Object result = null;
		try {
			me.begin();
			result = pjp.proceed();
		} catch (BaseException baseEx) {
			me.setStatus(baseEx.getCode());
			throw baseEx;
		} catch (Throwable t) {
			me.setStatus(PerformanceEventMessage.NAN_ERROR_CODE);
		} finally {
			// 方法执行完毕，将本事件出栈
			this.popFromStack();
			me.end();
			if (this.logger.isWarnEnabled()) {
				logger.warn(EventUtils.EVENT_GSON.toJson(me), new Object[] {});
			}
		}
		return result;
	}

	public void afterPropertiesSet() throws Exception {
		if (this.loggerName == null) {
			this.logger = MyLoggerFactory.getLogger(PerformanceInterceptor.class);
		} else {
			this.logger = MyLoggerFactory.getLogger(this.loggerName);
		}
	}

	/**
	 * 设置日志名
	 * 
	 * @param loggerName 日志名
	 */
	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}

	/**
	 * 
	 * 将性能事件压入threadLocal级别的栈中
	 * 
	 * @param pEventMsg 待push的性能事件
	 */
	protected void pushToStack(PerformanceEventMessage pEventMsg) {
		Stack<PerformanceEventMessage> pStack = this.threadMsgStack.get();
		if (pStack == null) {
			pStack = new Stack<PerformanceEventMessage>();
			this.threadMsgStack.set(pStack);
		}
		pStack.push(pEventMsg);
	}

	/**
	 * 
	 * 将性能事件从threadLocal级别的栈中弹出
	 * 
	 * @return 弹出的性能事件
	 */
	protected PerformanceEventMessage popFromStack() {
		Stack<PerformanceEventMessage> pStack = this.threadMsgStack.get();
		if (pStack != null && pStack.size() != 0) {
			return pStack.pop();
		}
		return null;
	}

	/**
	 * 
	 * 取得threadLocal级别的栈的栈顶元素
	 * 
	 * @return 栈顶事件
	 */
	protected PerformanceEventMessage getTopStackElement() {
		Stack<PerformanceEventMessage> pStack = this.threadMsgStack.get();
		if (pStack != null && pStack.size() != 0) {
			return pStack.lastElement();
		}
		return null;
	}

	/**
	 * 
	 * 取得threadLocal级别的栈的第一个性能事件（栈底元素）
	 * 
	 * @return 栈中的第一个性能事件
	 */
	protected PerformanceEventMessage getFirstStackElement() {
		Stack<PerformanceEventMessage> pStack = this.threadMsgStack.get();
		if (pStack != null && pStack.size() != 0) {
			return pStack.firstElement();
		}
		return null;
	}

	/**
	 * 
	 * 取得threadLocal级别的栈的长度
	 * 
	 * @return 栈的长度
	 */
	protected int getStackLength() {
		Stack<PerformanceEventMessage> pStack = this.threadMsgStack.get();
		if (pStack != null) {
			return pStack.size();
		}
		return 0;
	}

	protected String generateFunctionName(ProceedingJoinPoint pjp) {
		Signature signature = pjp.getSignature();
		StringBuffer functionName = new StringBuffer();
		functionName.append(signature.getDeclaringTypeName() + "#");
		functionName.append(signature.getName());
		return functionName.toString();
	}
}
