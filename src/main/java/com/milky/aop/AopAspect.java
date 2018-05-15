package com.milky.aop;

/**
 * Created by 52678 on 2018/5/10.
 */
public class AopAspect {
    private String aspectRef;
    private String expression;
    private String beforeMethod;
    private String afterMethod;
    private String afterThrowingMethod;
    private Object aspectRefObject;

    public String getAspectRef() {
        return aspectRef;
    }

    public void setAspectRef(String aspectRef) {
        this.aspectRef = aspectRef;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getBeforeMethod() {
        return beforeMethod;
    }

    public void setBeforeMethod(String beforeMethod) {
        this.beforeMethod = beforeMethod;
    }

    public String getAfterMethod() {
        return afterMethod;
    }

    public void setAfterMethod(String afterMethod) {
        this.afterMethod = afterMethod;
    }

    public String getAfterThrowingMethod() {
        return afterThrowingMethod;
    }

    public void setAfterThrowingMethod(String afterThrowingMethod) {
        this.afterThrowingMethod = afterThrowingMethod;
    }

    public Object getAspectRefObject() {
        return aspectRefObject;
    }

    public void setAspectRefObject(Object aspectRefObject) {
        this.aspectRefObject = aspectRefObject;
    }
}
