package com.hydroyura.prodms.archive.server.service;

import com.hydroyura.prodms.archive.client.model.api.ApiRes;
import com.hydroyura.prodms.files.server.api.res.GetUrlsLatestRes;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ArchiveApi {

    private static final String URI = "/api/v1/drawings/%s";
    private RestClient restClient;

    public ArchiveApi(@Value("${microservices.urls.files}") String filesUrl) {
        this.restClient = RestClient.create(filesUrl);
    }

    public Optional<GetUrlsLatestRes> get(String number) {
        return restClient
            .get()
            .uri(URI.formatted(number))
            .exchange((req, res) -> {
                if (res.getStatusCode().is2xxSuccessful()) {
                    return Optional
                        .ofNullable(res.bodyTo(new ParameterizedTypeReference<ApiRes<GetUrlsLatestRes>>() {}).getData());
                }
                return Optional.empty();
            });

    }

}
