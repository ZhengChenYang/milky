package com.milky.core;

/**
 * Created by 52678 on 2018/4/23.
 */
public interface ObjectFactory<T> {
    T getObject() throws Exception;
}
