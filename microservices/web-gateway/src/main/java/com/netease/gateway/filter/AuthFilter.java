package com.netease.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.thoughtworks.xstream.core.BaseException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/12
 */
public class AuthFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        logger.info("AuthFilter run");
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headers = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        String userId = headers.get("userId");
        if (StringUtils.isBlank(userId)) {
            logger.error("Auth failed");
            return false;
        } else {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                ctx.addZuulRequestHeader(entry.getKey(), entry.getValue());
            }
        }
        return null;
    }
}
