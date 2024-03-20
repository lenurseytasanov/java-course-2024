package edu.java.scrapper;

import edu.java.dto.LinkUpdateRequest;
import edu.java.scrapper.configuration.ApplicationConfig;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class BotClient {

    private final WebClient webClient;

    @Autowired(required = false)
    public BotClient(WebClient.Builder webClientBuilder, ApplicationConfig config) {
        this(webClientBuilder, config.botClientBaseUrl());
    }

    @Autowired(required = false)
    public BotClient(WebClient.Builder webClientBuilder, String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public void sendUpdate(LinkUpdateRequest linkUpdateRequest) {
        String body = """
            {
                "id": %d,
                "url": "%s",
                "description": "%s",
                "tgChatIds": [%s]
            }""".formatted(linkUpdateRequest.id(), linkUpdateRequest.url(),
            linkUpdateRequest.description(),
            Arrays.stream(linkUpdateRequest.tgChatIds())
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(", "))
        );

        this.webClient.post()
            .uri("/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .retrieve().toBodilessEntity().block();
    }
}
