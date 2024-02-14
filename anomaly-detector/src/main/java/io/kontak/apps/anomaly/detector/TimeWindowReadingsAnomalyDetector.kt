package io.kontak.apps.anomaly.detector

import io.kontak.apps.event.Anomaly
import io.kontak.apps.event.TemperatureReading
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.math.abs

@Component
class TimeWindowReadingsAnomalyDetector : AnomalyDetector {

    private val threshold = 5.0
    private val readingBuffer = HashMap<String, TimeBoundQueue>()
    data class MarkedTemperatureReading(val reading: TemperatureReading, var marked: Boolean = false)

    override fun invoke(t: TemperatureReading): List<Anomaly> {
        val readings = readingBuffer.computeIfAbsent(t.thermometerId) { TimeBoundQueue() }
        readings.offer(t)
        return readings.scanForAnomalies(threshold).map { it.reading.toAnomaly() }.onEach { println("Anomaly detected: $it") }
    }

    private class TimeBoundQueue(
        private val windowSeconds: Int = 10,
        private val queue: Queue<MarkedTemperatureReading> = ConcurrentLinkedQueue<MarkedTemperatureReading>()
    ) : Iterable<MarkedTemperatureReading> by queue {
        fun offer(t: TemperatureReading) {
            queue.isEmpty()
            while (queue.isNotEmpty() && Duration.between(queue.first().reading.timestamp, t.timestamp).toSeconds() > windowSeconds) {
                queue.poll()
            }
            queue.offer(MarkedTemperatureReading(t))
        }

        fun scanForAnomalies(threshold: Double): List<MarkedTemperatureReading> {
            val avg = averageTemperature()
            return if (avg == null) {
                 emptyList()
            } else {
                val anomalies = queue.asSequence()
                    .filterNot { it.marked }
                    .filter { abs(it.reading.temperature - avg) > threshold }
                    .toList()

                anomalies.forEach { it.marked = true }
                anomalies
            }
        }

        fun averageTemperature(): Double? {
            var sum = 0.0
            var count = 0
            for (t in this) {
                sum += t.reading.temperature
                count++
            }
            return if (count > 0) sum / count else null
        }

    }
}
