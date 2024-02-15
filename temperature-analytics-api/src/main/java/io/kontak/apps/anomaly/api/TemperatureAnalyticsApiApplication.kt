package io.kontak.apps.anomaly.api

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication

class TemperatureAnalyticsApiApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(TemperatureAnalyticsApiApplication::class.java, *args)
        }
    }
}
