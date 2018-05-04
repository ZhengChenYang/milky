package com.milky.bean.factory.xml;

import com.milky.bean.factory.FactoryContext;
import com.milky.bean.factory.support.BeanDefinitionReader;
import com.milky.core.Resource;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 52678 on 2018/3/5.
 */
public class XmlBeanDefinitionReader implements BeanDefinitionReader {

    private DocumentLoader documentLoader = new DocumentLoader();

    private BeanDefinitionDocumentReader documentReader;

    @Override
    public void loadResource(Resource resource, FactoryContext context) {
        InputStream inputStream = null;
        InputSource inputSource = null;
        try {
            inputStream = resource.getInputStream();
            inputSource = new InputSource(inputStream);
            loadBeanDefinitions(inputSource, resource);
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

    private void loadBeanDefinitions(InputSource inputSource, Resource resource){
        Document doc = null;
        try {
            doc = doLoadDocument(inputSource);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        registerBeanDefinitions(doc, resource);
    }

    private Document doLoadDocument(InputSource inputSource) throws IOException, SAXException {
        return this.documentLoader.loadDocument(inputSource);
    }

    private void registerBeanDefinitions(Document doc, Resource resource){
        BeanDefinitionDocumentReader documentReader = createDocumentReader();
        documentReader.registerBeanDefinitions(doc, createReaderContext(resource));
    }

    protected BeanDefinitionDocumentReader createDocumentReader(){
        if(this.documentReader!=null){
            return this.documentReader;
        }
        return (BeanDefinitionDocumentReader)new XmlBeanDefinitionDocumentReader();
    }

    protected ReaderContext createReaderContext(Resource resource){
        ReaderContext readerContext = new ReaderContext();
        readerContext.setDefinitionReader(this);
        readerContext.setResource(resource);

        return readerContext;
    }
}
