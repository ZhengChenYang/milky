package com.milky.bean.factory.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Created by 52678 on 2018/3/6.
 */
public class XmlBeanDefinitionDocumentReader implements BeanDefinitionDocumentReader{

    /**
     * parser XML document
     * @param doc
     */
    @Override
    public void registerBeanDefinitions(Document doc, ReaderContext context) {

        Node root = (Element) doc.getDocumentElement();
        BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegate(context);
        parseAndRegisterBeanDefinitions(root, delegate);
    }

    private void parseAndRegisterBeanDefinitions(Node root, BeanDefinitionParserDelegate deletage){
//        if(deletage.isDefault)
    }
}
