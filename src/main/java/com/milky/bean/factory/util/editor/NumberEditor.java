package com.milky.bean.factory.util.editor;

/**
 * Created by 52678 on 2018/4/9.
 */
public class NumberEditor implements DataEditorSupport{

    private static final Class SHORT_CLASS = short.class;
    private static final Class SHORT_WRAPPER_CLASS = Short.class;

    private static final Class INT_CLASS = int.class;
    private static final Class INT_WRAPPER_CLASS = Integer.class;

    private static final Class LONG_CLASS = long.class;
    private static final Class LONG_WRAPPER_CLASS = Long.class;

    private static final Class FLOAT_CLASS = float.class;
    private static final Class FLOAT_WRAPPER_CLASS = Float.class;

    private static final Class DOUBLE_CLASS = double.class;
    private static final Class DOUBLE_WRAPPER_CLASS = Double.class;

    private Class targetType;

    public NumberEditor(Class targetType){
        this.targetType = targetType;
    }

    @Override
    public Object edit(String rawValue) {

        if(targetType.equals(SHORT_CLASS)){
            return Short.parseShort(rawValue);
        }
        else if(targetType.equals(SHORT_WRAPPER_CLASS)){
            return new Short(rawValue);
        }
        else if(targetType.equals(INT_CLASS)){
            return Integer.parseInt(rawValue);
        }
        else if(targetType.equals(INT_WRAPPER_CLASS)){
            return new Integer(rawValue);
        }
        else if(targetType.equals(LONG_CLASS)){
            return Long.parseLong(rawValue);
        }
        else if(targetType.equals(LONG_WRAPPER_CLASS)){
            return new Long(rawValue);
        }
        else if(targetType.equals(FLOAT_CLASS)){
            return Float.parseFloat(rawValue);
        }
        else if(targetType.equals(FLOAT_WRAPPER_CLASS)){
            return new Float(rawValue);
        }
        else if(targetType.equals(DOUBLE_CLASS)){
            return Double.parseDouble(rawValue);
        }
        else if(targetType.equals(DOUBLE_WRAPPER_CLASS)){
            return new Double(rawValue);
        }
        else {
            return null;
        }
    }
}
