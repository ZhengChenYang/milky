package com.milky.core;

import com.milky.bean.factory.support.BeanDefinitionRegistry;
import com.milky.bean.factory.util.BeanUtils;
import com.milky.bean.factory.util.PrimitiveTypeConversion;
import com.milky.bean.factory.xml.ReaderContext;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 52678 on 2018/3/19.
 */
public class MicroKernel implements BeanDefinitionRegistry {


    private static Logger LOGGER = LogManager.getLogger(MicroKernel.class);

    /** Constant for the bean scope which is singleton */
    private final String SINGLETON_SCOPE = "singleton";

    /** Constant for the bean scope which is prototype */
    private final String PROTOTYPE_SCOPE = "prototype";

    /** Map of bean definition objects */
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>(256);

    /** List of bean definition names, in registration order */
    private final List<String> beanDefinitionNames = new ArrayList<String>(256);

    /** Config object for kernel */
    private MicroKernelConfig config;

    /** List of Loaded components */
    private List<Component> componentList = new ArrayList<Component>(64);

    /** cache of singleton objects: bean name --> bean instance */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>();

    /** cache of Class: class name --> Class instance */
    private final Map<String, Class> cachedClasses = new ConcurrentHashMap<String, Class>();

    /** Names of beans that are currently in creation **/
    private final Set<String> singletonsCurrentlyInCreation =
            Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>(16));

    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<String, ObjectFactory<?>>(16);

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws Exception {

        BeanDefinition oldBeanDefinition = beanDefinitionMap.get(beanName);

        if(oldBeanDefinition!=null){
            if(!isAllowBeanDefinitionOverriding()){
                throw new Exception("Cannot register bean definition, there is already " +
                        "["+oldBeanDefinition+"] bound");
            }
            executeComponentsInRegistry(beanName, beanDefinition);
            this.beanDefinitionMap.put(beanName, beanDefinition);
        }
        else{
            executeComponentsInRegistry(beanName, beanDefinition);
            this.beanDefinitionMap.put(beanName, beanDefinition);
            this.beanDefinitionNames.add(beanName);
        }
    }


    private void executeComponentsInRegistry(String beanName, BeanDefinition beanDefinition){
        for(Component component: componentList){
            component.doInRegistry(beanName, beanDefinition);


        }
    }

    @Override
    public void removeBeanDefinition(String beanName) {

    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return null;
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return false;
    }


    public boolean isAllowBeanDefinitionOverriding(){
        return config.isAllowBeanDefinitionOverriding();
    }

    public Object getBean(String beanName) throws Exception {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if(beanDefinition==null){
            throw new Exception("there is no bean definition named ["+beanName+"]");
        }
        Object singletonInstance = getSingleton(beanName);
        if(singletonInstance==null && isSingletonCurrentlyInCreation(beanName)){
            throw new Exception("there is a circular reference!");
        }

        final BeanWrapper beanWrapper = new BeanWrapper(singletonInstance);
        String scope = beanDefinition.getScope();
        if(scope.equals(SINGLETON_SCOPE)){

            if(singletonInstance!=null){
                return singletonInstance;
            }

            //before creation
            beforeSingletonCreation(beanName);

            Constructor constructor = beanDefinition.getCachedConstructor();
            if(constructor==null){
                constructor = BeanUtils.getAppropriateConstructor(beanDefinition);
                beanDefinition.setCachedConstructor(constructor);
            }

            //instantiate the bean
            instantiateBean(constructor, beanDefinition, beanWrapper);

            addSingletonFactory(beanName, new ObjectFactory(){

                @Override
                public Object getObject() throws Exception {
                    return beanWrapper.getWrappedObject();
                }
            });

            // populate the bean
            populateBean(beanDefinition, beanWrapper);

            //after creation
            afterSingletonCreation(beanName);

            System.out.println(beanWrapper.getWrappedObject());

        }
        else if(scope.equals(PROTOTYPE_SCOPE)){


        }

        return null;

    }

    private void populateBean(BeanDefinition beanDefinition, BeanWrapper beanWrapper){

        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        if(propertyValues.isEmpty()){
            return ;
        }
        Class clazz = beanDefinition.getBeanClass();
        Field[] fields = clazz.getDeclaredFields();
        Map<String, String> fieldsType = BeanUtils.getFieldsType(fields);

        // populate the bean
        List<PropertyValue> propertyValueList = propertyValues.getPropertyValueList();

        for(PropertyValue propertyValue: propertyValueList){
            // conversion of the primitive type attributes
            String fieldType = fieldsType.get(propertyValue.getName());
            if(!propertyValue.isConverted()){
                try {
                    BeanUtils.executeConversion(propertyValue, this, fieldType);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Object fieldValue = propertyValue.getValue();

            // find the set method
            Method method = null;
            try {
                method = getSetMethod(clazz, propertyValue.getName(), fieldType);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            // inject the value into the field
            try {
                method.invoke(beanWrapper.getWrappedObject(),fieldValue);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
    }

    private Method getSetMethod(Class clazz, String fieldName, String fieldType) throws NoSuchMethodException {
        fieldName = fieldName.trim();
        char firstChar = Character.toUpperCase(fieldName.charAt(0));
        String methodName = "set" + firstChar + fieldName.substring(1);
        Class fieldClass = PrimitiveTypeConversion.getPrimitiveClass(fieldType);
        return clazz.getMethod(methodName, fieldClass);
    }

    public Object getSingleton(String beanName) {
        Object singletonInstance = this.singletonObjects.get(beanName);
        if(singletonInstance==null){
            singletonInstance = getSingletonFactory(beanName);
        }
        return singletonInstance;
    }

    private void instantiateBean(Constructor constructor, BeanDefinition bd, BeanWrapper beanWrapper) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Map<Integer, ConstructorArgumentValues.ValueHolder> map = bd.getConstructorArgumentValues().getIndexedArgumentValues();
        if(map.size()==0){
            constructor.newInstance();
        }
        else{
            // assemble the parameters
            Class[] argsTypes = constructor.getParameterTypes();
            Object[] params = new Object[map.size()];
            for(int i=0; i<map.size(); i++){
                ConstructorArgumentValues.ValueHolder valueHolder = map.get(i);
                if(valueHolder.isConverted()){
                    // execute the conversion
                    params[i] = valueHolder.getValue();
                }
                else{
                    try {
                        params[i] = BeanUtils.executeConversion(valueHolder, this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


            Object newInstance = constructor.newInstance(params);
            LOGGER.info(newInstance + "实例创建成功！");

            //
            beanWrapper.setWrappedObject(newInstance);
        }
    }

    public Map<String, Class> getCachedClasses() {
        return cachedClasses;
    }

    public boolean isSingletonCurrentlyInCreation(String beanName) {
        return this.singletonsCurrentlyInCreation.contains(beanName);
    }

    public void beforeSingletonCreation(String beanName){
        this.singletonsCurrentlyInCreation.add(beanName);
    }

    public void afterSingletonCreation(String beanName){
        this.singletonsCurrentlyInCreation.remove(beanName);
    }

    public void addSingletonFactory(String beanName, ObjectFactory objectFactory){
        this.singletonFactories.put(beanName, objectFactory);
    }

    public Class getCachedClass(String className){
        if(this.cachedClasses.containsKey(className)){
            return this.cachedClasses.get(className);
        }
        else{
            Class clazz = null;
            try {
                clazz = Class.forName(className);
                this.cachedClasses.put(className, clazz);
                return clazz;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ObjectFactory getSingletonFactory(String factoryName){
        return this.singletonFactories.get(factoryName);
    }
}
