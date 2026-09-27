package com.framework;

import com.framework.client.GitHubClient;
import com.framework.config.RepositoryConfig;
import com.framework.repository.DynamoDbTestRepository;
import com.framework.repository.FrameworkActivityRepository;
import com.framework.service.GitHubActivityService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class JavaFrameworkActivityTrackerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(JavaFrameworkActivityTrackerApplication.class, args);
	}

	@Override
	public void run(String... args) throws IOException, InterruptedException {

		DynamoDbTestRepository dynamoDbTestRepository =
				new DynamoDbTestRepository();

		dynamoDbTestRepository.testConnection();

		GitHubClient gitHubClient = new GitHubClient();

		FrameworkActivityRepository activityRepository =
				new FrameworkActivityRepository();

		GitHubActivityService activityService =
				new GitHubActivityService(
						gitHubClient,
						activityRepository
				);

		List<RepositoryConfig> repositories = List.of(

				new RepositoryConfig(
						"Spring Boot",
						"spring-projects",
						"spring-boot"
				),

				new RepositoryConfig(
						"Quarkus",
						"quarkusio",
						"quarkus"
				),

				new RepositoryConfig(
						"Micronaut",
						"micronaut-projects",
						"micronaut-core"
				)
		);

		for (RepositoryConfig repository : repositories) {

			try {

				activityService.collectActivity(repository);

			} catch (Exception e) {

				System.err.println(
						"Failed collecting "
								+ repository.framework()
				);

				e.printStackTrace();
			}
		}

		System.out.println(
				"GitHub token configured: "
						+ gitHubClient.isAuthenticated()
		);
	}
}