package com.milky.bean.factory.util;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.CompareGenerator;

import java.lang.reflect.Constructor;
import java.util.Comparator;

/**
 * Created by 52678 on 2018/3/31.
 */
public class ConstructorComparator implements Comparator<Constructor> {

    @Override
    public int compare(Constructor o1, Constructor o2) {
        if(o1.getParameterCount()>o2.getParameterCount()){
            return 1;
        }
        else if(o1.getParameterCount()<o2.getParameterCount()){
            return -1;
        }
        else{
            return 0;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
