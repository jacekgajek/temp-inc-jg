package io.kontak.apps.anomaly.storage

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class AnomalyStorageApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(AnomalyStorageApplication::class.java, *args)
        }
    }
}
