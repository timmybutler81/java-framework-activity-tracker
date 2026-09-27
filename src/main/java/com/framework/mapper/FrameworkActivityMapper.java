package com.framework.mapper;

import com.framework.entity.FrameworkActivityEntity;
import com.framework.model.FrameworkActivity;

public class FrameworkActivityMapper {

    private FrameworkActivityMapper() {
    }

    public static FrameworkActivityEntity toEntity(
            FrameworkActivity activity
    ) {

        FrameworkActivityEntity entity =
                new FrameworkActivityEntity();

        entity.setGithubId(activity.githubId());
        entity.setFramework(activity.framework());
        entity.setRepository(activity.repository());
        entity.setNumber(activity.number());
        entity.setTitle(activity.title());
        entity.setType(activity.type());
        entity.setState(activity.state());

        entity.setCreatedAt(
                activity.createdAt() != null
                        ? activity.createdAt().toString()
                        : null
        );

        entity.setUpdatedAt(
                activity.updatedAt() != null
                        ? activity.updatedAt().toString()
                        : null
        );

        entity.setClosedAt(
                activity.closedAt() != null
                        ? activity.closedAt().toString()
                        : null
        );

        entity.setCollectedAt(
                java.time.OffsetDateTime.now().toString()
        );

        return entity;
    }
}