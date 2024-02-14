package io.kontak.apps.event

import java.time.Instant
import java.util.UUID

@JvmRecord
data class TemperatureReading(val id: String = UUID.randomUUID().toString(), val temperature: Double, val roomId: String, @JvmField val thermometerId: String, val timestamp: Instant)
