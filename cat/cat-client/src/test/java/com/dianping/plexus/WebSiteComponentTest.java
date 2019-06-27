package com.dianping.plexus;

import org.codehaus.plexus.PlexusTestCase;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/28
 */
public class WebSiteComponentTest extends PlexusTestCase {


    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testBasic() throws Exception {
        WebSiteComponent component = lookup(WebSiteComponent.ROLE);
        assertNotNull(component);
        component.monitor();
    }
}
