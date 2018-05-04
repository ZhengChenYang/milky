package com.milky.bean.factory;

import com.milky.bean.factory.xml.ReaderContext;
import com.milky.bean.factory.xml.XmlBeanDefinitionReader;
import com.milky.core.MicroKernel;
import com.milky.core.Resource;

import java.io.Reader;

/**
 * Created by 52678 on 2018/3/5.
 */
public class XmlBeanFactory extends MicroKernel{
    private XmlBeanDefinitionReader reader;

    public XmlBeanFactory(){

    }

    public XmlBeanDefinitionReader getReader() {
        return reader;
    }

    public void setReader(XmlBeanDefinitionReader reader) {
        this.reader = reader;
    }

    public void loadResource(Resource resource){
        ReaderContext readerContext = new ReaderContext(this, reader);
        readerContext.setResource(resource);
        reader.loadResource(resource, readerContext);
    }
}
