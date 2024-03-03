package edu.java.bot;

import edu.java.dto.LinkResponse;
import edu.java.dto.LinkUpdateRequest;
import edu.java.dto.ListLinksResponse;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ScrapperClient {
    private final WebClient webClient;

    public ScrapperClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost").build();
    }

    public void addChat(long id) {
        this.webClient.post()
            .uri("/tg-chat/%d".formatted(id))
            .retrieve();
    }

    public void removeChat(long id) {
        this.webClient.delete()
            .uri("/tg-chat/%d".formatted(id))
            .retrieve();
    }

    public ListLinksResponse getLinks(long id) {
        return this.webClient.get()
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(id))
            .retrieve()
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    public LinkResponse addLink(long id, String link) {
        String body = """
            {
                "link": "%s"
            }""".formatted(link);

        return this.webClient.post()
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(id))
            .bodyValue(body)
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .block();
    }

    public LinkResponse removeLink(long id, String link) {
        String body = """
            {
                "link": "%s"
            }""".formatted(link);

        return this.webClient
            .method(HttpMethod.DELETE)
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(id))
            .bodyValue(body)
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .block();
    }
}
