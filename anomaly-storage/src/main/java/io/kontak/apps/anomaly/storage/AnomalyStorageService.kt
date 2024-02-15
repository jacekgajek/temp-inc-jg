package io.kontak.apps.anomaly.storage

import io.kontak.apps.anomaly.storage.config.logger
import io.kontak.apps.db.toDocument
import io.kontak.apps.event.Anomaly
import org.springframework.stereotype.Service

@Service
class AnomalyStorageService(private val repository: AnomalyRepository) {
    val log = logger()
    fun store(a: Anomaly): Anomaly {
        log.trace("Saving anomaly: {}", a)
        repository.save(a.toDocument())
        return a
    }
}
