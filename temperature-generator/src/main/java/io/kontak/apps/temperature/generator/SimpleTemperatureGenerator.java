package io.kontak.apps.temperature.generator;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import io.kontak.apps.event.TemperatureReading;

@Component
public class SimpleTemperatureGenerator implements TemperatureGenerator {

    private final Random random = new Random();
    private final List<Pair<UUID, List<UUID>>> rooms = List.of(
            Pair.of(
                    UUID.fromString("0bb4cc51-cd6d-45d4-84da-769a8eaba8f7"),
                    List.of(UUID.fromString("d0f34b1f-d5ac-4767-96dc-17e584c5fed4"),
                            UUID.fromString("82bb2a24-6911-4c0c-8aa5-ae129068c3c6"))),
            Pair.of(UUID.fromString("b1468547-30da-4f0b-b059-9daac7a799da"),
                    List.of(UUID.fromString("cdcda041-d4d8-4b41-b770-19b99ac75b0e"),
                            UUID.fromString("37e05302-6163-41ee-be0b-fd6a39fa9ccb"),
                            UUID.fromString("1c6ddf20-11ac-4ee5-b343-1b3bc7f99246"))),
            Pair.of(UUID.fromString("a8193b62-1925-4124-9d35-3f7a0defe10a"),
                    List.of(UUID.fromString("06fe0ee5-1035-4209-8031-18ab02192357"))),
            Pair.of(UUID.fromString("0d8b7457-c5be-406b-899a-f7ca5289d277"),
                    List.of(UUID.fromString("afc6cc56-f34e-443a-ae58-80b5b9555a49")))
    );

    @Override
    public List<TemperatureReading> generate() {
        return List.of(generateSingleReading());
    }

    private TemperatureReading generateSingleReading() {
        var room = rooms.get(random.nextInt(rooms.size()));
        var temp = randomTemp();
        return new TemperatureReading(UUID.randomUUID().toString(),
                temp,
                room.getFirst().toString(),
                room.getSecond().get(random.nextInt(room.getSecond().size())).toString(),
                Instant.now()
        );
    }

    private double randomTemp() {
        double x;
        if (random.nextInt() % 5 == 0) {
            x = random.nextDouble(10d, 30d);
        } else {
            x = random.nextDouble(15d, 16d);
        }
        return x;
    }
}
