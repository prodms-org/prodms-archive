package com.hydroyura.prodms.archive.server.config.web;

import static com.hydroyura.prodms.archive.server.SharedConstants.LOG_MSG_GOT_REQUEST;
import static com.hydroyura.prodms.archive.server.SharedConstants.REQUEST_ATTR_UUID_KEY;
import static com.hydroyura.prodms.archive.server.SharedConstants.REQUEST_LOG_ID_HEADER_NAME;
import static com.hydroyura.prodms.archive.server.SharedConstants.REQUEST_TIMESTAMP_KEY;
import static com.hydroyura.prodms.archive.server.SharedConstants.REQUEST_URI_KEY;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class RestControllerLoggerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        UUID uuid = extractUUDI(req);
        Timestamp ts = Timestamp.from(Instant.now());
        String uri = req.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();

        log.info(LOG_MSG_GOT_REQUEST, uri, uuid, ts);

        req.setAttribute(REQUEST_ATTR_UUID_KEY, uuid.toString());
        req.setAttribute(REQUEST_TIMESTAMP_KEY, ts.toString());
        req.setAttribute(REQUEST_URI_KEY, uri);

        return Boolean.TRUE;
    }

    private UUID extractUUDI(HttpServletRequest req) {
        var uuid = Optional
            .ofNullable(req.getHeader(REQUEST_LOG_ID_HEADER_NAME))
            .map(UUID::fromString);
        return uuid.orElseGet(this::generateUUID);
    }

    private UUID generateUUID() {
        log.warn("Request doesn't contain UUID in header. New one will be created...");
        return UUID.randomUUID();
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res, Object handler, ModelAndView modelAndView) {}

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler, Exception ex) {}
}
