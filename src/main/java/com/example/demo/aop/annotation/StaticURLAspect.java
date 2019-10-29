package com.example.demo.aop.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.entity.JSONDataResult;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;

/**
 * 添加@StaticURL注解的方法，若返回值不为空，会为其加上配置的访问路径前缀
 * 
 * @author raining_heavily
 * @date 2019年10月23日
 * @time 下午1:07:00
 */
@Aspect
@Component
public class StaticURLAspect {

	private static final Logger logger = LoggerFactory.getLogger(StaticURLAspect.class);

	@Value("${image.access.url}")
	private String accessPref;

	// 指定切面
	@Pointcut("@annotation(com.example.demo.aop.annotation.StaticURL)")
	public void pointcut() {
	}

	@Around("pointcut()")
	public Object advice(ProceedingJoinPoint joinPoint) throws SystemException {
		logger.info("执行了StaticURL切面");
		JSONDataResult jResult = new JSONDataResult();
		Object proceed = null;
		try {
			proceed = joinPoint.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
			throw new SystemException(ExceptionEnums.UNKNOWN_ERROR);
		}
		if (proceed instanceof JSONDataResult) {
			jResult = (JSONDataResult) proceed;
//			HashMap<String, Object> map = jResult.getData();
//			jResult.add(Dict.ACCESS_PREF, accessPref);
			HashMap<String, Object> map = jResult.getData();
			for (Entry<String, Object> entry : map.entrySet()) {
				Object value = entry.getValue();
				// 如果是com.example.demo.entity中的类
				if (value.getClass().getTypeName().startsWith("com.example.demo.entity")) {
					Object object = handleEntity(entry.getValue());
					// 覆盖掉之前的值
					jResult.add(entry.getKey(), object);
				}
				// 如果是List
				if (value instanceof ArrayList) {
					ArrayList<Object> list = (ArrayList<Object>) value;
					ArrayList<Object> newList = new ArrayList<>();
					for (Object obj : list) {
						//当为list时不再进行类型判断
						Object item = handleEntity(obj);
						newList.add(item);
					}
					jResult.add(entry.getKey(), newList);
				}

			}
		} else {
			MethodSignature sign = (MethodSignature) joinPoint.getSignature();
			Method method = sign.getMethod();
			logger.error(method.getName() + "返回格式错误！Not JSONDataResult");
			throw new SystemException(ExceptionEnums.UNKNOWN_ERROR);
		}
		return jResult;
	}

	/**
	 * 遍历自定义实体类，将标记了@StaticURL注解的字段加上访问前缀
	 * 
	 * @param object
	 * @return
	 */
	private Object handleEntity(Object object) {
		Class<? extends Object> clazz = object.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(StaticURL.class)) {// 字段是否添加了指定注解
				field.setAccessible(true);// 设置操作权限为true
				String name = this.firstUpperCase(field.getName());
				try {
					Method getMethod = clazz.getMethod("get" + name);
					Object value = getMethod.invoke(object);// 获取到该属性的值
					if (value != null&&!"".equals(value)) {
						Method setMethod = clazz.getMethod("set" + name, field.getType());
						value = accessPref + value;
						setMethod.invoke(object, value);
					}
				} catch (Exception e) {
					logger.error("数据处理异常！！！");
					e.printStackTrace();
				}
				logger.info("标记了@StaticURL的字段有：" + clazz.getName() + field.getName());
			}
//			return object;
		}
		return object;
	}

	/**
	 * 将首字母大写
	 * 
	 * @param str
	 * @return
	 */
	public String firstUpperCase(String str) {
		String upperChar = str.substring(0, 1).toUpperCase();
		String remanentChar = str.substring(1);
		return upperChar + remanentChar;
	}

//	@Before("cut()")
//	public void doBefore() {
//		logger.info("-------------------切面before：");
//	}

//	@AfterReturning(value = "@annotation(com.example.demo.aop.annotation.StaticURL)", returning = "wqr")
//	public Object doAfter(JoinPoint joinPoint, Object wqr) {
//		logger.info("---------value:"+wqr);
//		if(wqr != null && !"".equals(wqr.toString())) {
//			wqr = accessPref + wqr;
//		}
//		return wqr;
//	}

//	@After("cut()")
//	public void doAfter() {
//		logger.info("-------------------切面after：");
//	}
//	
//	@AfterThrowing(value="@annotation(com.example.demo.aop.annotation.StaticURL)",throwing = "ex")
//	public void doThrow() {
//		
//	}
}
