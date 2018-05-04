package com.milky.bean.factory.xml;

import com.milky.core.BeanDefinition;
import com.milky.core.ConstructorArgumentValues;
import com.milky.core.PropertyValue;
import com.milky.core.PropertyValues;
import com.sun.java.swing.plaf.windows.TMSchema;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by lenovo on 2018/4/28.
 */
public class BeanDefinitionParserDelegate {
    public static final String BEANS_NAMESPACE_URI = "http://www.milky.org/schema/beans";

    private static final String ID_ATTRIBUTE = "id";
    private static final String CLASS_ATTRIBUTE = "class";
    private static final String SCOPE_ATTRIBUTE = "scope";
    private static final String CONSTRUCTOR_ARG_ATTRIBUTE = "constructor-arg";
    private static final String INDEX_ATTRIBUTE = "index";
    private static final String VALUE_ATTRIBUTE = "value";
    private static final String TYPE_ATTRIBUTE = "type";
    private static final String REF_ATTRIBUTE = "ref";
    private static final String ARRAY_ATTRIBUTE = "array";
    private static final String LIST_ATTRIBUTE = "list";
    private static final String SET_ATTRIBUTE = "set";
    private static final String MAP_ATTRIBUTE = "map";

    private static final String VALUE_TYPE_ATTRIBUTE = "value-type";

    private static final String KEY_TYPE_ATTRIBUTE = "key-type";

    private static final String KEY_ATTRIBUTE = "key";

    private static final String NAME_ATTRIBUTE = "name";

    private static final String BEAN_ATTRIBUTE = "bean";

    private Set<String> defaultTagNameSet = new HashSet<String>();

    private ReaderContext readerContext;

    public BeanDefinitionParserDelegate(ReaderContext readerContext){
        this.readerContext = readerContext;

        defaultTagNameSet.add(ID_ATTRIBUTE);
        defaultTagNameSet.add(CLASS_ATTRIBUTE);
        defaultTagNameSet.add(CONSTRUCTOR_ARG_ATTRIBUTE);
        defaultTagNameSet.add(INDEX_ATTRIBUTE);
        defaultTagNameSet.add(VALUE_ATTRIBUTE);
        defaultTagNameSet.add(TYPE_ATTRIBUTE);
        defaultTagNameSet.add(REF_ATTRIBUTE);
        defaultTagNameSet.add(ARRAY_ATTRIBUTE);
        defaultTagNameSet.add(LIST_ATTRIBUTE);
        defaultTagNameSet.add(SET_ATTRIBUTE);
        defaultTagNameSet.add(MAP_ATTRIBUTE);
        defaultTagNameSet.add(VALUE_TYPE_ATTRIBUTE);
        defaultTagNameSet.add(KEY_TYPE_ATTRIBUTE);
        defaultTagNameSet.add(KEY_ATTRIBUTE);
        defaultTagNameSet.add(NAME_ATTRIBUTE);
        defaultTagNameSet.add(BEAN_ATTRIBUTE);
    }

    public boolean isDefaultNamespace(Element ele) {
        return ele.isDefaultNamespace(BEANS_NAMESPACE_URI);
    }

    public void parseBeanDefinitionElement(Element ele) throws Exception {
        String id = ele.getAttribute(ID_ATTRIBUTE);
        String className = ele.getAttribute(CLASS_ATTRIBUTE);
        Class clazz = this.readerContext.getCachedClass(className);
        String scope = ele.getAttribute(SCOPE_ATTRIBUTE);

        BeanDefinition bd = new BeanDefinition();

        // 解析默认属性
        bd.setName(id);
        bd.setBeanClass(clazz);
        bd.setScope(scope);

        // 解析构造函数参数
        parseConstructorArgElements(ele, bd);
    
        // 解析属性值
        parsePropertyElements(ele, bd);

        // 解析自定义标签
        parseCustomElements(ele, bd);
    }

