package io.kontak.apps.anomaly.api

import io.kontak.apps.db.AnomalyDocument
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository
import reactor.core.publisher.Flux

interface AnomalyRepository : ReactiveElasticsearchRepository<AnomalyDocument, String> {
    fun getAnomalyDocumentByRoomId(roomId: String): Flux<AnomalyDocument>
    fun getAnomalyDocumentByThermometerId(thermometerId: String): Flux<AnomalyDocument>
}
