package com.milky.core.exception;

/**
 * Created by 52678 on 2018/5/20.
 */
public class BeanCurrentlyInCreationException extends Exception {
    public BeanCurrentlyInCreationException(String beanName){
        super("there is a circular reference for ["+beanName+"]!");
    }

}
