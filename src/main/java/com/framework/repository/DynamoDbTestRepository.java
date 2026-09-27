package com.framework.repository;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ListTablesResponse;

public class DynamoDbTestRepository {

    private final DynamoDbClient dynamoDbClient;

    public DynamoDbTestRepository() {
        this.dynamoDbClient = DynamoDbClient.create();
    }

    public void testConnection() {

        ListTablesResponse response =
                dynamoDbClient.listTables();

        System.out.println("AWS DynamoDB connection successful");
        System.out.println("Tables: " + response.tableNames());
    }
}