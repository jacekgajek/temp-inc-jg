package io.kontakt.apps.anomaly.detector

import io.kontak.apps.anomaly.detector.TimeWindowReadingsAnomalyDetector
import io.kontak.apps.event.TemperatureReading
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.jupiter.api.Test
import java.time.Instant

class TimeWindowReadingsAnomalyDetectorTest {
    @Test
    fun anomalies1() {
        val readings = listOf(
            19.1 to 1684945005L,
            19.2 to 1684945006L,
            19.5 to 1684945007L,
            19.7 to 1684945008L,
            19.3 to 1684945009L,
            25.1 to 1684945010L,
            18.2 to 1684945011L,
            19.1 to 1684945012L,
            19.2 to 1684945013L,
            25.4 to 1684945015L,
        ).map { (temp, millis) -> TemperatureReading(temperature = temp, roomId = "1", thermometerId = "1", timestamp = Instant.ofEpochMilli(millis))  }

        val detector = TimeWindowReadingsAnomalyDetector()
        val anoms = readings.map(detector).flatten()

        assertThat(anoms).hasSize(2)
        assertThat(anoms[0].temperature).isCloseTo(25.1, Offset.offset(0.01))
        assertThat(anoms[1].temperature).isCloseTo(25.4, Offset.offset(0.01))
    }
}