package com.milky.bean.factory.util.editor;

/**
 * Created by 52678 on 2018/5/7.
 */
public class StringEditor implements DataEditorSupport {
    @Override
    public Object edit(String rawValue) {
        return rawValue;
    }
}
