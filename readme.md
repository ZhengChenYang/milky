

# 快速开始 #
本框架配置方便，使用简单。在使用框架前需要先引入依赖包milky.jar。首先，我们从一个简单的项目开始，逐步演示如何使用本框架。  

## 创建xml文件 ##
xml文件中的根节点必须是<beans>，否则在读取配置的时候会抛出错误。&lt;beans&gt;的子节点指定具体的&lt;bean&gt;节点。&lt;bean&gt;节点中，需要指定id属性（这是必须的），以及class属性，scope属性，这些都是必须的。  
创建一个xml文件
``` xml
<?xml version="1.0" encoding="UTF-8" ?>
<beans>
	<bean id="testBean" class="Test" scope="singleton"></bean>
</beans>
```  

上面这段xml配置，指定了id，class，scope属性。id属性的值必须是容器中唯一的。class属性指定了bean的类类型。scope属性指定了bean的周期，它在容器中是单例的。这样配置的bean会直接调用默认构造函数来实例化bean。  

## 配置构造函数 ##
选择使用的构造函数，我们需要对构造函数的参数进行配置。在&lt;bean&gt;的子节点进行配置。&lt;constructor-arg&gt;节点代表一个参数。
``` xml
<?xml version="1.0" encoding="UTF-8" ?>
<beans>
    <bean id="testBean" class="Test" scope="singleton">
        <constructor-arg index="1" type="char" name="a" value="abc" />
        <constructor-arg index="2" type="int" name="b" value="2" />
        <constructor-arg index="3" type="int" name="c" value="3" />
	</bean>
</beans>
```

在上面配置中，index属性指定参数在参数列表中的序列(从0开始)，type属性指定参数的类型，name属性指定参数名，value属性指定属性值。接下来我们读取配置，并且输出这个bean的属性看看。
``` java
File file = new File("C:\\Users\\52678\\Desktop\\file\\project\\milky\\src\\main\\resources\\test.xml");
FileSystemResource fsr = new FileSystemResource(file);
XmlBeanFactory factory = new XmlBeanFactory();
factory.loadResource(fsr);
Test testA = (Test) factory.getBean("testBean");
System.out.println(testA.toString());
```

这是以上代码运行的输出
```
Test{a=a, b=2, c=3, s=false, str=null}
```  

可以看到上面的代码容器包装类XmlBeanFactory加载了这个配置，并通过getBean()方法来获得指定的bean的实例。输出的结果可以看到，a,b,c三个属性都通过构造器得到了注入。  

## 属性setter注入 ##
依赖注入除了通过构造器的方式注入，也可以通过属性的setter方法注入。同样我们需要对xml进行配置。
``` xml
<?xml version="1.0" encoding="UTF-8" ?>
<beans>
    <bean id="testBean" class="Test" scope="singleton">
        <constructor-arg index="1" type="char" name="a" value="abc" />
        <constructor-arg index="2" type="int" name="b" value="2" />
        <constructor-arg index="3" type="int" name="c" value="3" />
		<property name="s" value="true" />
	</bean>
</beans>
```

&lt;property&gt;标签用来指定需要注入的属性，标签中的name属性指定了注入属性名，value指定了注入的值，ref指定了注入的引用对象。需要注意的时，value属性和ref属性不能同时使用，即一次只能选择一个属性进行注入。修改了xml后，我们重新运行之前的代码，来看看id为testBean的bean发生了怎么样的变化。  

这是以上代码运行的输出
```
Test{a=a, b=2, c=3, s=true, str=null}
```  

我们可以看到，属性名为s的属性值不再是初始的false，而是通过依赖注入修改为了true。  

## 配置AOP ##
用xml配置切面
``` xml
<!-- 切面bean -->
<bean id="transactionPointCut" class="PointCut" />

<!-- aop配置 -->
<aop-config>
    <!-- 定义相关的切面 -->
    <aop-aspect ref="transactionPointCut">
        <!-- 定义切入点 -->
        <aop-pointcut expression=" aopTest[0-9]" id="transactionPointCut"/>
        <aop-before pointcut-ref="transactionPointCut" method="beginTransaction"/>
        <aop-after pointcut-ref="transactionPointCut" method="commitTransaction"/>
        <aop-after-throwing pointcut-ref="transactionPointCut" method="rollBackTransaction"/>
    </aop-aspect>
</aop-config>
```

Test类部分代码
``` java
// Test.java
public void aopTest0(){
    System.out.println("this is aop test");
}

public void aopTest1(){
    System.out.println("this is aop test2");
}
``` 

PoinCut类代码
``` java
//PoinCut.java
public class PointCut {
    public void beginTransaction(){
        System.out.println("--- begin transaction ---");
    }

    public void commitTransaction(){
        System.out.println("--- commit transaction ---");
    }

    public void rollBackTransaction(){
        System.out.println("--- rollback transaction ---");
    }
}
``` 

切入点的定义使用&lt;aop-pointcut&gt;节点来定义，其中的属性expression必须是正则表达式。只有类中的方法可以被增强，并且可以进行前置增强和后置增强，以及方法抛出错误后的处理。正则表达式需要指定类名+空格+方法名的方式来匹配。  
接着我们运行代码，看下方法的增强效果。
```java
Test testA = (Test) factory.getBean("testBean");
testA.aopTest0();
testA.aopTest1();
```  

下面是输出  
```
--- begin transaction ---  
this is aop test  
--- commit transaction ---
  
--- begin transaction ---  
this is aop test2  
--- commit transaction ---  
```


