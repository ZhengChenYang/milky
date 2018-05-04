package com.milky.bean.factory.xml;


import com.milky.core.Resource;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 52678 on 2018/3/5.
 */
public class XmlBeanDefinitionReader{

    private DocumentLoader documentLoader = new DocumentLoader();

    private BeanDefinitionDocumentReader documentReader;

    public void loadResource(Resource resource, ReaderContext readerContext) {
        InputStream inputStream = null;
        InputSource inputSource = null;
        try {
            inputStream = resource.getInputStream();
            inputSource = new InputSource(inputStream);
            loadBeanDefinitions(inputSource, readerContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadBeanDefinitions(InputSource inputSource, ReaderContext readerContext){
        Document doc = null;
        try {
            doc = doLoadDocument(inputSource);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        registerBeanDefinitions(doc, readerContext);
    }

    private Document doLoadDocument(InputSource inputSource) throws IOException, SAXException {
        return this.documentLoader.loadDocument(inputSource);
    }

    private void registerBeanDefinitions(Document doc, ReaderContext readerContext){
        XmlBeanDefinitionDocumentReader documentReader = new XmlBeanDefinitionDocumentReader();
        documentReader.registerBeanDefinitions(doc, readerContext);
    }


}
