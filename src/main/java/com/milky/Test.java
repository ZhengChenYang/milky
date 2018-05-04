package com.milky;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;


/**
 * Created by 52678 on 2018/4/10.
 */
public class Test {

    public char a;
    public int b;
    public int c;
    private boolean s;
    public Test(char a, int b, int c){
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public boolean isS() {
        return s;
    }

    public void setS(boolean s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return "Test{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", s=" + s +
                '}';
    }

    public static void main(String[] args) throws NoSuchMethodException {
        Field[] field = String.class.getDeclaredFields();
        for(Field f:field){
            System.out.println(f.getName());
        }





    }
}
