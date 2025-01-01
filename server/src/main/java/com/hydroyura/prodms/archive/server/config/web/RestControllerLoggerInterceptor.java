package com.hydroyura.prodms.archive.server.config.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class RestControllerLoggerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        log.info("111");
        return HandlerInterceptor.super.preHandle(req, res, handler);
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res, Object handler,
                           ModelAndView modelAndView) throws Exception {
        log.info("222");
        HandlerInterceptor.super.postHandle(req, res, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler, Exception ex)
        throws Exception {
        log.info("333");
        HandlerInterceptor.super.afterCompletion(req, res, handler, ex);
    }
}
