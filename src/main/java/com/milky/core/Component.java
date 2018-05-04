package com.milky.core;

/**
 * Created by 52678 on 2018/3/23.
 */
public interface Component {

    /** Invoke the function when register bean definition */
    void doInRegistry(String beanName, BeanDefinition beanDefinition);
}
