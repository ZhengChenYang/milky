package com.milky.core;

import java.lang.reflect.Constructor;
import java.util.Map;

/**
 * Created by 52678 on 2018/3/18.
 */
public class BeanDefinition {
    private Class beanClass;
    private String name;
    private String scope;
    private ConstructorArgumentValues constructorArgumentValues;
    private PropertyValues propertyValues;
    private Map<String, Object> customizedFeatureMap;
    private Constructor cachedConstructor;

    public BeanDefinition(){
        this.propertyValues = new PropertyValues();
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public ConstructorArgumentValues getConstrcutorArgumentValues() {
        return constructorArgumentValues;
    }

    public void setConstructorArgumentValues(ConstructorArgumentValues constructorArgumentValues) {
        this.constructorArgumentValues = constructorArgumentValues;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    public Map<String, Object> getCustomizedFeatureMap() {
        return customizedFeatureMap;
    }

    public void setCustomizedFeatureMap(Map<String, Object> customizedFeatureMap) {
        this.customizedFeatureMap = customizedFeatureMap;
    }

    public ConstructorArgumentValues getConstructorArgumentValues() {
        return constructorArgumentValues;
    }

    public Constructor getCachedConstructor() {
        return cachedConstructor;
    }

    public void setCachedConstructor(Constructor cachedConstructor) {
        this.cachedConstructor = cachedConstructor;
    }

    public void addProperty(PropertyValue propertyValue){
        this.propertyValues.addPropertyValue(propertyValue);
    }

}
