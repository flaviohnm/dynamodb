package br.com.dynamodb.config;

import br.com.dynamodb.model.Customer;
import io.awspring.cloud.dynamodb.DynamoDbTableNameResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import static org.assertj.core.api.Assertions.assertThat;

class DynamoDBConfigurationTest {

    private static final String ACCESS_KEY = "test-access-key";
    private static final String SECRET_KEY = "test-secret-key";
    private static final String ENDPOINT = "http://localhost:8000";
    private static final String REGION = "sa-east-1";

    private DynamoDBConfiguration configuration;

    @BeforeEach
    void setUp() {
        configuration = new DynamoDBConfiguration();
        ReflectionTestUtils.setField(configuration, "accessKey", ACCESS_KEY);
        ReflectionTestUtils.setField(configuration, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(configuration, "endpoint", ENDPOINT);
        ReflectionTestUtils.setField(configuration, "region", REGION);
    }

    @Test
    void getDynamoDbClient_deveCriarClienteComEndpointERegiaoConfigurados() {
        DynamoDbClient client = configuration.getDynamoDbClient();

        assertThat(client).isNotNull();
        assertThat(client.serviceClientConfiguration().region().id()).isEqualTo(REGION);
        assertThat(client.serviceClientConfiguration().endpointOverride())
                .isPresent()
                .get()
                .isEqualTo(java.net.URI.create(ENDPOINT));
    }

    @Test
    void getDynamoDbEnhancedClient_deveEncapsularClienteBaseRecebido() {
        DynamoDbClient baseClient = configuration.getDynamoDbClient();

        DynamoDbEnhancedClient enhancedClient = configuration.getDynamoDbEnhancedClient(baseClient);

        assertThat(enhancedClient).isNotNull();
    }

    @Test
    void dynamoDbTableNameResolver_deveRetornarCustomersParaClasseCustomer() {
        DynamoDbTableNameResolver resolver = configuration.dynamoDbTableNameResolver();

        String tableName = resolver.resolve(Customer.class);

        assertThat(tableName).isEqualTo("customers");
    }

    @Test
    void dynamoDbTableNameResolver_deveRetornarNomeSimplesEmMinusculoParaOutrasClasses() {
        DynamoDbTableNameResolver resolver = configuration.dynamoDbTableNameResolver();

        String tableName = resolver.resolve(String.class);

        assertThat(tableName).isEqualTo("string");
    }

    @Test
    void dynamoDbTableNameResolver_naoDeveConfundirClasseComNomeParecidoComCustomer() {
        // Garante que a comparação é por Class.equals (identidade de tipo),
        // não por nome — mata mutantes que trocassem equals por comparação de String.
        class Customer {
        }

        DynamoDbTableNameResolver resolver = configuration.dynamoDbTableNameResolver();

        String tableName = resolver.resolve(Customer.class);

        assertThat(tableName).isEqualTo("customer");
    }
}