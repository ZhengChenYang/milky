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

    @Override
    public void postProcess(BeanDefinition bd, MicroKernel microKernel) {
        AopBeanDefinitionPostProcessor beanDefinitionPostProcessor = new AopBeanDefinitionPostProcessor();
        Map<String, Object> customizedFeatureMap = bd.getCustomizedFeatureMap();
        for (Map.Entry<String, Object> entry: customizedFeatureMap.entrySet()){
            Object value = entry.getValue();
            if(value instanceof AopAspect){
                beanDefinitionPostProcessor.addAopAspect((AopAspect)value);
            }
        }
        microKernel.registerBeanDefinitionPostProcessor(beanDefinitionPostProcessor);
    }
}
