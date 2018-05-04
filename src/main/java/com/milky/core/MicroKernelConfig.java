package com.milky.core;

/**
 * Created by 52678 on 2018/3/23.
 */
public class MicroKernelConfig {

    /** whether to allow re-registration of a different bean definition with the same name */
    private boolean allowBeanDefinitionOverriding = true;

    public boolean isAllowBeanDefinitionOverriding(){
        return this.allowBeanDefinitionOverriding;
    }
}
