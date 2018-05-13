package com.milky.core;

import java.lang.reflect.Constructor;

/**
 * Created by 52678 on 2018/5/10.
 */
public interface BeanDefinitionPostProcessor {
    Object postProcess(Constructor constructor, Object[] params, BeanDefinition bd, MicroKernel microKernel);
}
