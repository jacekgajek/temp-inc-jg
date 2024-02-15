package io.kontak.apps.anomaly.detector

import io.kontak.apps.event.Anomaly
import io.kontak.apps.event.TemperatureReading
import org.apache.kafka.streams.kstream.KStream
import java.util.function.Function

open class TemperatureMeasurementsListener(private val anomalyDetectors: List<AnomalyDetector>) : Function<KStream<String, TemperatureReading>, KStream<String, Anomaly>> {
    override fun apply(events: KStream<String, TemperatureReading>): KStream<String, Anomaly> {
        return events
            .flatMapValues { temperatureReading: TemperatureReading -> detectAnomalies(temperatureReading) }
            .selectKey { _, anomaly: Anomaly -> anomaly.thermometerId }
    }

    private fun detectAnomalies(temperatureReading: TemperatureReading): Iterable<Anomaly> =
        anomalyDetectors.asSequence()
            .map { detector -> detector(temperatureReading) }
            .flatten()
            .asIterable()
            .toList()


    /*
    POSSIBLE ALTERNATIVE SOLUTION:

    Use Kafka time-window capabilities, something like this:

        override fun apply(events: KStream<String, TemperatureReading>): KStream<String, Anomaly> {
        return events
            .selectKey { s, value -> value.id.toByteArray() }
            .groupByKey()
            .windowedBy(TimeWindows.ofSizeAndGrace(ofSeconds(10), ofSeconds(1)).advanceBy(ofSeconds(1)))
            .aggregate(
                { ReadingList() },
                { key, reading, list -> ReadingList(list.readings + reading) },
                Materialized.with(Serdes.ByteArray(), JsonSerde(ReadingList::class.java))
            )
            .mapValues { readings -> detectAnomalies(readings.readings) }
            .toStream()
            .flatMapValues { _, v -> v }
            .selectKey { _, value -> value.readingID }
    }
     */
}
