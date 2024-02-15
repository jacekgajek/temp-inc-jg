package io.kontakt.apps.anomaly.detector

import io.kontak.apps.anomaly.api.AnomalyAnalyticsService
import io.kontak.apps.anomaly.api.AnomalyRepository
import io.kontak.apps.anomaly.api.ThermometerAnomalies
import io.kontak.apps.db.AnomalyDocument
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class AnomalyAnalyticsServiceTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var anomalyAnalyticsService: AnomalyAnalyticsService

    @Autowired
    private lateinit var anomalyRepository: AnomalyRepository

    @Test
    fun roomAnoms() {
        val ee = listOf(
            AnomalyDocument("1", 10.0, "r1", "t1", 123, "d1"),
            AnomalyDocument("2", 10.0, "r1", "t2", 124, "d2"),
            AnomalyDocument("3", 10.0, "r2", "t3", 125, "d2"),
            AnomalyDocument("4", 10.0, "r2", "t4", 126, "d1"),
        )

        anomalyRepository.deleteAll().block()
        anomalyRepository.saveAll(ee).collectList().block()

        runBlocking {
            delay(1000)
            assertThat(anomalyRepository.count().block()!!).isEqualTo(4)

            val anoms = anomalyAnalyticsService.getRoomAnomalies("r1")
            assertThat(anoms).hasSize(2)
            assertThat(anoms[0].readingID).isEqualTo("1")
            assertThat(anoms[1].readingID).isEqualTo("2")
        }
    }

    @Test
    fun thermometerAnoms() {
        val ee = listOf(
            AnomalyDocument("1", 10.0, "r1", "t1", 123, "d1"),
            AnomalyDocument("2", 10.0, "r1", "t2", 124, "d2"),
            AnomalyDocument("3", 10.0, "r2", "t3", 125, "d2"),
            AnomalyDocument("4", 10.0, "r2", "t4", 126, "d1"),
        )

        anomalyRepository.deleteAll().block()
        anomalyRepository.saveAll(ee).collectList().block()


        runBlocking {
            delay(1000)
            assertThat(anomalyRepository.count().block()!!).isEqualTo(4)

            val anoms = anomalyAnalyticsService.getThermometerAnomalies("t4")
            assertThat(anoms).hasSize(1)
            assertThat(anoms[0].readingID).isEqualTo("4")
        }
    }

    @Test
    fun thermometerThreshold() {
        val ee = listOf(
            AnomalyDocument("1", 10.0, "r1", "t1", 123, "d1"),
            AnomalyDocument("2", 10.0, "r1", "t3", 124, "d2"),
            AnomalyDocument("3", 10.0, "r2", "t3", 125, "d2"),
            AnomalyDocument("4", 10.0, "r2", "t4", 126, "d1"),
            AnomalyDocument("5", 10.0, "r2", "t4", 126, "d1"),
            AnomalyDocument("6", 10.0, "r2", "t4", 126, "d1"),
            AnomalyDocument("7", 10.0, "r2", "t4", 126, "d1"),
            AnomalyDocument("8", 10.0, "r1", "t2", 124, "d2"),
            AnomalyDocument("9", 10.0, "r2", "t2", 125, "d2"),
            AnomalyDocument("10", 10.0, "r2", "t2", 125, "d2"),
        )

        anomalyRepository.deleteAll().block()
        anomalyRepository.saveAll(ee).collectList().block()

        runBlocking {
            delay(1000)
            assertThat(anomalyRepository.count().block()!!).isEqualTo(10)

            val therms = anomalyAnalyticsService.getThermometers(2)
            assertThat(therms).hasSize(2)
            assertThat(therms).hasSameElementsAs(listOf(ThermometerAnomalies("t2", 3), ThermometerAnomalies("t4", 4)))
        }
    }
}