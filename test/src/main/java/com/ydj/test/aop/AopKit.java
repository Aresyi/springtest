package com.ydj.test.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * @author : yidejun
 * @version : 1.0
 * @createTime : 2021/12/14 5:35 下午
 * @description :
 */
@Slf4j
public class AopKit {

    /**
     * 获得【方法】上的注解对象实例
     * <Strong>尝试了多种方式获取...</Strong>
     * @param method
     * @param annotationClass
     * @param <T>
     * @return
     */
    public static <T extends Annotation> T getAnnotationPresent(Method method, Class<T> annotationClass) {
        T annotation = Objects.nonNull(method) ? method.getAnnotation(annotationClass) : null;
        if (Objects.nonNull(annotation)) {
            return annotation;
        }

        annotation = AnnotationUtils.findAnnotation(method,annotationClass);
        if (Objects.nonNull(annotation)) {
            return annotation;
        }

        annotation = AnnotationUtils.getAnnotation(method,annotationClass);
        return annotation;
    }

    /**
     * 获得【类】或【方法】上的注解对象实例
     * @param pjp
     * @param annotationClass
     * @param <T>
     * @return
     */
    public static <T extends Annotation> T getAnnotationPresent(JoinPoint pjp, Class<T> annotationClass) {
        Class<?> clazz = pjp.getTarget().getClass();
        T annotation = Objects.nonNull(clazz) ? clazz.getAnnotation(annotationClass) : null;
        if (Objects.nonNull(annotation)) {
            return annotation;
        }

        Method method = getMethod(pjp);
        annotation = getAnnotationPresent(method,annotationClass);
        return annotation;
    }

    /**
     * 获得切点修饰的方法对象
     * @param pjp
     * @return
     */
    public static Method getMethod(JoinPoint pjp) {
        Signature sig = pjp.getSignature();
        if (!(sig instanceof MethodSignature)) {
            log.error("getMethod error, don't support signature:" + sig.getName());
            return null;
        }

        Method currentMethod = null;
        try {
            MethodSignature mSig = (MethodSignature) sig;
            currentMethod = mSig.getMethod();
            if (Objects.nonNull(currentMethod)){
                return currentMethod;
            }

            Object target = pjp.getTarget();
            currentMethod = target.getClass().getMethod(mSig.getName(), mSig.getParameterTypes());
        } catch (NoSuchMethodException e) {
            log.error("getMethod error, NoSuchMethodException " + sig.getName(), e);
        }
        return currentMethod;
    }

    /**
     * 获取指定类中的相匹配的方法
     * @param beanClazz
     * @param methodName
     * @param paramTypes
     * @return
     */
    public static Method findMethod(Class<?> beanClazz, String methodName, Class<?>... paramTypes) {
        int parameterSize = paramTypes.length;
        Method[] clazzMethods = beanClazz.getMethods();
        Optional<Method> first = Arrays.stream(clazzMethods)
                .filter(one -> Objects.equals(methodName, one.getName())
                        && Objects.nonNull(one.getParameterTypes())
                        && one.getParameterTypes().length == parameterSize)
                .findFirst();

        return first.orElse(ReflectionUtils.findMethod(beanClazz,methodName,paramTypes));
    }


    /**
     * 反射调用执行方法
     * @param target
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    public static Object invokeMethod(Object target, Method method, Object[] args) throws Throwable {
        return AopUtils.invokeJoinpointUsingReflection(target,method,args);
    }

}
