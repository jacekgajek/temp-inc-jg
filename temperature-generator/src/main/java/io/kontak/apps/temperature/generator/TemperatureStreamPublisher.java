package io.kontak.apps.temperature.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import io.kontak.apps.event.TemperatureReading;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class TemperatureStreamPublisher {
    private final Logger log = LoggerFactory.getLogger(TemperatureStreamPublisher.class);

    private final Sinks.Many<Message<TemperatureReading>> messageProducer = Sinks.many().multicast().onBackpressureBuffer();

    public Flux<Message<TemperatureReading>> getMessageProducer() {
        return messageProducer.asFlux();
    }

    public void publish(TemperatureReading temperatureReading) {
        log.trace("Emitting reading: {}", temperatureReading);
        messageProducer.tryEmitNext(
                MessageBuilder.withPayload(temperatureReading)
                        .setHeader("identifier", temperatureReading.thermometerId)
                        .build()
        ).orThrow();
    }
}
