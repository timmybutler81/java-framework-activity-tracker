package com.framework.model;

import java.time.OffsetDateTime;

public record FrameworkActivity(
        String framework,
        String repository,
        long githubId,
        int number,
        String title,
        String type,
        String state,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        OffsetDateTime closedAt
) {
}