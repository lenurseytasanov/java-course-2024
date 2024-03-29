package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.dto.LinkResponse;
import edu.java.dto.ListLinksResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ScrapperClient {
    private final WebClient webClient;

    private static final String DEFAULT_CHAT_PATH = "/tg-chat/%d";

    private static final String DEFAULT_LINKS_PATH = "/links";

    private static final String DEFAULT_LINKS_HEADER = "Tg-Chat-Id";

    private static final String DEFAULT_LINKS_BODY = """
            {
                "link": "%s"
            }""";

    @Autowired(required = false)
    public ScrapperClient(WebClient.Builder webClientBuilder, ApplicationConfig config) {
        this(webClientBuilder, config.scrapperClientBaseUrl());
    }

    @Autowired(required = false)
    public ScrapperClient(WebClient.Builder webClientBuilder, String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public String addChat(long id) {
        return webClient.post()
            .uri(DEFAULT_CHAT_PATH.formatted(id))
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(e -> Mono.just(e.getMessage()))
            .block();
    }

    public String removeChat(long id) {
        return webClient.delete()
            .uri(DEFAULT_CHAT_PATH.formatted(id))
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(e -> Mono.just(e.getMessage()))
            .block();
    }

    public Mono<ListLinksResponse> getLinks(long id) {
        return webClient.get()
            .uri(DEFAULT_LINKS_PATH)
            .header(DEFAULT_LINKS_HEADER, String.valueOf(id))
            .retrieve()
            .onStatus(HttpStatusCode::isError, response -> Mono.error(RuntimeException::new))
            .bodyToMono(ListLinksResponse.class);
    }

    public Mono<String> addLink(long id, String link) {
        String body = DEFAULT_LINKS_BODY.formatted(link);

        return this.webClient.post()
            .uri(DEFAULT_LINKS_PATH)
            .header(DEFAULT_LINKS_HEADER, String.valueOf(id))
            .bodyValue(body)
            .retrieve()
            .onStatus(HttpStatusCode::isError, response -> Mono.error(RuntimeException::new))
            .bodyToMono(LinkResponse.class)
            .map(response -> response.url().toString());
    }

    public Mono<String> removeLink(long id, String link) {
        String body = DEFAULT_LINKS_BODY.formatted(link);

        return this.webClient
            .method(HttpMethod.DELETE)
            .uri(DEFAULT_LINKS_PATH)
            .header(DEFAULT_LINKS_HEADER, String.valueOf(id))
            .bodyValue(body)
            .retrieve()
            .onStatus(HttpStatusCode::isError, response -> Mono.error(RuntimeException::new))
            .bodyToMono(LinkResponse.class)
            .map(response -> response.url().toString());
    }
}
