package com.milky;

/**
 * Created by 52678 on 2018/5/15.
 */
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
