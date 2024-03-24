package edu.java.scrapper;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.github.GitHubClient;
import edu.java.scrapper.github.GitHubResponse;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest(httpPort = 8080)
public class ScrapperApplicationTest {

    private GitHubClient gitHubClient;

    @BeforeEach
    public void setUp() {
        stubFor(get(urlEqualTo("/repos/user/test"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "updated_at": "2011-01-26T19:14:43Z"
                    }""")));
         gitHubClient = new GitHubClient(WebClient.builder(), "http://localhost:8080");
    }

    @Test
    public void correctRequestTest() {
        GitHubResponse response = gitHubClient.fetchUpdate("user", "test");

        assertEquals(response.updatedAt(), OffsetDateTime.parse("2011-01-26T19:14:43Z"));
        assertNotEquals(response.updatedAt(), OffsetDateTime.parse("2023-01-26T19:14:43Z"));
    }

    @Test
    public void failedRequestTest() {
        assertThrows(WebClientResponseException.class, () -> gitHubClient.fetchUpdate("user", "repo"));
    }
}
