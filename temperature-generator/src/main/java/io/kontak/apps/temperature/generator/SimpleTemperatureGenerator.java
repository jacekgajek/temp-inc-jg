package io.kontak.apps.temperature.generator;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Component;

import io.kontak.apps.event.TemperatureReading;

@Component
public class SimpleTemperatureGenerator implements TemperatureGenerator {

    private final Random random = new Random();
    private final UUID[] rooms = {
            UUID.fromString("0bb4cc51-cd6d-45d4-84da-769a8eaba8f7"),
            UUID.fromString("b1468547-30da-4f0b-b059-9daac7a799da"),
            UUID.fromString("a8193b62-1925-4124-9d35-3f7a0defe10a"),
            UUID.fromString("0d8b7457-c5be-406b-899a-f7ca5289d277"),
    };
    private final UUID[] thermometers = {
            UUID.fromString("d0f34b1f-d5ac-4767-96dc-17e584c5fed4"),
            UUID.fromString("82bb2a24-6911-4c0c-8aa5-ae129068c3c6"),
            UUID.fromString("cdcda041-d4d8-4b41-b770-19b99ac75b0e"),
            UUID.fromString("37e05302-6163-41ee-be0b-fd6a39fa9ccb"),
            UUID.fromString("1c6ddf20-11ac-4ee5-b343-1b3bc7f99246"),
            UUID.fromString("06fe0ee5-1035-4209-8031-18ab02192357"),
            UUID.fromString("afc6cc56-f34e-443a-ae58-80b5b9555a49"),
    };

    @Override
    public List<TemperatureReading> generate() {
        return List.of(generateSingleReading());
    }

    private TemperatureReading generateSingleReading() {
        //TODO basic implementation, should be changed to the one that will allow to test and demo solution on realistic data
        return new TemperatureReading(UUID.randomUUID().toString(),
                random.nextDouble(10d, 30d),
                rooms[random.nextInt(rooms.length)].toString(),
                thermometers[random.nextInt(thermometers.length)].toString(),
                Instant.now()
        );
    }
}
