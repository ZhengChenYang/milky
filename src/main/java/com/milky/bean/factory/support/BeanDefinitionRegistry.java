package com.milky.bean.factory.support;


import com.milky.core.BeanDefinition;

/**
 * @author Zheng Chenyang
 * @since 201
 */
public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws Exception;

    void removeBeanDefinition(String beanName);

    BeanDefinition getBeanDefinition(String beanName);

    boolean containsBeanDefinition(String beanName);

}
