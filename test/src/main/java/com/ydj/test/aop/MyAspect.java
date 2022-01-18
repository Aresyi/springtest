package com.ydj.test.aop;

import com.ydj.test.aop.bean.Entity;
import com.ydj.test.aop.bean.Param;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Aspect
@Component
public class MyAspect implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("applicationContext = " + applicationContext);
        MyAspect.applicationContext = applicationContext;
    }

//    // 定义切点（切入位置）
//    @Pointcut("execution(* com.ydj.test.aop.bean.TestBean.*(..))")
//    private void pointcut(){}
//
//
//    @Before("pointcut()")
//    public void before(JoinPoint joinPoint){
//        System.out.println("我是前置通知");
//    }

    @AfterThrowing("@within(com.ydj.test.aop.Retry)")
    public void afterThrowing(JoinPoint joinPoint){
        System.out.println("出现异常。。。。。。");
    }

    @AfterThrowing(value = "@annotation(com.ydj.test.aop.Retry)",throwing = "ex")
    public void doException(JoinPoint pjp,Exception ex) throws IOException {
        String flag = Hold.get();
        System.out.println("出现异常。。。。。。afterThrowing4Method......"+ex + ", flag = " + flag);
        if (Objects.nonNull(flag)){
            System.out.println("重试执行 flag = " + flag);
            return;
        }

        Method method = AopKit.getMethod(pjp);
        Class<?> returnType=null;
        if (method != null){
            returnType = method.getReturnType();
            System.out.println("returnType = " + returnType);
        }

        String methodName = method.getName();
        System.out.println("methodName = " + methodName);
        Class<?> declaringClass = method.getDeclaringClass();
        System.out.println("declaringClass = " + declaringClass);

        Object[] args = pjp.getArgs();
        String argsStr = JacksonUtils.beanToString(args);
        System.out.println("args = " +  Arrays.asList(args));
        System.out.println("args = " +  argsStr);


//        Object[] objects = JacksonUtils.stringToBean(argsStr, Object[].class);
//
//        Class<?>[] parameterTypes = method.getParameterTypes();
//        for (Class clazz : parameterTypes){
//            System.out.println("clazz = " + clazz);
//        }

        List<Param> paramList = new ArrayList<>();
        for (Object obj : args){
            System.out.println("obj = " + obj.getClass() + "--->" + JacksonUtils.beanToString(obj));

            Param one = Param.builder().clazz(obj.getClass()).jsonStr(JacksonUtils.beanToString(obj)).build();
            paramList.add(one);
        }

        Entity en = Entity.builder().clazz(declaringClass).methodName(methodName).paramList(paramList).build();
        System.out.println("Entity = " + JacksonUtils.beanToString(en));


        retry(declaringClass,method,en);

        Retry annotation = AopKit.getAnnotationPresent(pjp, Retry.class);
        Class<? extends Throwable>[] classes = annotation.supportTypes();
        boolean b = Arrays.stream(classes).anyMatch(one -> one == ex.getClass());
        if (b){
            System.out.println("我支持的异常");
        }

    }

    private void retry(Class declaringClass, Method method, Entity entity) throws IOException {

        int size = entity.getParamList().size();

        Object bean = applicationContext.getBean(declaringClass);
        Object[] objects = new Object[size];
        for (int i = 0 ; i < size; i++){
            Param param = entity.getParamList().get(i);
            System.out.println("param = " + param);
            objects[i] = JacksonUtils.stringToBean(param.getJsonStr(),param.getClazz());
//            objects[i] = JSONObject.parseObject(param.getJsonStr(),param.getClazz());
        }

        try {
            System.out.println("----------------------重试测试 开始----------------------");
            Hold.put(UUID.randomUUID().toString());
            method.invoke(bean,objects);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }finally {
            Hold.clean();
            System.out.println("----------------------重试测试 结束----------------------");
        }
    }

}