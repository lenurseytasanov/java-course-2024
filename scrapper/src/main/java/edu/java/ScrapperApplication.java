package edu.java;

import edu.java.configuration.ApplicationConfig;
import edu.java.github.GitHubClient;
import edu.java.stackoverflow.StackOverflowClient;
import edu.java.stackoverflow.StackOverflowResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class ScrapperApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(ScrapperApplication.class, args);
    }

    @Autowired
    private GitHubClient gitHubClient;
    @Autowired
    private StackOverflowClient stackOverflowClient;

    @Override
    public void run(String... args) throws Exception {
//        GitHubResponse response1 = gitHubClient.fetchUpdate("lenurseytasanov", "java-course-2024");
//        System.out.println(response1.updatedAt());

//        StackOverflowResponse response = stackOverflowClient.fetchUpdate(78050639);
//        System.out.println(response.getUpdateAt());
    }
}
