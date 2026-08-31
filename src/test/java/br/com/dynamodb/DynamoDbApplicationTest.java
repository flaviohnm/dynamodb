package br.com.dynamodb;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

public class DynamoDbApplicationTest {

    @Test
    void main_deveChamarSpringApplicationRunComAClasseEArgsCorretos() {
        String[] args = {"--spring.profiles.active=test"};

        try (MockedStatic<SpringApplication> springApplicationMock = mockStatic(SpringApplication.class)) {
            DynamoDbApplication.main(args);

            springApplicationMock.verify(() ->
                    SpringApplication.run(eq(DynamoDbApplication.class), eq(args))
            );
        }
    }

    @Test
    void main_devePropagarArrayDeArgsVazio() {
        String[] args = {};

        try (MockedStatic<SpringApplication> springApplicationMock = mockStatic(SpringApplication.class)) {
            DynamoDbApplication.main(args);

            springApplicationMock.verify(() ->
                    SpringApplication.run(eq(DynamoDbApplication.class), eq(args))
            );
        }
    }
}
