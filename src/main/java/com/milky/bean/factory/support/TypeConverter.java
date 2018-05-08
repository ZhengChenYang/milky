package com.milky.bean.factory.support;

import com.milky.core.MicroKernel;

/**
 * Created by lenovo on 2018/5/1.
 */
public interface TypeConverter {
    Object convert(MicroKernel microKernel) throws Exception;
}
