package edu.java.github;

import edu.java.configuration.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GitHubClient {

    private final WebClient webClient;

    @Autowired(required = false)
    public GitHubClient(
        WebClient.Builder webClientBuilder,
        ApplicationConfig config
    ) {
        this(webClientBuilder, config.githubBaseUrl());
    }

    @Autowired(required = false)
    public GitHubClient(WebClient.Builder webClientBuilder, String baseUrl) {
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
