package com.framework.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.framework.model.GitHubIssue;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class GitHubClient {

    private static final String BASE_URL =
            "https://api.github.com";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String githubToken;

    public GitHubClient() {

        this.httpClient =
                HttpClient.newHttpClient();

        this.objectMapper =
                new ObjectMapper();

        this.objectMapper.registerModule(
                new JavaTimeModule()
        );

        this.githubToken =
                System.getenv("GITHUB_TOKEN");
    }

    public List<GitHubIssue> getIssues(
            String owner,
            String repository
    ) throws IOException, InterruptedException {

        String url =
                BASE_URL
                        + "/repos/"
                        + owner
                        + "/"
                        + repository
                        + "/issues"
                        + "?state=all"
                        + "&sort=created"
                        + "&direction=desc"
                        + "&per_page=100";

        HttpRequest.Builder requestBuilder =
                HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header(
                                "Accept",
                                "application/vnd.github+json"
                        )
                        .header(
                                "X-GitHub-Api-Version",
                                "2026-03-10"
                        );

        if (githubToken != null
                && !githubToken.isBlank()) {

            requestBuilder.header(
                    "Authorization",
                    "Bearer " + githubToken
            );
        }

        HttpRequest request =
                requestBuilder.GET().build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() != 200) {

            throw new RuntimeException(
                    "GitHub API request failed. Status: "
                            + response.statusCode()
                            + " Body: "
                            + response.body()
            );
        }

        return objectMapper.readValue(
                response.body(),
                new TypeReference<List<GitHubIssue>>() {
                }
        );
    }

    public boolean isAuthenticated()
            throws IOException, InterruptedException {

        return githubToken != null
                && !githubToken.isBlank();
    }

}