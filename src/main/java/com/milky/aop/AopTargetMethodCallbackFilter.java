package com.milky.aop;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

/**
 * Created by 52678 on 2018/5/15.
 */
public class AopTargetMethodCallbackFilter implements CallbackFilter{
    private Method[] toBeProxyMethods;

    public AopTargetMethodCallbackFilter(Method[] methods){
        this.toBeProxyMethods = methods;
    }

    public Method[] getMethods() {
        return toBeProxyMethods;
    }

    public void setMethods(Method[] methods) {
        this.toBeProxyMethods = methods;
    }

    @Override
    /**
     * 0, represents that the method won't be proxy.
     * 1, represents that the method will be proxy.
     */
    public int accept(Method method) {
        for(Method toBePoxyMethod: this.toBeProxyMethods){
            if(toBePoxyMethod.equals(method)){
                return 1;
            }
        }
        return 0;
    }
}