# 扩展接口 #

## 注册bean ##
MicroKernel中有注册bean配置的接口registerBeanDefinition(String beanName, Beandefinition beanDefinition)。实际上，xml解析时正是对标签进行解析，然后将元数据填充到BeanDefinition中。当然，我们可以从任何数据格式上进行解析，然后根据自定义的逻辑填充到BeanDefinition。bean的注册接口就显得额外重要。我们来看下BeanDefinition的实例代码。

```java
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
```

## xml自定义标签 ##
xml解析器开放了对自定义标签解析的接口。自定义标签的解析器需要实现XMlTagParse接口，然后将实例化的对象注册到xml解析器中。

``` java
// AopXmlTagParser.java
public class AopXmlTagParser implements XmlTagParser {
	public void parse(Element ele, BeanDefinition bd) {
	        try {
	            parseAopAspectElements(ele, bd);
	            bd.setBeanClass(AopPostFactoryProcessor.class);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	//...
}

// 注册自定义标签解析器
factory.registerCustomTagParser("aop-config", new AopXmlTagParser());
```

自定义标签解析器注册时，需要传入标签的名字，这是唯一指定的。


## MicroKernelPostProcess接口 ##
MicroKernelPostProcess主要用于短路注册bean的逻辑。如果beanDefinition中的beanClass属性是MicroKernelPostProcessor的子类，容器不会将该beanDefinition进行注册，而是通过反射，实例化该类。然后调用接口中的postProcess()方法执行特殊的逻辑。AOP正式通过这种方式，在容器中注册了BeanDefinitionPostProcess。  

``` java
public class AopPostFactoryProcessor implements MicroKernelPostProcessor{

    private static final String AOP_KEY = "aop";
    @Override
    public void postProcess(BeanDefinition bd, MicroKernel microKernel) {
        AopBeanDefinitionPostProcessor beanDefinitionPostProcessor = new AopBeanDefinitionPostProcessor();
        Map<String, Object> customizedFeatureMap = bd.getCustomizedFeatureMap();
        Object value = customizedFeatureMap.get(AOP_KEY);
        beanDefinitionPostProcessor.setAopAspect((AopAspect)value);
        microKernel.registerBeanDefinitionPostProcessor(beanDefinitionPostProcessor);
    }
}
```

## BeanDefinitionPostProcess接口 ##
BeanDefinitionPostProcess主要用于实例化过程中的判断。在返回值非null的情况下，它会跳过容器内部的实例化，从而使用BeandefinitionPostProcess返回的实例对象。它可以用于短路实例化过程，从而使用自定义的方式进行实例化。在AOP模块中，正是向
容器中注册了一个BeanDefinitionPostProcess，从而使其短路了默认的bean实例化过程，而使用CGLib进行实例化。以下是BeanDefinitionPostProcess的一个实现类

``` java
public class AopBeanDefinitionPostProcessor implements BeanDefinitionPostProcessor {
	private AopAspect aopAspect;

	@Override
	public Object postProcess(Constructor constructor, Object[] params, BeanDefinition bd, MicroKernel microKernel) throws Exception {
		// ...
			
		// 注意，不是返回null,从而短路了默认的实例化过程
		return bean;
	}
}
```


## 类型转换 ##
DataPropertyEditorRegistrar是一个管理类型转换的类。默认情况下，DataPropertyEditorRegistrar提供了15种类型的转换。他们将字符串按照一定的逻辑转换为对应类对象。正是这个DataPropertyEditorRegistrar类的存在，BeanDefinition中的元数据可以通过DataPropertyEditorRegistrar转换为对应的对象，从而实现了属性的注入。DataPropertyEditorRegistrar开放了注册口，用户可以自定义转换类型。所有的类型转换类必须实现DataEditorSupport接口。接下来，我们实现一个简单的String转换为Date的例子。

``` java
public class DatePropertyEditor extends DataEditorSupport{
	private String format = "yyyy-MM-dd";
	public Object edit(String rawValue){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date d = null;
		try{
			 d = sdf.parse(raw); 					
		}catch(ParseException e){
			e.printStackTrace();
		}
		return d;
	}
}
```

上面演示了如何创建一个类型转换的类。接着，我们把这个类进行注册。  

``` java
DataPropertyEditorRegistrar.register(Date.class, new DatePropertyEditor());
```

注册完后，容器就具备了将String转换为Date类型的能力了。


## TypeConverter接口 ##
TypeConverter接口是一个有趣的接口。当beanDefinition中的初始值不是String时，它不会调用DataPropertyEditorRegistrar进行转换，而是采用了TypeConverter接口中的方法convert()。也就是说，用户可以自定义复杂的转换方式。单纯的String有时候难以表示复杂的类，因此，用于自定义如何转换复杂的类就显得尤为必要实现TypeConverter接口的类会调用convert()方法或者最终转换后的对象。容器中提供的array,list,map,set等集合类由于表示复杂，不易用String表示，因此会被解析成TypeConverter接口的实现类来保存元数据，最终通过该实现类返回转换后的对象。

``` java
public class ManagedList extends ArrayList<ManagedList.Entry> implements TypeConverter {
	private String elementTypeName;

	public Object convert(MicroKernel microKernel) throws Exception {
		List<Object> list = new ArrayList<Object>();
		//...
		return list;
	}

	//...
}
```
以上就是一个实现TypeConverter接口的类，xml解析器在读取XML配置并完成解析后，会将元数据存储在该类中，最后在容器中，通过调用convert方法正式转换成List类的对象。  

