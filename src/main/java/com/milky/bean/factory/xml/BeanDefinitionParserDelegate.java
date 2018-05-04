package com.milky.bean.factory.xml;

/**
 * Created by 52678 on 2018/3/7.
 */
public class BeanDefinitionParserDelegate {

    private ReaderContext readerContext;

    public BeanDefinitionParserDelegate(ReaderContext readerContext){
        this.readerContext = readerContext;
    }
}
