package com.dianping.plexus;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/28
 */
public interface WebSiteComponent {

    String ROLE = WebSiteComponent.class.getName();

    void monitor() throws Exception;
}
