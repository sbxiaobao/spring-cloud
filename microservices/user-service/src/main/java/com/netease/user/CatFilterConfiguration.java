//package com.netease.user;
//
//import com.dianping.cat.servlet.CatFilter;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * 〈〉
// *
// * @author baodekang
// * @create 2019/4/12
// */
//@Configuration
//public class CatFilterConfiguration {
//
//    @Bean
//    public FilterRegistrationBean catFilter() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//
//        CatFilter filter = new CatFilter();
//        registration.setFilter(filter);
//        registration.addUrlPatterns("/*");
//        registration.setName("cat-filter");
//        registration.setOrder(2);
//        return registration;
//    }
//}
