package com.milky.core;

/**
 * Created by 52678 on 2018/4/5.
 */
public class BeanWrapper {

    private Object wrappedObject;

    public BeanWrapper(Object wrappedObject){
        this.wrappedObject = wrappedObject;
    }

    public Object getWrappedObject() {
        return wrappedObject;
    }

    public void setWrappedObject(Object wrappedObject) {
        this.wrappedObject = wrappedObject;
    }
}
