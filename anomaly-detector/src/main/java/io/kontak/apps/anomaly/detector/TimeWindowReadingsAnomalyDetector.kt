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

    override fun invoke(t: TemperatureReading): List<Anomaly> {
        val readings = readingBuffer.computeIfAbsent(t.thermometerId) { TimeBoundQueue() }
        readings.offer(t)
        return readings.scanForAnomalies(threshold).map { it.toAnomaly() }.onEach { println("Anomaly detected: $it") }
    }

    private class TimeBoundQueue(
        private val windowSeconds: Int = 10,
        private val queue: Queue<TemperatureReading> = ConcurrentLinkedQueue<TemperatureReading>()
    ) : Iterable<TemperatureReading> by queue {
        fun offer(t: TemperatureReading) {
            queue.isEmpty()
            while (queue.isNotEmpty() && Duration.between(queue.first().timestamp, t.timestamp).toSeconds() > windowSeconds) {
                queue.poll()
            }
            queue.offer(t)
        }

        fun scanForAnomalies(threshold: Double): List<TemperatureReading> {
            val avg = averageTemperature()
            return if (avg == null) {
                emptyList()
            } else {
                queue.asSequence()
                    .filter { abs(it.temperature - avg) > threshold }
                    .toList()
            }
        }

        fun averageTemperature(): Double? {
            var sum = 0.0
            var count = 0
            for (t in this) {
                sum += t.temperature
                count++
            }
            return if (count > 0) sum / count else null
        }

    }
}
