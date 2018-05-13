import com.milky.Test;

import com.milky.bean.factory.XmlBeanFactory;
import com.milky.core.*;


import java.io.File;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

/**
 * Created by 52678 on 2018/3/6.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        test2();

    }

    private static void test1(){
        char[] s;
        BeanDefinition beanDefinition = new BeanDefinition();
        // bean name
        beanDefinition.setName("firstBean");
        beanDefinition.setScope("singleton");
        beanDefinition.setBeanClass(Test.class);

        // bean constructor
        int offset = 1;
        int count = 3;
        ConstructorArgumentValues cav = new ConstructorArgumentValues();
        cav.addArgumentValue(0,
                new ConstructorArgumentValues.ValueHolder(
                        "a",
                        char.class.getName(),
                        null)
        );
        cav.addArgumentValue(1,
                new ConstructorArgumentValues.ValueHolder(
                        "1",
                        int.class.getName(),
                        null
                )
        );
        cav.addArgumentValue(2, new ConstructorArgumentValues.ValueHolder(
                "3",
                int.class.getName(),
                null)
        );
        beanDefinition.setConstructorArgumentValues(cav);


        // set attribute
        PropertyValue propertyValue = new PropertyValue();
        propertyValue.setName("s");
        propertyValue.setValue("True");
        beanDefinition.addProperty(propertyValue);

        // register the bean definition to the MicroKernel
        MicroKernel microKernel = new MicroKernel();
        try {
            microKernel.registerBeanDefinition("firstBean", beanDefinition);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("cannot register the bean definition");
        }


        // test the function of getBean
        try {
            System.out.println(microKernel.getBean("firstBean"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void test2() throws Exception {
//        File file = new File("C:\\Users\\52678\\Desktop\\file\\project\\milky\\src\\main\\resources\\test.xml");
//        FileSystemResource fsr = new FileSystemResource(file);
//        XmlBeanFactory factory = new XmlBeanFactory();
//        factory.loadResource(fsr);
//
//        System.out.println(factory.getBean("testBean"));
//        Map<String, Object> map = factory.getSingletonObjects();
//        System.out.println(map);
        Method[] methods = String.class.getMethods();
        for(int i=0; i<methods.length; i++){
            System.out.println(methods[i].getName());
        }
    }

}
