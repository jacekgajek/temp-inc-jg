package io.kontak.apps.anomaly.detector

import io.kontak.apps.event.Anomaly
import io.kontak.apps.event.TemperatureReading

interface AnomalyDetector : (TemperatureReading) -> List<Anomaly>

context(T)
inline fun <reified T : AnomalyDetector> TemperatureReading.toAnomaly(): Anomaly = Anomaly(
    readingID = id,
    roomId = roomId,
    thermometerId = thermometerId,
    timestamp = timestamp,
    temperature = temperature,
    anomalyDetector = T::class.simpleName!!
)
