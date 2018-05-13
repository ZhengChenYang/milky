package com.milky.aop;

import com.milky.core.BeanDefinition;
import com.milky.core.BeanDefinitionPostProcessor;
import com.milky.core.MicroKernel;
import net.sf.cglib.proxy.Enhancer;
import org.springframework.aop.config.AspectComponentDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 52678 on 2018/5/10.
 */
public class AopBeanDefinitionPostProcessor implements BeanDefinitionPostProcessor {

    private AopAspect aopAspect;

    @Override
    public Object postProcess(Constructor constructor, Object[] params, BeanDefinition bd, MicroKernel microKernel) {
        // 通过切面配置，增强bean
        // 查看当前bean definition是否匹配
        Object result = null;
        Method[] methods = bd.getBeanClass().getMethods();
        String str = aopAspect.getExpression();
        Pattern pattern = Pattern.compile(str);
        List<Method> proxyMethods = new ArrayList<Method>();
        for(int i=0; i<methods.length; i++){
            String methodName = bd.getBeanClass()+" "+methods[i].getName();
            Matcher matcher = pattern.matcher(methodName);
            if(matcher.find()){
                proxyMethods.add(methods[i]);
            }
        }

        if(proxyMethods.size()<=0){
            return null;
        }

        Object aspectRef = null;
        try {
            aspectRef = microKernel.getBean(aopAspect.getAspectRef());
            aopAspect.setAspectRefObejct(aspectRef);
            result = createProxyObject(constructor, params, bd.getBeanClass(),
                    (Method[]) proxyMethods.toArray(), aopAspect);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    private Object createProxyObject(Constructor constructor, Object[] params, Class superClass, Method[] proxyMethods,
                                     AopAspect aopAspect){
        Class[] argsTypes = constructor.getParameterTypes();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(superClass);
        enhancer.setCallback(new TargetInterceptor(proxyMethods, aopAspect));
        return enhancer.create(argsTypes, params);
    }

    public AopAspect getAopAspect() {
        return aopAspect;
    }

    public void setAopAspect(AopAspect aopAspect) {
        this.aopAspect = aopAspect;
    }
}
