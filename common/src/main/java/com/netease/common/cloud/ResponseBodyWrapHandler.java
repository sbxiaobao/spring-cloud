package com.netease.common.cloud;

import com.netease.common.pojo.ResponseBody;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/28
 */
public class ResponseBodyWrapHandler implements HandlerMethodReturnValueHandler {

    private HandlerMethodReturnValueHandler delegate;

    public ResponseBodyWrapHandler(HandlerMethodReturnValueHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return delegate.supportsReturnType(returnType);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        int code = ((ServletWebRequest) webRequest).getResponse().getStatus();
        if (code == HttpStatus.OK.value()) {
            ResponseBody responseBody = new ResponseBody();
            responseBody.setResult(returnValue);
            delegate.handleReturnValue(responseBody, returnType, mavContainer, webRequest);
        } else {
            delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
        }
    }
}