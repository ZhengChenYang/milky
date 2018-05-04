package com.milky.bean.factory.xml;

import org.w3c.dom.Document;

/**
 * Created by 52678 on 2018/3/6.
 */
public interface BeanDefinitionDocumentReader {
    void registerBeanDefinitions(Document doc, ReaderContext readerContext);
}
