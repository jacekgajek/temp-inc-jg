package io.kontak.apps.event

import java.time.Instant

@JvmRecord
data class Anomaly(val readingID: String, val temperature: Double, val roomId: String, @JvmField val thermometerId: String, val timestamp: Instant, val anomalyDetector: String)

