package com.milky.bean.factory.util.editor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 52678 on 2018/5/7.
 */
public class DataPropertyEditorRegistrar {
    private static Map<Class, DataEditorSupport> registry = new HashMap<Class, DataEditorSupport>();

    static {
        register(char.class, new CharacterEditor());
        register(Character.class, new CharacterEditor());

        register(String.class, new StringEditor());
        register(boolean.class, new BooleanEditor());
        register(Boolean.class, new BooleanEditor());

        register(short.class, new NumberEditor(short.class));
        register(Short.class, new NumberEditor(Short.class));
        register(int.class, new NumberEditor(int.class));
        register(Integer.class, new NumberEditor(Integer.class));
        register(long.class, new NumberEditor(long.class));
        register(Long.class, new NumberEditor(Long.class));
        register(float.class, new NumberEditor(float.class));
        register(Float.class, new NumberEditor(Float.class));
        register(double.class, new NumberEditor(double.class));
        register(Double.class, new NumberEditor(Double.class));
    }

    public static void register(Class targetType, DataEditorSupport support){
        registry.put(targetType, support);
    }

    public static Object convert(Class targetType, String rawValue) throws Exception {
        DataEditorSupport support = registry.get(targetType);
        if(support!=null){
            return support.edit(rawValue);
        }
        else{
            throw new Exception("there is no editor for target type!");
        }
    }
}
