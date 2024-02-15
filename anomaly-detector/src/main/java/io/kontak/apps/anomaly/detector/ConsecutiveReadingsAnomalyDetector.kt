package io.kontak.apps.anomaly.detector

import io.kontak.apps.anomaly.detector.config.logger
import io.kontak.apps.event.Anomaly
import io.kontak.apps.event.TemperatureReading
import org.springframework.stereotype.Component
import java.util.concurrent.ArrayBlockingQueue
import kotlin.math.abs

@Component
class ConsecutiveReadingsAnomalyDetector : AnomalyDetector {
    private val count = 10
    private val threshold = 5.0
    private val log = logger()
    private val readingBuffer = HashMap<String, SizeBoundQueue>()

    override fun invoke(t: TemperatureReading): List<Anomaly> {
        val buffer = readingBuffer.computeIfAbsent(t.thermometerId) { SizeBoundQueue(count) }
        buffer.offer(t)
        return buffer.scanForAnomalies(threshold).map { it.toAnomaly() }.onEach { log.trace("Anomaly detected: {}", it) }
    }


    private class SizeBoundQueue(private val count: Int) {
        private val buffer: ArrayBlockingQueue<TemperatureReading> = ArrayBlockingQueue<TemperatureReading>(count)

        fun offer(t: TemperatureReading) {
            if (buffer.remainingCapacity() == 0) {
                buffer.poll()
            }
            buffer.offer(t)
        }

        fun scanForAnomalies(threshold: Double): List<TemperatureReading> {
            if (buffer.remainingCapacity() > 0) {
                return emptyList()
            } else {
                val temperatures = buffer.map { it.temperature }
                val sum: Double = temperatures.sum()
                val n = count - 1
                return buffer.asSequence()
                    .filter { abs(it.temperature - (sum - it.temperature) / n) > threshold }
                    .toList()
            }
        }
    }
}