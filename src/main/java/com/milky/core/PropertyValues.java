package com.milky.core;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 52678 on 2018/3/18.
 */
public class PropertyValues {
    private List<PropertyValue> propertyValueList;

    public PropertyValues(){
        this.propertyValueList = new LinkedList<PropertyValue>();
    }

    public boolean isEmpty(){
        return propertyValueList.isEmpty();
    }

    public List<PropertyValue> getPropertyValueList() {
        return Collections.unmodifiableList(propertyValueList);
    }

    public void setPropertyValueList(List<PropertyValue> propertyValueList) {
        this.propertyValueList = propertyValueList;
    }

    public void addPropertyValue(PropertyValue propertyValue){
        this.propertyValueList.add(propertyValue);
    }

}
