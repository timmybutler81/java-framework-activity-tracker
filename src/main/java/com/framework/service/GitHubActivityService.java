package com.framework.service;

import com.framework.client.GitHubClient;
import com.framework.config.RepositoryConfig;
import com.framework.mapper.FrameworkActivityMapper;
import com.framework.model.FrameworkActivity;
import com.framework.model.GitHubIssue;
import com.framework.repository.FrameworkActivityRepository;

import java.io.IOException;
import java.util.List;

public class GitHubActivityService {

    private final GitHubClient gitHubClient;
    private final FrameworkActivityRepository activityRepository;

    public GitHubActivityService(
            GitHubClient gitHubClient,
            FrameworkActivityRepository activityRepository
    ) {
        this.gitHubClient = gitHubClient;
        this.activityRepository = activityRepository;
    }

    public List<FrameworkActivity> collectActivity(
            RepositoryConfig repository
    ) throws IOException, InterruptedException {

        List<GitHubIssue> results =
                gitHubClient.getIssues(
                        repository.owner(),
                        repository.repository()
                );

        List<FrameworkActivity> activities =
                results.stream()
                        .map(item ->
                                new FrameworkActivity(
                                        repository.framework(),
                                        repository.owner()
                                                + "/"
                                                + repository.repository(),
                                        item.getId(),
                                        item.getNumber(),
                                        item.getTitle(),
                                        item.isPullRequest()
                                                ? "PULL_REQUEST"
                                                : "ISSUE",
                                        item.getState(),
                                        item.getCreatedAt(),
                                        item.getUpdatedAt(),
                                        item.getClosedAt()
                                )
                        )
                        .toList();

        activities.stream()
                .map(FrameworkActivityMapper::toEntity)
                .forEach(activityRepository::save);

        printSummary(
                repository,
                activities
        );

        activities.stream()
                .limit(3)
                .forEach(System.out::println);

        return activities;
    }

    private void printSummary(
            RepositoryConfig repository,
            List<FrameworkActivity> activities
    ) {

        long issues =
                activities.stream()
                        .filter(activity ->
                                "ISSUE".equals(
                                        activity.type()
                                )
                        )
                        .count();

        long pullRequests =
                activities.stream()
                        .filter(activity ->
                                "PULL_REQUEST".equals(
                                        activity.type()
                                )
                        )
                        .count();

        long openIssues =
                activities.stream()
                        .filter(activity ->
                                "ISSUE".equals(
                                        activity.type()
                                )
                        )
                        .filter(activity ->
                                "open".equalsIgnoreCase(
                                        activity.state()
                                )
                        )
                        .count();

        long closedIssues =
                activities.stream()
                        .filter(activity ->
                                "ISSUE".equals(
                                        activity.type()
                                )
                        )
                        .filter(activity ->
                                "closed".equalsIgnoreCase(
                                        activity.state()
                                )
                        )
                        .count();

        System.out.println();

        System.out.println(
                "Framework: "
                        + repository.framework()
        );

        System.out.println(
                "Repository: "
                        + repository.owner()
                        + "/"
                        + repository.repository()
        );

        System.out.println(
                "Issues returned: "
                        + issues
        );

        System.out.println(
                "Open issues: "
                        + openIssues
        );

        System.out.println(
                "Closed issues: "
                        + closedIssues
        );

        System.out.println(
                "Pull requests returned: "
                        + pullRequests
        );
    }
}