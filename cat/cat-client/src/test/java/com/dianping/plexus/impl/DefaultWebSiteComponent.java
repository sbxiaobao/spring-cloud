package com.dianping.plexus.impl;

import com.dianping.plexus.WebSiteComponent;

import java.util.List;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/28
 */
public class DefaultWebSiteComponent implements WebSiteComponent {

    /**
     * @plexus.configuration
     */
    private List<String> websites;

    @Override
    public void monitor() throws Exception {
        if (websites != null) {
            for (String webstite : websites) {
                System.out.println(webstite);
            }
        }
    }


    public void addWebSites(List<String> websites) {
        this.websites = websites;
    }
}
