

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;


/**
 * Created by 52678 on 2018/4/10.
 */
public class Test {

    public char a;
    public int b;
    public int c;
    private boolean s;
    public char[] str;
    private Test test;

    public Test(){}

    public Test(Test test){
        this.test = test;
    }

    public Test(char a, int b, int c){
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Test(char a, int b, int c, char[] str){
        this.a = a;
        this.b = b;
        this.c = c;
        this.str = str;
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
                ", str=" + Arrays.toString(str) +
                '}';
    }

    public static void main(String[] args) throws NoSuchMethodException {
        Field[] field = String.class.getDeclaredFields();
        for(Field f:field){
            System.out.println(f.getName());
        }
    }


    public void aopTest0(){
        System.out.println("this is aop test");
    }

    public void aopTest1(){
        System.out.println("this is aop test2");
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }
}
