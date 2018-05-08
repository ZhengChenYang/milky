package com.milky.bean.factory.xml.tag;

import com.milky.core.BeanDefinition;
import org.w3c.dom.Element;

/**
 * Created by 52678 on 2018/5/5.
 */
public interface XmlTagParser {
    void parse(Element ele, BeanDefinition bd);
    String getTagName();
}
