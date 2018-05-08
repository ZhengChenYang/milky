package com.milky.bean.factory.xml;

import com.milky.bean.factory.support.TypeConverter;
import com.milky.bean.factory.util.BeanUtils;
import com.milky.bean.factory.util.editor.DataPropertyEditorRegistrar;
import com.milky.core.MicroKernel;
import com.milky.core.ObjectFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * Created by lenovo on 2018/5/1.
 */
public class ManagedSet extends LinkedHashSet<ManagedSet.Entry> implements TypeConverter {
    private String elementTypeName;

    public String getElementTypeName() {
        return elementTypeName;
    }

    public void setElementTypeName(String elementTypeName) {
        this.elementTypeName = elementTypeName;
    }

    public Object convert(MicroKernel microKernel) throws Exception {
        HashSet<Object> map = new HashSet<Object>();
        for(Entry e: this){
            Object newValue = null;
            if(e.value!=null){
                Class targetType = BeanUtils.getClassByName(elementTypeName);
                newValue= DataPropertyEditorRegistrar.convert(targetType, e.value);
            }
            else if(e.ref!=null){
                newValue = microKernel.getBean(e.ref);
            }
            map.add(newValue);
        }
        return map;
    }

    public void addValueEntry(String value){
        Entry entry = new Entry();
        entry.value = value;
        this.add(entry);
    }

    public void addRefEntry(String ref){
        Entry entry = new Entry();
        entry.ref = ref;
        this.add(entry);
    }


    public class Entry {
        public String value;
        public String ref;
    }
}
