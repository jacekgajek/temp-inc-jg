package io.kontak.apps.anomaly.storage

import io.kontak.apps.anomaly.storage.config.logger
import io.kontak.apps.event.Anomaly
import org.apache.kafka.streams.kstream.KStream
import org.springframework.stereotype.Component
import java.util.function.Consumer

@Component
class AnomalyListener(private val storageService: AnomalyStorageService) : Consumer<KStream<String, Anomaly>> {
    private val log = logger()
    override fun accept(events: KStream<String, Anomaly>) {
        events.peek { _, a -> log.trace("Received anomaly: {}", a) }
            .foreach { _, a ->
                storageService.store(a)
            }
    }
}
