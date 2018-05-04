package com.milky.bean.factory.support;

import com.milky.bean.factory.FactoryContext;
import com.milky.core.Resource;

/**
 * Created by 52678 on 2018/3/5.
 */
public interface BeanDefinitionReader {
    void loadResource(Resource resource, FactoryContext context);
}
