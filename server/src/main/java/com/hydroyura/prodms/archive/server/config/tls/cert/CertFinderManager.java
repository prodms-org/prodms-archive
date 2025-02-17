package com.hydroyura.prodms.archive.server.config.tls.cert;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CertFinderManager {

    @Autowired
    private Collection<CertFinder> certFinders = Collections.emptyList();


    public void findCert() {
        Optional<CertFinder.CertKeyContainer> result = Optional.empty();
        for (CertFinder finder : certFinders) {
            if (result.isPresent()) {
                break;
            }
            result = finder.find();
        }

        // TODO: create custom ex
        var container = result.orElseThrow(() -> new RuntimeException(""));



    }

}
