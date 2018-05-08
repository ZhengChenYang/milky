package com.milky.bean.factory.xml;

import com.milky.bean.factory.support.TypeConverter;
import com.milky.bean.factory.util.BeanUtils;
import com.milky.bean.factory.util.editor.DataPropertyEditorRegistrar;
import com.milky.bean.factory.util.editor.NumberEditor;
import com.milky.core.MicroKernel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/5/1.
 */
public class ManagedList extends ArrayList<ManagedList.Entry> implements TypeConverter {
    private String elementTypeName;

    public String getElementTypeName() {
        return elementTypeName;
    }

    public void setElementTypeName(String elementTypeName) {
        this.elementTypeName = elementTypeName;
    }

    public Object convert(MicroKernel microKernel) throws Exception {
        List<Object> list = new ArrayList<Object>();
        for(ManagedList.Entry e: this){
            if(e.value!=null){
                Class targetType = BeanUtils.getClassByName(elementTypeName);
                Object newValue = DataPropertyEditorRegistrar.convert(targetType, e.value);
                list.add(newValue);
            }
            else if(e.ref!=null){
                Object newValue = microKernel.getBean(e.ref);
                list.add(newValue);
            }
        }
        return list;
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
