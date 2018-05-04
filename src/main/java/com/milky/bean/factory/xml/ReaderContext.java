package com.milky.bean.factory.xml;

import com.milky.core.MicroKernel;
import com.milky.core.Resource;
import com.sun.glass.ui.ClipboardAssistance;

/**
 * Created by 52678 on 2018/3/6.
 */
public class ReaderContext {

    private XmlBeanDefinitionReader definitionReader;
    private Resource resource;
    private MicroKernel microKernel;

    public ReaderContext(MicroKernel microKernel, XmlBeanDefinitionReader reader){
        this.microKernel = microKernel;
        this.definitionReader = reader;
    }
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

    public MicroKernel getMicroKernel() {
        return microKernel;
    }

    public void setMicroKernel(MicroKernel microKernel) {
        this.microKernel = microKernel;
    }

    public Class getCachedClass(String className){
        return this.microKernel.getCachedClass(className);
    }
}
