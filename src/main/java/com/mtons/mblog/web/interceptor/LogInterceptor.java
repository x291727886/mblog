/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.mtons.mblog.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.DateUtils;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志拦截器
 * @author ThinkGem
 * @version 2018-08-11
 */
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

	private static final ThreadLocal<Long> startTimeThreadLocal =
			new NamedThreadLocal<Long>("LogInterceptor StartTime");
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
		long beginTime = System.currentTimeMillis();// 1、开始时间  
		startTimeThreadLocal.set(beginTime);		// 线程绑定变量（该数据只有当前请求的线程可见）  
		log.debug("开始计时: {}  URI: {}", new SimpleDateFormat("hh:mm:ss.SSS")
				.format(beginTime), request.getRequestURI());
//		if (log.isDebugEnabled()){
//		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
		if (modelAndView != null){
			log.info("ViewName: " + modelAndView.getViewName() + " <<<<<<<<< " + request.getRequestURI() + " >>>>>>>>> " + handler);
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
		long beginTime = startTimeThreadLocal.get();// 得到线程绑定的局部变量（开始时间）
		long endTime = System.currentTimeMillis(); 	// 2、结束时间
		long executeTime = endTime - beginTime;	// 3、获取执行时间
		startTimeThreadLocal.remove(); // 用完之后销毁线程变量数据
		
		// 打印JVM信息。
		Runtime runtime = Runtime.getRuntime();
		log.debug("计时结束: {}  用时: {}  URI: {}  总内存: {}  已用内存: {}",
				DateUtils.formatDate(new Date(endTime), "hh:mm:ss.SSS"), executeTime, request.getRequestURI(),
				runtime.totalMemory(), runtime.totalMemory()-runtime.freeMemory());
		/*if (log.isDebugEnabled()){
		}*/
		
	}

}
