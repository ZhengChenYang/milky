package com.milky.bean.factory.xml;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.print.Doc;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by 52678 on 2018/3/6.
 */
public class DocumentLoader {
    public Document loadDocument(InputSource inputSource) throws IOException, SAXException {
        DocumentBuilderFactory factory = createDocumentBuilderFactory();
        DocumentBuilder builder = createDocumentBuilder(factory);
        return builder.parse(inputSource);
    }

    private DocumentBuilderFactory createDocumentBuilderFactory(){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        return factory;
    }

    private DocumentBuilder createDocumentBuilder(DocumentBuilderFactory factory){
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return builder;
    }
}
