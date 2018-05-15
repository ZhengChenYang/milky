package com.milky.aop;



import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by 52678 on 2018/5/13.
 */
public class TargetInterceptor implements MethodInterceptor {

    private AopAspect aopAspect;
    private Method beforeMethod;
    private Method afterMethod;
    private Method afterThrowingMethod;
    private Object aopAspectObject;

    public TargetInterceptor(AopAspect aspect){
        this.aopAspect = aspect;

        //创建对象
        this.aopAspectObject = this.aopAspect.getAspectRefObject();
        try {
            this.beforeMethod = this.aopAspectObject.getClass().getMethod(aopAspect.getBeforeMethod());
            this.afterMethod = this.aopAspectObject.getClass().getMethod(aopAspect.getAfterMethod());
            this.afterThrowingMethod = this.aopAspectObject.getClass().getMethod(aopAspect.getAfterThrowingMethod());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object intercept(Object o, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
        Object result = null;
        try{
            beforeMethod.invoke(this.aopAspectObject);
            result = methodProxy.invokeSuper(o, params);
            afterMethod.invoke(this.aopAspectObject);
        }
        catch (Exception e){
            afterThrowingMethod.invoke(this.aopAspectObject);
            e.printStackTrace();
        }

        return result;
    }
}


