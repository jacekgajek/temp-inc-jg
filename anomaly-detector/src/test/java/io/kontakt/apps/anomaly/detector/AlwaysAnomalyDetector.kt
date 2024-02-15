package io.kontakt.apps.anomaly.detector

import io.kontak.apps.anomaly.detector.AnomalyDetector
import io.kontak.apps.anomaly.detector.toAnomaly
import io.kontak.apps.event.Anomaly
import io.kontak.apps.event.TemperatureReading
import org.springframework.stereotype.Component

@Component
class AlwaysAnomalyDetector : AnomalyDetector {
    override fun invoke(p1: TemperatureReading): List<Anomaly> {
        return listOf(p1.toAnomaly())
    }
}