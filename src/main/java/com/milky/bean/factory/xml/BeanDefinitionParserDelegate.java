package com.milky.bean.factory.xml;

import com.milky.bean.factory.util.BeanUtils;
import com.milky.bean.factory.xml.tag.XmlTagParser;
import com.milky.core.BeanDefinition;
import com.milky.core.ConstructorArgumentValues;
import com.milky.core.PropertyValue;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by lenovo on 2018/4/28.
 */
public class BeanDefinitionParserDelegate {
    public static final String BEANS_NAMESPACE_URI = "http://www.milky.org/schema/beans";

    private static final String BEAN_ELEMENT = "bean";
    private static final String ID_ATTRIBUTE = "id";
    private static final String CLASS_ATTRIBUTE = "class";
    private static final String SCOPE_ATTRIBUTE = "scope";
    private static final String CONSTRUCTOR_ARG_ELEMENT = "constructor-arg";
    private static final String INDEX_ATTRIBUTE = "index";

    private static final String VALUE_ELEMENT = "value";
    private static final String VALUE_ATTRIBUTE = "value";

    private static final String TYPE_ATTRIBUTE = "type";
    private static final String PROPERTY_ELEMENT = "property";

    private static final String REF_ELEMENT = "ref";
    private static final String REF_ATTRIBUTE = "ref";

    private static final String ARRAY_ELEMENT = "array";
    private static final String LIST_ELEMENT = "list";
    private static final String SET_ELEMENT = "set";
    private static final String MAP_ELEMENT = "map";

    private static final String VALUE_TYPE_ATTRIBUTE = "value-type";

    private static final String KEY_TYPE_ATTRIBUTE = "key-type";

    private static final String KEY_ATTRIBUTE = "key";

    private static final String NAME_ATTRIBUTE = "name";

    private static final String BEAN_ATTRIBUTE = "bean";
    private static final String VALUE_REF_ATTRIBUTE ="value-ref";

    private static final String ENTRY_ELEMENT = "entry";

    private Set<String> defaultTagNameSet = new HashSet<String>();

    /** map of the parsers for custom tag **/
    private Map<String, XmlTagParser> customTagParser = new HashMap<String, XmlTagParser>();

    private ReaderContext readerContext;

    public BeanDefinitionParserDelegate(){

        defaultTagNameSet.add(BEAN_ELEMENT);
        defaultTagNameSet.add(VALUE_ELEMENT);
        defaultTagNameSet.add(REF_ELEMENT);
        defaultTagNameSet.add(ARRAY_ELEMENT);
        defaultTagNameSet.add(LIST_ELEMENT);
        defaultTagNameSet.add(SET_ELEMENT);
        defaultTagNameSet.add(MAP_ELEMENT);
        defaultTagNameSet.add(ENTRY_ELEMENT);
        defaultTagNameSet.add(CONSTRUCTOR_ARG_ELEMENT);
        defaultTagNameSet.add(PROPERTY_ELEMENT);

    }

    public ReaderContext getReaderContext() {
        return readerContext;
    }

    public void setReaderContext(ReaderContext readerContext) {
        this.readerContext = readerContext;
    }

    public void parseBeanDefinitionElement(Element ele) throws Exception {
        String id = ele.getAttribute(ID_ATTRIBUTE);
        if(BeanUtils.isBlank(id)){
            throw new Exception("id attribute cannot be null!");
        }
        else if(readerContext.containsBeanName(id)){
            throw new Exception("id cannot be repetitive!");
        }

        String className = ele.getAttribute(CLASS_ATTRIBUTE);
        if(BeanUtils.isBlank(className)){
            throw new Exception("class attribute cannot be blank!");
        }
        Class clazz = this.readerContext.getCachedClass(className);

        String scope = ele.getAttribute(SCOPE_ATTRIBUTE);

        BeanDefinition bd = new BeanDefinition();

        // 解析默认属性
        bd.setName(id);
        bd.setBeanClass(clazz);

        if(!BeanUtils.isBlank(scope)){
            bd.setScope(scope);
        }

        // 解析构造函数参数
        parseConstructorArgElements(ele, bd);
    
        // 解析属性值
        parsePropertyElements(ele, bd);

        // 解析自定义标签
        parseCustomElements(ele, bd);

        // register the bean definition in the micro kernel
        readerContext.registerBeanDefinition(bd);
    }

