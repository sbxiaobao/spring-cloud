package com.netease.javassist;

import javassist.ClassPool;
import javassist.CtClass;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/26
 */
public class Demo {

    public static void main(String[] args) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("test.Rectangle");
        cc.setSuperclass(pool.get("test.Point"));
        cc.writeFile();
    }
}