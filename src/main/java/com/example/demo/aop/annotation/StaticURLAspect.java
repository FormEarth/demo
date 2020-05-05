package com.example.demo.aop.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.baomidou.mybatisplus.core.toolkit.SerializationUtils;
import com.example.demo.common.SystemProperties;
import com.example.demo.util.Util;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
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
 * 注解@StaticURL的切面处理类
 *
 * @author raining_heavily
 * @date 2019年10月23日
 * @time 下午1:07:00
 */
@Aspect
@Component
public class StaticURLAspect {

    private static final Logger logger = LoggerFactory.getLogger(StaticURLAspect.class);

    private String accessPref = SystemProperties.INSTANCE.getInstance().getProperty("image.access.url");

    @Pointcut("@annotation(com.example.demo.aop.annotation.StaticURL)")
    public void pointcut() {
    }

    /**
     * 拦截标记了StaticURL的方法，遍历方法的返回数据，将标记StaticURL注解的属性添加上访问路径
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("pointcut()")
    public Object advice(ProceedingJoinPoint joinPoint) throws Throwable {
        JSONDataResult jResult;
        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable e) {
            logger.error("执行目标方法就出错了啊,不再执行切面，完事了…………………………");
            throw e;
        }
        if (proceed instanceof JSONDataResult) {
            jResult = (JSONDataResult) proceed;
            logger.info("获取的数据："+ jResult);
            HashMap<String, Object> map = jResult.getData();
            for (Entry<String, Object> entry : map.entrySet()) {
                Object value = entry.getValue();

                if (entry.getKey() == "relativePath") {
                    jResult.add(entry.getKey(), accessPref + entry.getValue());
                }
                // 如果是com.example.demo.entity中的类
                if (value.getClass().getTypeName().startsWith("com.example.demo.entity")) {
                    Object newObject = entry.getValue();
                    Object object = handleEntity(newObject);
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
     * 遍历自定义实体类，将标记了@StaticURL注解的字段加上访问前缀，主要String、List判断
     *
     * @param object
     * @return
     */
    private Object handleEntity(Object object) throws SystemException {
        Class<? extends Object> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            {
                // 字段是否添加了指定注解
                if (field.isAnnotationPresent(StaticURL.class)) {
                    logger.info("标记了@StaticURL的字段有：" + clazz.getName() + "," + field.getName());
                    // 设置操作权限为true
                    field.setAccessible(true);
                    //TODO 自定义实体嵌套自定义实体，递归
                    String name = Util.firstUpperCase(field.getName());
                    try {
                        Method getMethod = clazz.getMethod("get" + name);
                        // 获取到该属性的值
                        Object value = getMethod.invoke(object);
                        if (value != null && !"".equals(value)) {
                            Method setMethod = clazz.getMethod("set" + name, field.getType());
                            //字符串的相对路径
                            if (value instanceof String) {
                                value = accessPref + value;
                            } else if (value instanceof ArrayList) {
                                //数组的相对路径
                                ArrayList<String> list = (ArrayList<String>) value;
                                ArrayList<String> newList = new ArrayList<>();
                                for (String relativePath : list) {
                                    newList.add(accessPref + relativePath);
                                }
                                value = newList;
                            }
                            setMethod.invoke(object, value);
                        }
                    } catch (Exception e) {
                        logger.error("切面数据处理异常！！！");
                        throw new SystemException(ExceptionEnums.DEFAULT_FAIL);
                    }
                }
            }
        }
        return object;
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

    @AfterThrowing(value = "@annotation(com.example.demo.aop.annotation.StaticURL)", throwing = "ex")
    public void doThrow(JoinPoint joinPoint, Exception ex) throws Exception {
        logger.error("some exception appear in the aspect");
//        if (ex instanceof SystemException) {
//            logger.error("是SystemException啊");
//            throw ex;
//        } else {
//            throw new SystemException(ExceptionEnums.UNKNOWN_ERROR);
//        }
    }
}
