package com.milky.bean.factory.util;

import com.milky.bean.factory.support.TypeConverter;
import com.milky.core.BeanDefinition;
import com.milky.core.ConstructorArgumentValues;
import com.milky.core.MicroKernel;
import com.milky.core.PropertyValue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

        //whether this candidate matches the bean defintition
        Map<Integer, ConstructorArgumentValues.ValueHolder> valueHolderMap = bd.getConstrcutorArgumentValues().getIndexedArgumentValues();
        Class[] classes = candidate.getParameterTypes();
        for(int i=0; i<classes.length; i++){
            ConstructorArgumentValues.ValueHolder vh = valueHolderMap.get(i);
            String className = classes[i].getName();
            if(!className.equals(vh.getType())){
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
                    Object newValue = ((TypeConverter)rawValue).convert();
                    valueHolder.setValue(newValue);
                    valueHolder.setConverted(true);
                }
                else if(PrimitiveTypeConversion.isPrimitive(targetType)){   // deal with the primitive type
                    Object newValue = PrimitiveTypeConversion.convert((String)rawValue, targetType);
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
                    Object newValue = ((TypeConverter)rawValue).convert();
                    propertyValue.setValue(newValue);
                    propertyValue.setConverted(true);
                }
                else if(PrimitiveTypeConversion.isPrimitive(targetType)) {  // deal with the primitive type
                    Object newValue = PrimitiveTypeConversion.convert((String) rawValue, targetType);
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
}
