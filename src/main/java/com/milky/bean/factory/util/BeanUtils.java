package com.milky.bean.factory.util;

import com.milky.bean.factory.support.TypeConverter;
import com.milky.bean.factory.util.editor.DataPropertyEditorRegistrar;
import com.milky.bean.factory.util.editor.NumberEditor;
import com.milky.core.BeanDefinition;
import com.milky.core.ConstructorArgumentValues;
import com.milky.core.MicroKernel;
import com.milky.core.PropertyValue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by 52678 on 2018/3/31.
 * @since 1.7+
 */

public class BeanUtils {


    public static Constructor getAppropriateConstructor(BeanDefinition bd) throws NoSuchMethodException {
        Constructor constrToUse = null;
        Class clazz = bd.getBeanClass();

        int nrOfArgument = 0;
        if(bd.getConstructorArgumentValues()!=null){
            nrOfArgument = bd.getConstructorArgumentValues().getNrOfArguments();
        }

        if(nrOfArgument==0){
            return clazz.getDeclaredConstructor();
        }
        else{
            Constructor[] candidates = clazz.getDeclaredConstructors();
            Arrays.sort(candidates, new ConstructorComparator());
            for(int i=0; i<candidates.length; i++){
                if(candidates[i].getParameterCount()<nrOfArgument){
                    continue;
                }
                else if(candidates[i].getParameterCount()==nrOfArgument){
                    if(isMatchConstructorParameter(candidates[i], bd)){
                        constrToUse = candidates[i];
                    }
                }
                else{
                    break;
                }
            }

//            bd.getConstrcutorArgumentValues().
        }
        return constrToUse;
    }

    private static boolean isMatchConstructorParameter(Constructor candidate, BeanDefinition bd){

        //whether this candidate matches the bean definition
        Map<Integer, ConstructorArgumentValues.ValueHolder> valueHolderMap = bd.getConstructorArgumentValues().getIndexedArgumentValues();
        Class[] classes = candidate.getParameterTypes();
        for(int i=0; i<classes.length; i++){
            ConstructorArgumentValues.ValueHolder vh = valueHolderMap.get(i);
            String className = classes[i].getName();
            if(className.equals(vh.getType()) || (classes[i].isArray() && vh.getType().equals("array")) ||
                    (classes[i].isInstance(List.class) && vh.getType().equals("list")) ||
                    (classes[i].isInstance(Map.class) && vh.getType().equals("map")) ||
                    (classes[i].isInstance(Set.class) && vh.getType().equals("set")) ){
                continue;
            }
            else{
                return false;
            }
        }
        return true;
    }

    public static Object executeConversion(ConstructorArgumentValues.ValueHolder valueHolder, MicroKernel kernel) throws Exception {

        String targetType = valueHolder.getType();
        targetType = targetType.trim().toLowerCase();
        String ref = valueHolder.getRef();


        if(!valueHolder.isConverted()){

            Object rawValue = valueHolder.getValue();

            if (rawValue!=null) {
                if(rawValue instanceof TypeConverter){  //deal with the object inherit the TypeConverter
                    Object newValue = ((TypeConverter)rawValue).convert(kernel);
                    valueHolder.setValue(newValue);
                    valueHolder.setConverted(true);
                }
                else if(rawValue instanceof String){   // deal with the string object
                    Class className = BeanUtils.getClassByName(targetType);
                    Object newValue = DataPropertyEditorRegistrar.convert(className, (String)rawValue);
                    valueHolder.setValue(newValue);
                    valueHolder.setConverted(true);
                }
                else{
                    throw new Exception("error primitive type name!");
                } 

            }

            //deal with the reference type
            if(ref != null){
                Object newValue = kernel.getBean(ref);
                valueHolder.setValue(newValue);
                valueHolder.setConverted(true);
            }

        }

        return valueHolder.getValue();
    }

    public static Object executeConversion(PropertyValue propertyValue, MicroKernel kernel, String targetType) throws Exception {

        String ref = propertyValue.getRef();

        if(!propertyValue.isConverted()){
            Object rawValue = propertyValue.getValue();
            if (rawValue!=null) {
                if(rawValue instanceof TypeConverter){  //deal with the object inherit the TypeConverter
                    Object newValue = ((TypeConverter)rawValue).convert(kernel);
                    propertyValue.setValue(newValue);
                    propertyValue.setConverted(true);
                }
                else if(rawValue instanceof String){  // deal with the string object
                    Class className = BeanUtils.getClassByName(targetType);
                    Object newValue = DataPropertyEditorRegistrar.convert(className, (String)rawValue);
                    propertyValue.setValue(newValue);
                    propertyValue.setConverted(true);
                }
            }

            if(ref!=null){
                Object newValue = kernel.getBean(ref);
                propertyValue.setValue(newValue);
                propertyValue.setConverted(true);
            }
        }

        return propertyValue.getValue();
    }

    public static Map<String,String> getFieldsType(Field[] fields) {
        Map<String,String> fieldsType = new HashMap<String, String>();
        for(int i = 0; i < fields.length; i++){
            String name = fields[i].getName();
            String type = fields[i].getType().toString().trim();
            fieldsType.put(name, type);
        }
        return fieldsType;
    }

    public static Class getClassByName(String className) {
        if (PrimitiveTypeConversion.isPrimitive(className)) {
            return PrimitiveTypeConversion.getPrimitiveClass(className);
        }
        Class clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }
    public static Boolean isBlank(String str){
        if(str==null || str.trim().equals("")){
            return true;
        }
        return false;
    }


}
