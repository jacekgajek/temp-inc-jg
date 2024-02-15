package io.kontak.apps.anomaly.api

import io.kontak.apps.db.AnomalyDocument
import io.kontak.apps.db.toEvent
import io.kontak.apps.event.Anomaly
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.search.aggregations.Aggregations
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.stereotype.Service

@Service
class AnomalyAnalyticsService(
    private val repo: AnomalyRepository,
    private val ops: ElasticsearchOperations
) {

    suspend fun getThermometerAnomalies(thermometerId: String) : List<Anomaly> {
        return repo.getAnomalyDocumentByThermometerId(thermometerId).asFlow()
            .map { it.toEvent() }
            .toList()
    }

    suspend fun getRoomAnomalies(roomId: String) : List<Anomaly> {
        return repo.getAnomalyDocumentByRoomId(roomId).asFlow()
            .map { it.toEvent() }
            .toList()
    }

    /**    list of thermometers with amount of anomalies higher that threshold (provided as query param or from properties) */
    suspend fun getThermometers(anomalyCountThreshold: Long) : List<ThermometerAnomalies> {
        val agg = AggregationBuilders.terms("thermometer_ids")
            .field("thermometer_id.keyword")
            .minDocCount(anomalyCountThreshold + 1)
        val query = NativeSearchQueryBuilder().withAggregations(agg)
            .withMaxResults(0)
            .build()
        val hits = ops.search(query, AnomalyDocument::class.java)
        val aggs = hits.aggregations.aggregations() as Aggregations
        val termsAgg = aggs.get("thermometer_ids") as ParsedStringTerms
        return termsAgg.buckets.map { ThermometerAnomalies(it.keyAsString, it.docCount) }
    }
}

data class ThermometerAnomalies(val thermometerId: String, val anomalyCount: Long)
