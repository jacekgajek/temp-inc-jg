package io.kontak.apps.anomaly.storage

import io.kontak.apps.db.toDocument
import io.kontak.apps.event.Anomaly
import org.springframework.stereotype.Service

@Service
class AnomalyStorageService(private val repository: AnomalyRepository) {
    fun store(a: Anomaly): Anomaly {
        println("Saving anomaly: $a")
        repository.save(a.toDocument())
        return a
    }
}