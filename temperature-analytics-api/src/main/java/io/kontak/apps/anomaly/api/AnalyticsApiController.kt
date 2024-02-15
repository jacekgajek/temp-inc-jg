package io.kontak.apps.anomaly.api

import io.kontak.apps.event.Anomaly
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class AnalyticsApiController(private val service: AnomalyAnalyticsService) {

    @GetMapping("/thermometers/{id}/anomalies")
    suspend fun getThermometerAnomalies(@PathVariable id: String) : List<Anomaly> {
        return service.getThermometerAnomalies(id)
    }


    @GetMapping("/rooms/{id}/anomalies")
    suspend fun getRoomAnomalies(@PathVariable id: String) : List<Anomaly> {
        return service.getRoomAnomalies(id)
    }

    @GetMapping("/thermometers")
    suspend fun getThermometers(@RequestParam("threshold") anomalyThreshold: Long) : List<ThermometerAnomalies> {
        return service.getThermometers(anomalyThreshold)
    }
}