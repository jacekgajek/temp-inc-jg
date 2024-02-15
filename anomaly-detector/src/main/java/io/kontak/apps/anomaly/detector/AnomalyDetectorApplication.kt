package io.kontak.apps.anomaly.detector

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class AnomalyDetectorApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(AnomalyDetectorApplication::class.java, *args)
        }
    }
}
