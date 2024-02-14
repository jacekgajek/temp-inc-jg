package io.kontak.apps.anomaly.detector

import io.kontak.apps.event.Anomaly
import io.kontak.apps.event.TemperatureReading
import org.springframework.stereotype.Component
import java.util.concurrent.ArrayBlockingQueue
import kotlin.math.abs

@Component
class ConsecutiveReadingsAnomalyDetector : AnomalyDetector {
    private val count = 10
    private val threshold = 5.0
    private val readingBuffer = HashMap<String, SizeBoundQueue>()

    data class MarkedTemperatureReading(val reading: TemperatureReading, var marked: Boolean = false)

    override fun invoke(t: TemperatureReading): List<Anomaly> {
        val buffer = readingBuffer.computeIfAbsent(t.thermometerId) { SizeBoundQueue(count) }
        buffer.offer(t)
        return buffer.scanForAnomalies(threshold).map { it.reading.toAnomaly() }.onEach { println("Anomaly detected: $it") }
    }


    private class SizeBoundQueue(private val count: Int) {
        private val buffer: ArrayBlockingQueue<MarkedTemperatureReading> = ArrayBlockingQueue<MarkedTemperatureReading>(count)

        fun offer(t: TemperatureReading) {
            if (buffer.remainingCapacity() == 0) {
                buffer.poll()
            }
            buffer.offer(MarkedTemperatureReading(t))
        }

        fun scanForAnomalies(threshold: Double): List<MarkedTemperatureReading> {
            if (buffer.remainingCapacity() > 0) {
                return emptyList()
            } else {
                val temperatures = buffer.map { it.reading.temperature }
                val sum: Double = temperatures.sum()
                val n = count - 1
                val anomalies = buffer.asSequence()
                    .filterNot { it.marked }
                    .filter { abs(it.reading.temperature - (sum - it.reading.temperature) / n) > threshold }
                    .toList()
                anomalies.forEach { it.marked = true }
                return anomalies
            }
        }
    }
}