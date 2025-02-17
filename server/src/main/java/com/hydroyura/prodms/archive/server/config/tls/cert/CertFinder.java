package com.hydroyura.prodms.archive.server.config.tls.cert;

import java.util.Optional;
import lombok.Data;

public interface CertFinder {

    Optional<CertKeyContainer> find();

    @Data
    class CertKeyContainer {
        private String cert;
        private String privateKey;
    }

}
