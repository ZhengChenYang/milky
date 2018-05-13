package com.milky.aop;



import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by 52678 on 2018/5/13.
 */
public class TargetInterceptor implements MethodInterceptor {

    private Method[] methods;
    private AopAspect aopAspect;

    public TargetInterceptor(Method[] methods, AopAspect aspect){
        this.methods = methods;
        this.aopAspect = aspect;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        return null;
    }
}


