package ibm.gse.eda.maas;

import org.junit.Rule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class BaseITTest {
    @Rule
    public static KafkaContainer kafkaContainerForTest = new KafkaContainer(DockerImageName.parse("strimzi/kafka:latest-kafka-2.6.0"));
   
    @BeforeAll
    public static void startAll() {
        kafkaContainerForTest.start();
    }

    @AfterAll
    public static void stopAll() {
        kafkaContainerForTest.stop();
    }

}