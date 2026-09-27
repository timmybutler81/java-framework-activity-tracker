package com.framework.repository;

import com.framework.entity.FrameworkActivityEntity;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class FrameworkActivityRepository {

    private static final String TABLE_NAME =
            "framework-activity";

    private final DynamoDbTable<FrameworkActivityEntity> table;

    public FrameworkActivityRepository() {

        DynamoDbClient dynamoDbClient =
                DynamoDbClient.create();

        DynamoDbEnhancedClient enhancedClient =
                DynamoDbEnhancedClient.builder()
                        .dynamoDbClient(dynamoDbClient)
                        .build();

        this.table =
                enhancedClient.table(
                        TABLE_NAME,
                        TableSchema.fromBean(
                                FrameworkActivityEntity.class
                        )
                );
    }

    public void save(
            FrameworkActivityEntity activity
    ) {
        table.putItem(activity);
    }
}