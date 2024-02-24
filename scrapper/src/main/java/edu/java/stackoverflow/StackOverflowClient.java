package edu.java.stackoverflow;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class StackOverflowClient {

    private final WebClient webClient;

    public StackOverflowClient(
        WebClient.Builder webClientBuilder,
        @Value("${app.stackoverflow-base-url}") String baseUrl
    ) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public StackOverflowResponse fetchUpdate(int id) {
        return this.webClient.get()
            .uri("/2.3/questions/{id}?site=stackoverflow", id)
            .retrieve()
            .bodyToMono(StackOverflowResponse.class)
            .block();
    }
}
