package io.kontak.apps.anomaly.storage

import io.kontak.apps.db.AnomalyDocument
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface AnomalyRepository : ElasticsearchRepository<AnomalyDocument, String> {
}