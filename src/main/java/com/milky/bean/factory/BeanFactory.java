package com.milky.bean.factory;


import com.milky.bean.factory.support.BeanDefinitionReader;
import com.milky.core.Resource;

/**
 * Created by 52678 on 2018/3/5.
 */
public class BeanFactory implements FactoryContext{
    private BeanDefinitionReader reader;

    public BeanFactory(){

    }

    public BeanDefinitionReader getReader() {
        return reader;
    }

    public void setReader(BeanDefinitionReader reader) {
        this.reader = reader;
    }

    public void loadResource(Resource resource){
        reader.loadResource(resource, this);
    }
}
