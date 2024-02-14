package io.kontakt.apps.anomaly.detector;

import java.time.Instant;

import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static org.assertj.core.api.Assertions.assertThat;

import io.kontak.apps.anomaly.storage.AnomalyRepository;
import io.kontak.apps.event.Anomaly;

public class AnomalyListenerTest extends AbstractIntegrationTest {

    @Value("${spring.cloud.stream.bindings.anomalyStorageProcessor-in-0.destination}")
    private String inputTopic;

    private String outputTopic = "testOutput";

    @Autowired
    private AnomalyRepository repo;

    @Test
    void testInOutFlow() throws InterruptedException {
        try (TestKafkaConsumer<Anomaly> consumer = new TestKafkaConsumer<>(
                kafkaContainer.getBootstrapServers(),
                outputTopic,
                Anomaly.class
        );
             TestKafkaProducer<Anomaly> producer = new TestKafkaProducer<>(
                     kafkaContainer.getBootstrapServers(),
                     inputTopic
             )) {
            String readingID = "aba7c743-a1dc-4bba-a3ef-3f07f4918073";
            Anomaly temperatureReading = new Anomaly(readingID, 20d, "room", "thermometer", Instant.parse("2023-01-01T00:00:00.000Z"), "MyDetector");
            repo.deleteAll();
            assertThat(repo.findAll()).hasSize(0);
            producer.produce(temperatureReading.thermometerId, temperatureReading);

            Thread.sleep(5000);

            var allAnomalies = Lists.newArrayList(repo.findAll().iterator());
            assertThat(allAnomalies).hasSize(1);
            assertThat(allAnomalies.get(0).getReadingId()).isEqualTo(readingID);
        }
    }
}
