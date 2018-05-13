package com.milky.aop;

import com.milky.core.BeanDefinition;
import com.milky.core.BeanDefinitionPostProcessor;
import com.milky.core.MicroKernel;
import com.milky.core.MicroKernelPostProcessor;

import java.util.Map;

/**
 * Created by 52678 on 2018/5/10.
 */
public class AopPostFactoryProcessor implements MicroKernelPostProcessor{

    private static final String AOP_KEY = "aop";
    @Override
    public void postProcess(BeanDefinition bd, MicroKernel microKernel) {
        AopBeanDefinitionPostProcessor beanDefinitionPostProcessor = new AopBeanDefinitionPostProcessor();
        Map<String, Object> customizedFeatureMap = bd.getCustomizedFeatureMap();
        Object value = customizedFeatureMap.get(AOP_KEY);
        beanDefinitionPostProcessor.setAopAspect((AopAspect)value);
        microKernel.registerBeanDefinitionPostProcessor(beanDefinitionPostProcessor);
    }
}
