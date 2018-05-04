package com.milky.bean.factory.xml;

import com.milky.bean.factory.support.TypeConverter;

import java.util.LinkedHashMap;

/**
 * Created by lenovo on 2018/5/1.
 */
public class ManagedMap<K, V> extends LinkedHashMap<K, V> implements TypeConverter {
    private String keyTypeName;

    private String valueTypeName;

    public String getKeyTypeName() {
        return keyTypeName;
    }

    public void setKeyTypeName(String keyTypeName) {
        this.keyTypeName = keyTypeName;
    }

    public String getValueTypeName() {
        return valueTypeName;
    }

    public void setValueTypeName(String valueTypeName) {
        this.valueTypeName = valueTypeName;
    }

    public Object convert() throws Exception {
        return null;
    }
}
