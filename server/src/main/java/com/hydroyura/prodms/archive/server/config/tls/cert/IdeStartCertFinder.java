package com.hydroyura.prodms.archive.server.config.tls.cert;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class IdeStartCertFinder implements CertFinder {

    private static final String PATH = "./tls";
    private static final String CERT_FILE = "prodms-archive.crt";
    private static final String PRIVATE_KEY_FILE = "prodms-archive.key";
    private static final String ERROR_MSG_DIRECTORY_NOT_FIND = "Can not find directory on path [{}]";

    @Override
    public Optional<CertKeyContainer> find() {
        Path path = Paths.get(PATH);
        if (Files.exists(path) && Files.isDirectory(path)) {
            String cert;
            try(InputStream is = Files.newInputStream(Paths.get(PATH + "/" + CERT_FILE))) {
                cert = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            } catch (Exception e) {
                log.warn("Cannot read cert from file = [%s]".formatted(PATH + "/" + CERT_FILE), e);
                return Optional.empty();
            }

            String privateKey;
            try(InputStream is = Files.newInputStream(Paths.get(PATH + "/" + PRIVATE_KEY_FILE))) {
                privateKey = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            } catch (Exception e) {
                log.warn("Cannot read private key from file = [%s]".formatted(PATH + "/" + PRIVATE_KEY_FILE), e);
                return Optional.empty();
            }
            var certKey = new CertKeyContainer();
            certKey.setCert(cert);
            certKey.setPrivateKey(privateKey);
            return Optional.of(certKey);
        } else {
            log.warn(ERROR_MSG_DIRECTORY_NOT_FIND, PATH);
            return Optional.empty();
        }
    }

}
