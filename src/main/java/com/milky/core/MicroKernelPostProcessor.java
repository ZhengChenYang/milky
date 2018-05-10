package com.milky.core;

/**
 * Created by 52678 on 2018/5/10.
 */
public interface MicroKernelPostProcessor {
    void postProcess(BeanDefinition bd, MicroKernel microKernel);
}
