package com.milky.bean.factory.xml;

import com.milky.core.Resource;

/**
 * Created by 52678 on 2018/3/6.
 */
public class ReaderContext {

    private XmlBeanDefinitionReader definitionReader;
    private Resource resource;

    public XmlBeanDefinitionReader getDefinitionReader() {
        return definitionReader;
    }

    public void setDefinitionReader(XmlBeanDefinitionReader definitionReader) {
        this.definitionReader = definitionReader;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
