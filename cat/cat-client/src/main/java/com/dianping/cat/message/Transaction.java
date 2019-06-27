package com.dianping.cat.message;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/28
 */
public interface Transaction extends Message {

    Transaction addChild(Message message);

}
