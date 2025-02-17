package com.hydroyura.prodms.archive.server.controller.api;


import static com.hydroyura.prodms.archive.server.SharedConstants.REQUEST_ATTR_UUID_KEY;
import static com.hydroyura.prodms.archive.server.SharedConstants.REQUEST_TIMESTAMP_KEY;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;

public abstract class AbstractRestController {

    private static final String GOT_REQUEST_MSG = "";

    protected final Logger log = LoggerFactory.getLogger(this.getClass());


    @ModelAttribute
    private void requestPreProcess(HttpServletRequest request) {
        UUID uuid = UUID.randomUUID();
        request.setAttribute(REQUEST_ATTR_UUID_KEY, uuid);
        Timestamp timestamp = Timestamp.from(Instant.now());
        request.setAttribute(REQUEST_TIMESTAMP_KEY, timestamp);
    }
}
