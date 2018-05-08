package com.milky.bean.factory.util.editor;

/**
 * Created by 52678 on 2018/5/7.
 */
public class BooleanEditor implements DataEditorSupport {

    public static final String VALUE_TRUE = "true";
    public static final String VALUE_FALSE = "false";

    public static final String VALUE_ON = "on";
    public static final String VALUE_OFF = "off";

    public static final String VALUE_YES = "yes";
    public static final String VALUE_NO = "no";

    public static final String VALUE_1 = "1";
    public static final String VALUE_0 = "0";

    @Override
    public Object edit(String rawValue) {
        String input = (rawValue != null ? rawValue.trim() : null);

        if ((VALUE_TRUE.equalsIgnoreCase(input) || VALUE_ON.equalsIgnoreCase(input) ||
                        VALUE_YES.equalsIgnoreCase(input) || VALUE_1.equals(input))) {
            return Boolean.TRUE;
        }
        else if ((VALUE_FALSE.equalsIgnoreCase(input) || VALUE_OFF.equalsIgnoreCase(input) ||
                        VALUE_NO.equalsIgnoreCase(input) || VALUE_0.equals(input))) {
            return Boolean.FALSE;
        }
        else {
            throw new IllegalArgumentException("Invalid boolean value [" + rawValue + "]");
        }
    }
}
