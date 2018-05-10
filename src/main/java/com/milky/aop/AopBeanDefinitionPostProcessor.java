package com.milky.aop;

import com.milky.core.BeanDefinition;
import com.milky.core.BeanDefinitionPostProcessor;
import com.milky.core.MicroKernel;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by 52678 on 2018/5/10.
 */
public class AopBeanDefinitionPostProcessor implements BeanDefinitionPostProcessor {

    private List<AopAspect> aopAspectList = new LinkedList<AopAspect>();

    @Override
    public void postProcess(BeanDefinition bd, MicroKernel microKernel) {
        // 通过界面配置，增强bean
    }

    public void addAopAspect(AopAspect aopAspect){
        this.aopAspectList.add(aopAspect);
    }
}
