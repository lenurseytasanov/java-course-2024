package edu.java;

import edu.java.dto.LinkUpdateRequest;
import edu.java.github.GitHubResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class BotClient {

    private final WebClient webClient;

    public BotClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost").build();
    }

    public void sendUpdate(LinkUpdateRequest linkUpdateRequest) {
        String body = """
            {
                "id": %d,
                "url": %s,
                "description": %s,
                "tgChatIds": [%s]
            }""".formatted(linkUpdateRequest.id(), linkUpdateRequest.url(),
            linkUpdateRequest.description(),
            Arrays.stream(linkUpdateRequest.tgChatIds())
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(", "))
        );

        this.webClient.post()
            .uri("/updates")
            .bodyValue(body)
            .retrieve();
    }
}
