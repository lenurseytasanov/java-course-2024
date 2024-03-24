package edu.java.scrapper.stackoverflow;

import edu.java.scrapper.configuration.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class StackOverflowClient {

    private final WebClient webClient;

    @Autowired(required = false)
    public StackOverflowClient(
        WebClient.Builder webClientBuilder,
        ApplicationConfig config
    ) {
        this(webClientBuilder, config.stackoverflowBaseUrl());
    }

    @Autowired(required = false)
    public StackOverflowClient(WebClient.Builder webClientBuilder, String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public StackOverflowResponse fetchUpdate(long id) {
        return this.webClient.get()
            .uri("/2.3/questions/{id}?site=stackoverflow", id)
            .retrieve()
            .bodyToMono(StackOverflowResponse.class)
            .block();
    }
}
