package edu.java.github;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GitHubClient {

    private final WebClient webClient;

    public GitHubClient(
        WebClient.Builder webClientBuilder,
        @Value("${app.github-base-url}") String baseUrl
    ) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public GitHubResponse fetchUpdate(@NotNull String owner, @NotNull String repo) {
        return this.webClient.get()
            .uri("/repos/{owner}/{repo}", owner, repo)
            .retrieve()
            .bodyToMono(GitHubResponse.class)
            .block();
    }
}
