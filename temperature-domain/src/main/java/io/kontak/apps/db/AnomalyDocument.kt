package io.kontak.apps.db

import io.kontak.apps.event.Anomaly
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import java.time.Instant

@Document(indexName = "anomaly")
class AnomalyDocument(
    @Field(name = "reading_id")
    @Id var readingId: String,
    val temperature: Double,
    @Field(name = "room_id")
    val roomId: String,
    @Field(name = "thermometer_id")
    val thermometerId: String,
    val timestamp: Long,
    val anomalyDetector: String,
)

fun Anomaly.toDocument(): AnomalyDocument =
    AnomalyDocument(
        readingId = readingID,
        temperature = temperature,
        roomId = roomId,
        thermometerId = thermometerId,
        timestamp = timestamp.toEpochMilli(),
        anomalyDetector = anomalyDetector
    )

fun AnomalyDocument.toEvent() = Anomaly(
    temperature = temperature,
    roomId = roomId,
    thermometerId = thermometerId,
    timestamp = Instant.ofEpochMilli(timestamp),
    anomalyDetector = anomalyDetector,
    readingID = readingId
)