    private void parsePropertyElements(Element beanEle, BeanDefinition bd) throws Exception {
        NodeList nl = beanEle.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (((Element)node).getTagName().equals("property")) {
                parsePropertyElement((Element) node, bd);
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
        bd.getPropertyValues().getPropertyValueList().add(propertyValue);

    }

    public void parseCustomElements(Element ele) {

    }

    public void parseCustomElements(Element ele, BeanDefinition bd) {

    }


    public Object parseCustomElement(Element ele, BeanDefinition bd){
        return null;
    }


    public void parseConstructorArgElements(Element ele, BeanDefinition bd) throws Exception {
        NodeList nl = ele.getChildNodes();
        for(int i=0; i<nl.getLength(); i++){
            Node node = nl.item(i);
            if(node instanceof Element){
                if(((Element) node).getTagName().equals(CONSTRUCTOR_ARG_ATTRIBUTE)){
                    parseConstructorArgElement(ele, bd);
                }
            }
        }
    }

    public void parseConstructorArgElement(Element ele, BeanDefinition bd) throws Exception {
        String indexAttr = ele.getAttribute(INDEX_ATTRIBUTE);
        String typeAttr = ele.getAttribute(TYPE_ATTRIBUTE);

        ConstructorArgumentValues.ValueHolder valueHolder = parsePropertyValue(ele, bd);

        valueHolder.setType(typeAttr);
        Map<Integer, ConstructorArgumentValues.ValueHolder> indexedArgumentValues
                = bd.getConstructorArgumentValues().getIndexedArgumentValues();
        indexedArgumentValues.put(Integer.getInteger(indexAttr), valueHolder);
    }


    private ConstructorArgumentValues.ValueHolder parsePropertyValue(Element ele, BeanDefinition bd) throws Exception {

        NodeList nl = ele.getChildNodes();
        Element subElement = null;

        ConstructorArgumentValues.ValueHolder valueHolder = new ConstructorArgumentValues.ValueHolder();

        for(int i=0; i<nl.getLength(); i++){
            Node node = nl.item(i);
            if(subElement!=null){
                throw new Exception("must not contain more than one sub-element");
            }
            else{
                subElement = (Element) node;
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
        }
        return valueHolder;

    }

    private Object parsePropertySubElement(Element ele, BeanDefinition bd) throws Exception {
        if(!isDefaultNamespace(ele)){
            return parseCustomElement(ele, bd);
        }
        String nodeName = ele.getTagName().toLowerCase();
        if(nodeName.equals(LIST_ATTRIBUTE)){
            return parseListElement(ele, bd);
        }
        else if(nodeName.equals(ARRAY_ATTRIBUTE)){
            return parseArrayElement(ele, bd);
        }
        else if(nodeName.equals(MAP_ATTRIBUTE)){
            return parseMapElement(ele, bd);
        }
        else if(nodeName.equals(SET_ATTRIBUTE)){
            return parseSetElement(ele, bd);
        }
        else{
            throw new Exception("unknown property!");

        }
    }

    private Object parseSetElement(Element ele, BeanDefinition bd) {
        String elementTypeName = ele.getAttribute(VALUE_TYPE_ATTRIBUTE);
        NodeList nl = ele.getChildNodes();
        ManagedSet<Object> managedSet = new ManagedSet<Object>();
        managedSet.setElementTypeName(elementTypeName);
        for(int i=0; i<nl.getLength(); i++){
            Element element = (Element) nl.item(i);
            if(element.getTagName().toLowerCase().equals("ref")){
                String bean = element.getAttribute(BEAN_ATTRIBUTE);
                managedSet.add(bean);
            }
        }
        return  managedSet;
    }

    private Object parseMapElement(Element ele, BeanDefinition bd) {
        String keyType = ele.getAttribute(KEY_TYPE_ATTRIBUTE);
        String valueType = ele.getAttribute(VALUE_TYPE_ATTRIBUTE);
        NodeList nl = ele.getChildNodes();

        ManagedMap<Object, Object> managedMap = new ManagedMap<Object, Object>();
        managedMap.setKeyTypeName(keyType);
        managedMap.setValueTypeName(valueType);
        for(int i=0; i<nl.getLength(); i++){
            Element entry = (Element) nl.item(i);
            if(entry.getTagName().toLowerCase().equals("entry")){
                String key = entry.getAttribute(KEY_ATTRIBUTE);
                String value = entry.getAttribute(VALUE_ATTRIBUTE);
                // 属性转换
                managedMap.put(key, value);
            }
        }
        //TODO bd增加
        return  managedMap;
    }

    private Object parseArrayElement(Element ele, BeanDefinition bd) {
        String elementType = ele.getAttribute(VALUE_TYPE_ATTRIBUTE);
        NodeList nl = ele.getChildNodes();
        ManagedArray<Object> managedArray = new ManagedArray<Object>();
        managedArray.setElementTypeName(elementType);
        for(int i=0; i<nl.getLength(); i++){
            Element eleTemp = (Element)nl.item(i);
            String value = eleTemp.getAttribute(VALUE_ATTRIBUTE);
            managedArray.add(value);
        }
        return managedArray;
    }

    private Object parseListElement(Element ele, BeanDefinition bd) {
        String elementType = ele.getAttribute(VALUE_TYPE_ATTRIBUTE);
        NodeList nl = ele.getChildNodes();
        ManagedList<Object> managedList = new ManagedList<Object>();
        managedList.setElementTypeName(elementType);
        for(int i=0; i<nl.getLength(); i++){
            Element eleTemp = (Element)nl.item(i);
            String value = eleTemp.getAttribute(VALUE_ATTRIBUTE);
            managedList.add(value);
        }
        return managedList;
    }

    public boolean isDefaultElement(Element ele) {
        String tagName = ele.getTagName().trim().toLowerCase();
        return this.defaultTagNameSet.contains(tagName);
    }

}
