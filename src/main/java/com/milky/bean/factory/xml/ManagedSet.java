package com.milky.bean.factory.xml;

import com.milky.bean.factory.support.TypeConverter;

import java.util.LinkedHashSet;

/**
 * Created by lenovo on 2018/5/1.
 */
public class ManagedSet<E> extends LinkedHashSet<E> implements TypeConverter {
    private String elementTypeName;

    public String getElementTypeName() {
        return elementTypeName;
    }

    public void setElementTypeName(String elementTypeName) {
        this.elementTypeName = elementTypeName;
    }

    public Object convert() throws Exception {
        return null;
    }
}
