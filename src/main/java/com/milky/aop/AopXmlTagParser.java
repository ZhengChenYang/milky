package com.milky.aop;

import com.milky.bean.factory.util.BeanUtils;
import com.milky.bean.factory.xml.tag.XmlTagParser;
import com.milky.core.BeanDefinition;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by 52678 on 2018/5/10.
 */
public class AopXmlTagParser implements XmlTagParser {
    @Override
    public void parse(Element ele, BeanDefinition bd) {
        try {
            parseAopAspectElements(ele, bd);
            bd.setBeanClass(AopPostFactoryProcessor.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseAopAspectElements(Element ele, BeanDefinition bd) throws Exception {
        NodeList nl = ele.getChildNodes();
        int numOfAspect = 0;
        for(int i=0; i<nl.getLength(); i++){
            Node node = nl.item(i);
            if(node instanceof Element){
                Element element = (Element)node;
                if(element.getTagName().toLowerCase().equals("aop-aspect")){
                    String refAttr = element.getAttribute("ref");
                    if(BeanUtils.isBlank(refAttr)){
                        throw new Exception("ref attribute cannot be blank!");
                    }
                    AopAspect aopAspect = new AopAspect();
                    String pointcut = parsePointCut(element);
                    String beforeMethod = parseBeforeMethod(element);
                    String afterMethod = parseAfterMethod(element);
                    String afterThrowingMethod = parseThrowingMethod(element);

                    aopAspect.setAspectRef(refAttr);
                    aopAspect.setExpression(pointcut);
                    aopAspect.setBeforeMethod(beforeMethod);
                    aopAspect.setAfterMethod(afterMethod);
                    aopAspect.setAfterThrowingMethod(afterThrowingMethod);

                    String featureName = "aop";
                    bd.addCustomizedFeature(featureName, aopAspect);
                    break;
                }
            }
        }
    }

    private String parseAfterMethod(Element ele) {
        NodeList nl = ele.getChildNodes();
        for(int i=0; i<nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                if (element.getTagName().toLowerCase().equals("aop-after")) {
                    return element.getAttribute("method");
                }
            }
        }
        return null;
    }

    private String parseBeforeMethod(Element ele) {
        NodeList nl = ele.getChildNodes();
        for(int i=0; i<nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                if (element.getTagName().toLowerCase().equals("aop-before")) {
                    return element.getAttribute("method");
                }
            }
        }
        return null;
    }

    private String parsePointCut(Element ele) {
        NodeList nl = ele.getChildNodes();
        for(int i=0; i<nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                if (element.getTagName().toLowerCase().equals("aop-pointcut")) {
                    return element.getAttribute("expression");
                }
            }
        }
        return null;
    }

    private String parseThrowingMethod(Element ele) {
        NodeList nl = ele.getChildNodes();
        for(int i=0; i<nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                if (element.getTagName().toLowerCase().equals("aop-after-throwing")) {
                    return element.getAttribute("method");
                }
            }
        }
        return null;
    }

    @Override
    public String getTagName() {
        return "aop-config";
    }
}
