package io.kontakt.apps.anomaly.detector

import io.kontak.apps.anomaly.detector.ConsecutiveReadingsAnomalyDetector
import io.kontak.apps.event.TemperatureReading
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.jupiter.api.Test
import java.time.Instant

class ConsecutiveReadingsAnomalyDetectorTest {
    @Test
    fun testNoAnomaly() {
        val detector = ConsecutiveReadingsAnomalyDetector()
        val readings = listOf(
            20.1, 21.2, 20.3, 19.1, 20.1, 19.2, 20.1, 18.1, 19.4, 20.1, 23.1
        ).map { makeReading(it) }

        val anomalies = readings.map(detector).flatten()

        assertThat(anomalies).isEmpty()
    }

    @Test
    fun testAnomaly1() {
        val detector = ConsecutiveReadingsAnomalyDetector()
        val readings = listOf(
            20.1, 21.2, 20.3, 19.1, 20.1, 19.2, 20.1, 18.1, 19.4, 20.1, 27.1, 23.1
        ).map { makeReading(it) }

        val anomalies = readings.map(detector).flatten()


        assertThat(anomalies).hasSize(1)
        assertThat(anomalies[0].temperature).isCloseTo(27.1, Offset.offset(0.01))
    }

    @Test
    fun testAnomaly2() {
        val detector = ConsecutiveReadingsAnomalyDetector()
        val readings = listOf(
            27.1, 20.1, 21.2, 20.3, 19.1, 20.1, 19.2, 20.1, 18.1, 19.4, 20.1, 23.1
        ).map { makeReading(it) }

        val anomalies = readings.map(detector).flatten()


        assertThat(anomalies).hasSize(1)
        assertThat(anomalies[0].temperature).isCloseTo(27.1, Offset.offset(0.01))
    }

    fun makeReading(t: Double) = TemperatureReading(temperature = t, roomId = "1", thermometerId = "1", timestamp = Instant.now())

}