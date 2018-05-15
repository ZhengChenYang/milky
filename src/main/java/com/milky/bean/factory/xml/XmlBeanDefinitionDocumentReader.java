package com.milky.bean.factory.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by 52678 on 2018/3/6.
 */
public class XmlBeanDefinitionDocumentReader implements BeanDefinitionDocumentReader{

    private BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegate();
    /**
     * parser XML document
     * @param doc
     */
    @Override
    public void registerBeanDefinitions(Document doc, ReaderContext context) {

        Node root =  doc.getDocumentElement();
        delegate.setReaderContext(context);
        parseAndRegisterBeanDefinitions(root, delegate);
    }

    private void parseAndRegisterBeanDefinitions(Node root, BeanDefinitionParserDelegate delegate){
        parseBeanDefinitions((Element) root, delegate);
    }

    public static void parseBeanDefinitions(Element root, BeanDefinitionParserDelegate delegate){
        NodeList nl = root.getChildNodes();
        for(int i=0; i<nl.getLength(); i++){
            Node node = nl.item(i);
            if(node instanceof Element){
                Element ele = (Element) node;
                if(delegate.isDefaultElement(ele)){
                    parseDefaultElement(ele, delegate);
                }
                else{
                    delegate.parseCustomElement(ele, null);
                }
            }
        }
    }

    private static void parseDefaultElement(Element ele, BeanDefinitionParserDelegate delegate) {
        if(ele.getTagName().equals("bean")){
            try {
                delegate.parseBeanDefinitionElement(ele);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public BeanDefinitionParserDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(BeanDefinitionParserDelegate delegate) {
        this.delegate = delegate;
    }
}
