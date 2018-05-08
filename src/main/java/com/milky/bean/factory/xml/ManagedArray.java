package com.milky.bean.factory.xml;


import com.milky.bean.factory.support.TypeConverter;
import com.milky.bean.factory.util.BeanUtils;
import com.milky.bean.factory.util.editor.DataPropertyEditorRegistrar;
import com.milky.bean.factory.util.editor.NumberEditor;
import com.milky.core.MicroKernel;

import javax.xml.crypto.Data;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by lenovo on 2018/5/1.
 */
public class ManagedArray extends ArrayList<ManagedArray.Entry> implements TypeConverter {

    private String elementTypeName = String.class.getName();

    public String getElementTypeName() {
        return elementTypeName;
    }

    public void setElementTypeName(String elementTypeName) {
        this.elementTypeName = elementTypeName;
    }

    public Object convert(MicroKernel microKernel) throws Exception {

        int index = 0;
        Class targetType = BeanUtils.getClassByName(elementTypeName);
        Object array = Array.newInstance(targetType, this.size());
        for(Entry e: this){
            if(e.value!=null){
                Object newValue = DataPropertyEditorRegistrar.convert(targetType, e.value);
                Array.set(array, index++, newValue);
            }
            else if(e.ref!=null){
                Object newValue = microKernel.getBean(e.ref);
                Array.set(array, index++, newValue);
            }
        }
        return array;
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

    public class Entry{
        public String value;
        public String ref;
    }
}