    private void parsePropertyElements(Element beanEle, BeanDefinition bd) throws Exception {
        NodeList nl = beanEle.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if(node instanceof Element){
                Element element = (Element) node;
                if (getTagName(element).equals(PROPERTY_ELEMENT)) {
                    parsePropertyElement((Element) node, bd);
                }
            }
        }
    }

    private void parsePropertyElement(Element ele, BeanDefinition bd) throws Exception {
        String propertyName = ele.getAttribute(NAME_ATTRIBUTE);
        Object val = null;

        PropertyValue propertyValue = new PropertyValue();
        propertyValue.setName(propertyName);
        NodeList nl = ele.getChildNodes();
        Element subElement = null;
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                if (subElement != null) {
                    throw new Exception(" must not contain more than one sub-element");
                } else {
                    subElement = (Element) node;
                }
            }
        }
        boolean hasRefAttribute = ele.hasAttribute(REF_ATTRIBUTE);
        boolean hasValueAttribute = ele.hasAttribute(VALUE_ATTRIBUTE);
        if (hasRefAttribute) {
            String ref = ele.getAttribute(REF_ATTRIBUTE);
            propertyValue.setRef(ref);
        }
        else if (hasValueAttribute) {
            String value = ele.getAttribute(VALUE_ATTRIBUTE);
            propertyValue.setValue(value);
        }
        else if (subElement != null) {
            Object value = parsePropertySubElement(subElement, bd);
            propertyValue.setValue(value);
        }
        bd.getPropertyValues().addPropertyValue(propertyValue);

    }

    public void parseCustomElements(Element ele, BeanDefinition bd) {
        BeanDefinition bd2 = bd;
        NodeList nl = ele.getChildNodes();
        for(int i=0; i<nl.getLength(); i++){
            Node node = nl.item(i);
            if(node instanceof  Element){
                if(bd2==null){
                    bd2 = new BeanDefinition();
                }
                Element element = (Element) node;
                if(isDefaultElement(element)){
                    continue;
                }
                else{
                    String tagName = getTagName(element);
                    XmlTagParser parser = this.customTagParser.get(tagName);
                    if(parser!=null){
                        parser.parse(element, bd);
                    }
                }
            }
        }
    }

    public void parseCustomElement(Element ele, BeanDefinition bd){
        BeanDefinition bd2 = bd;
        if(bd2==null){
            bd2 = new BeanDefinition();
        }
        String tagName = getTagName(ele);
        XmlTagParser parser = this.customTagParser.get(tagName);
        if(parser!=null){
            parser.parse(ele, bd2);
        }
        readerContext.registerBeanDefinition(bd2);
    }


    public void parseConstructorArgElements(Element ele, BeanDefinition bd) throws Exception {
        NodeList nl = ele.getChildNodes();
        for(int i=0; i<nl.getLength(); i++){
            Node node = nl.item(i);
            if(node instanceof Element){
                Element element = (Element) node;
                if(getTagName(element).equals(CONSTRUCTOR_ARG_ELEMENT)){
                    parseConstructorArgElement(element, bd);
                }
            }
        }
    }

    public void parseConstructorArgElement(Element ele, BeanDefinition bd) throws Exception {
        //String indexAttr = ele.getAttribute(INDEX_ATTRIBUTE);
        String typeAttr = ele.getAttribute(TYPE_ATTRIBUTE);

        ConstructorArgumentValues.ValueHolder valueHolder = parsePropertyValue(ele, bd);

        if(BeanUtils.isBlank(valueHolder.getType())){
           if(BeanUtils.isBlank(typeAttr)){
               throw new Exception("type attribute cannot be blank");
           }
           else {
               valueHolder.setType(typeAttr);
           }
        }

        int size = bd.getConstructorArgumentValues().getNrOfArguments();
        bd.getConstructorArgumentValues().addArgumentValue(size, valueHolder);
    }


    private ConstructorArgumentValues.ValueHolder parsePropertyValue(Element ele, BeanDefinition bd) throws Exception {

        NodeList nl = ele.getChildNodes();
        Element subElement = null;

        ConstructorArgumentValues.ValueHolder valueHolder = new ConstructorArgumentValues.ValueHolder();

        for(int i=0; i<nl.getLength(); i++){
            Node node = nl.item(i);
            if(node instanceof Element) {
                if(subElement!=null){
                    throw new Exception("must not contain more than one sub-element");
                }
                else{
                    subElement = (Element) node;
                }
            }
        }

        if(ele.hasAttribute(REF_ATTRIBUTE)){
            String ref = ele.getAttribute(REF_ATTRIBUTE);
            valueHolder.setRef(ref);
        }
        else if(ele.hasAttribute(VALUE_ATTRIBUTE)){
            String value = ele.getAttribute(VALUE_ATTRIBUTE);
            valueHolder.setValue(value);

        }
        else if(subElement!=null){
            Object value = parsePropertySubElement(subElement, bd);
            valueHolder.setValue(value);
            if(value instanceof ManagedArray){
                valueHolder.setType("array");
            }
            else if(value instanceof List){
                valueHolder.setType("list");
            }
            else if(value instanceof Map){
                valueHolder.setType("map");
            }
            else if(value instanceof Set){
                valueHolder.setType("set");
            }

        }
        return valueHolder;

    }

    private Object parsePropertySubElement(Element ele, BeanDefinition bd) throws Exception {

        String nodeName = ele.getTagName().toLowerCase();
        if(nodeName.equals(LIST_ELEMENT)){
            return parseListElement(ele, bd);
        }
        else if(nodeName.equals(ARRAY_ELEMENT)){
            return parseArrayElement(ele, bd);
        }
        else if(nodeName.equals(MAP_ELEMENT)){
            return parseMapElement(ele, bd);
        }
        else if(nodeName.equals(SET_ELEMENT)){
            return parseSetElement(ele, bd);
        }
        else{
            throw new Exception("unknown property!");
        }
    }

    private Object parseSetElement(Element ele, BeanDefinition bd) {
        String elementTypeName = ele.getAttribute(VALUE_TYPE_ATTRIBUTE);
        NodeList nl = ele.getChildNodes();
        ManagedSet managedSet = new ManagedSet();
        managedSet.setElementTypeName(elementTypeName);
        for(int i=0; i<nl.getLength(); i++){
            Node node = nl.item(i);
            if(node instanceof Element){
                Element element = (Element) node;
                if(getTagName(element).equals(REF_ELEMENT)){
                    String ref = element.getAttribute(BEAN_ATTRIBUTE);
                    managedSet.addRefEntry(ref);
                }
                else if(getTagName(element).equals(VALUE_ELEMENT)){
                    String value = element.getTextContent();
                    managedSet.addValueEntry(value);
                }
            }
        }
        return  managedSet;
    }

    private Object parseMapElement(Element ele, BeanDefinition bd) throws Exception {
        String keyType = ele.getAttribute(KEY_TYPE_ATTRIBUTE);
        String valueType = ele.getAttribute(VALUE_TYPE_ATTRIBUTE);
        NodeList nl = ele.getChildNodes();

        ManagedMap managedMap = new ManagedMap();
        managedMap.setKeyTypeName(keyType);
        managedMap.setValueTypeName(valueType);
        for(int i=0; i<nl.getLength(); i++){
            Node node = nl.item(i);
            if(node instanceof Element){
                Element element = (Element) node;
                if(getTagName(element).equals(ENTRY_ELEMENT)){
                    String key = element.getAttribute(KEY_ATTRIBUTE);
                    String value = element.getAttribute(VALUE_ATTRIBUTE);
                    String valueRef = element.getAttribute(VALUE_REF_ATTRIBUTE);
                    // 属性转换
                    if(value!=null){
                        managedMap.addValueEntry(key, value);
                    }
                    else if(valueRef!=null){
                        managedMap.addRefEntry(key, valueRef);
                    }
                }
                else{
                    throw new Exception("only entry tag can be parsed!");
                }
            }
        }
        //TODO bd增加
        return  managedMap;
    }

    private Object parseArrayElement(Element ele, BeanDefinition bd) {
        String elementType = ele.getAttribute(VALUE_TYPE_ATTRIBUTE);

        NodeList nl = ele.getChildNodes();
        ManagedArray managedArray = new ManagedArray();
        if(!BeanUtils.isBlank(elementType)){
            managedArray.setElementTypeName(elementType);
        }

        for(int i=0; i<nl.getLength(); i++){
            Node node = nl.item(i);
            if(node instanceof Element) {
                Element element = (Element) node;
                if(getTagName(element).equals(REF_ELEMENT)){
                    String ref = element.getAttribute(BEAN_ATTRIBUTE);
                    managedArray.addRefEntry(ref);
                }
                else if(getTagName(element).equals(VALUE_ELEMENT)){
                    String value = element.getTextContent();
                    managedArray.addValueEntry(value);
                }
            }
        }
        return managedArray;
    }

    private Object parseListElement(Element ele, BeanDefinition bd) {
        String elementType = ele.getAttribute(VALUE_TYPE_ATTRIBUTE);
        NodeList nl = ele.getChildNodes();
        ManagedList managedList = new ManagedList();
        managedList.setElementTypeName(elementType);
        for(int i=0; i<nl.getLength(); i++){
            Node node = nl.item(i);
            if(node instanceof Element) {
                Element element = (Element) node;
                if(getTagName(element).equals(REF_ELEMENT)){
                    String ref = element.getAttribute(BEAN_ATTRIBUTE);
                    managedList.addRefEntry(ref);
                }
                else if(getTagName(element).equals(VALUE_ELEMENT)){
                    String value = element.getTextContent();
                    managedList.addValueEntry(value);
                }
            }
        }
        return managedList;
    }

    public boolean isDefaultElement(Element ele) {
        String tagName = ele.getTagName().trim().toLowerCase();
        return this.defaultTagNameSet.contains(tagName);
    }

    public String getTagName(Element element){
        return element.getTagName().toLowerCase();
    }

    public void registerCustomTagParser(String tagName, XmlTagParser parser) throws Exception {
        if(this.defaultTagNameSet.contains(tagName)){
            throw new Exception("cannot register a default tag name");
        }
        this.customTagParser.put(tagName, parser);
    }

    public void printXmlTagParser(){
        System.out.println(this.customTagParser);
    }
}
