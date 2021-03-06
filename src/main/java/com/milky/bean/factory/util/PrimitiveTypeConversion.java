package com.milky.bean.factory.util;

/**
 * Created by 52678 on 2018/5/7.
 */
public class PrimitiveTypeConversion {

    private static final String BOOLEAN_TYPE = "boolean";

    private static final String CHAR_TYPE = "char";


    private static final String BYTE_TYPE = "byte";

    private static final String SHORT_TYPE = "short";

    private static final String INT_TYPE = "int";

    private static final String LONG_TYPE = "long";


    private static final String FLOAT_TYPE = "float";

    private static final String DOUBLE_TYPE = "double";


    private static final String VOID_TYPE = "void";


    private static final Class BOOLEAN_CLASS = boolean.class;

    private static final Class CHAR_CLASS = char.class;



    private static final Class BYTE_CLASS = byte.class;

    private static final Class SHORT_CLASS = short.class;

    private static final Class INT_CLASS = int.class;

    private static final Class LONG_CLASS = long.class;



    private static final Class FLOAT_CLASS = float.class;

    private static final Class DOUBLE_CLASS = double.class;

    public static boolean isPrimitive(String type) {

        if (type.equals(BOOLEAN_TYPE)) {
            return true;
        } else if (type.equals(CHAR_TYPE)) {
            return true;
        } else if (type.equals(BYTE_TYPE)) {
            return true;
        } else if (type.equals(SHORT_TYPE)) {
            return true;
        } else if (type.equals(INT_TYPE)) {
            return true;
        } else if (type.equals(LONG_TYPE)) {
            return true;
        } else if (type.equals(FLOAT_TYPE)) {
            return true;
        } else if (type.equals(DOUBLE_TYPE)) {
            return true;
        } else if (type.equals(VOID_TYPE)) {
            return true;
        } else
            return false;

    }

    public static Class getPrimitiveClass(String fieldType){

        if(fieldType.equals(BOOLEAN_TYPE)){
            return BOOLEAN_CLASS;
        }
        else if(fieldType.equals(CHAR_TYPE)){
            return CHAR_CLASS;
        }
        else if(fieldType.equals(BYTE_TYPE)){
            return BYTE_CLASS;
        }
        else if(fieldType.equals(SHORT_TYPE)){
            return SHORT_CLASS;
        }
        else if(fieldType.equals(INT_TYPE)){
            return INT_CLASS;
        }
        else if(fieldType.equals(LONG_TYPE)){
            return LONG_CLASS;
        }
        else if(fieldType.equals(FLOAT_TYPE)){
            return FLOAT_CLASS;
        }
        else if(fieldType.equals(DOUBLE_TYPE)){
            return DOUBLE_CLASS;
        }
        else {
            return null;
        }
    }
}
