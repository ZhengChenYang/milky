import com.milky.Test;
import com.milky.bean.factory.BeanFactory;
import com.milky.bean.factory.util.ConstructorComparator;
import com.milky.bean.factory.xml.XmlBeanDefinitionReader;
import com.milky.core.*;
import org.springframework.beans.factory.xml.XmlBeanFactory;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by 52678 on 2018/3/6.
 */
public class Main {

    public static void main(String[] args) throws NoSuchMethodException, ClassNotFoundException {

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
}
