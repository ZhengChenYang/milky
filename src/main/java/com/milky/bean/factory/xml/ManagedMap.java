package com.milky.bean.factory.xml;

import com.milky.bean.factory.support.TypeConverter;
import com.milky.bean.factory.util.BeanUtils;
import com.milky.bean.factory.util.editor.DataPropertyEditorRegistrar;
import com.milky.core.MicroKernel;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lenovo on 2018/5/1.
 */
public class ManagedMap extends LinkedHashMap<ManagedMap.KeyEntry, ManagedMap.ValueEntry> implements TypeConverter {
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

    public Object convert(MicroKernel microKernel) throws Exception {
        HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        for(Map.Entry<KeyEntry, ValueEntry> entry: this.entrySet()){
            KeyEntry key = entry.getKey();
            ValueEntry value = entry.getValue();
            Object newKey = null;
            if(key.value!=null){
                Class targetType = BeanUtils.getClassByName(keyTypeName);
                newKey = DataPropertyEditorRegistrar.convert(targetType, key.value);
            }


            Object newValue = null;
            if(value.value!=null){
                Class targetType = BeanUtils.getClassByName(valueTypeName);
                newValue = DataPropertyEditorRegistrar.convert(targetType, value.value);
            }
            else if(value.ref!=null){
                newValue = microKernel.getBean(value.ref);
            }

            hashMap.put(newKey, newValue);
        }
        return hashMap;
    }


    public void addValueEntry(String key, String value){
        KeyEntry keyEntry = new KeyEntry();
        keyEntry.value = key;

        ValueEntry valueEntry = new ValueEntry();
        valueEntry.value = value;

        this.put(keyEntry, valueEntry);
    }

    public void addRefEntry(String key, String ref){
        KeyEntry keyEntry = new KeyEntry();
        keyEntry.value = key;

        ValueEntry valueEntry = new ValueEntry();
        valueEntry.ref = ref;

        this.put(keyEntry, valueEntry);
    }

    public class KeyEntry{
        public String value;
    }

    public class ValueEntry{
        public String value;
        public String ref;
    }
}
