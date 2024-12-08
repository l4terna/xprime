package com.laterna.xaxathonprime._shared.loader;

import com.github.javafaker.Faker;
import com.laterna.xaxathonprime.event.Event;
import com.laterna.xaxathonprime.event.EventService;
import com.laterna.xaxathonprime.eventbase.EventBase;
import com.laterna.xaxathonprime.eventbase.EventBaseMapper;
import com.laterna.xaxathonprime.eventbase.EventBaseService;
import com.laterna.xaxathonprime.region.Region;
import com.laterna.xaxathonprime.region.RegionMapper;
import com.laterna.xaxathonprime.region.RegionService;
import com.laterna.xaxathonprime.region.dto.RegionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class TestDataLoader implements CommandLineRunner {
    
    private final RegionService regionService;
    private final EventBaseService eventBaseService;
    private final EventService eventService;
    private final EventBaseMapper eventBaseMapper;
    private final RegionMapper regionMapper;

    @Transactional
    @Override
    public void run(String... args) {
        Faker faker = new Faker(new Locale("ru"));

        List<RegionDto> regions = regionService.findAll();
        if (regions.isEmpty()) {
            throw new RuntimeException("No regions found in database");
        }

        // Создаем EventBase записи, случайно выбирая регион для каждого
        List<EventBase> eventBases = IntStream.range(0, 300)
                .mapToObj(i -> createEventBase(faker, getRandomRegion(regions, faker)))
                .map(r -> eventBaseMapper.toEntity(eventBaseService.save(r)))
                .toList();

        // Создаем Events для каждого EventBase
        eventBases.forEach(this::createEvent);
    }


    private RegionDto getRandomRegion(List<RegionDto> regions, Faker faker) {
        return regions.get(faker.number().numberBetween(0, regions.size()));
    }
    
    private EventBase createEventBase(Faker faker, RegionDto region) {
        LocalDateTime startDate = LocalDateTime.now().plusDays(faker.number().numberBetween(1, 365));
        
        return EventBase.builder()
            .name(generateEventName(faker))
            .gender(faker.options().option("M", "F", "ALL"))
            .minAge(faker.number().numberBetween(6, 18))
            .maxAge(faker.number().numberBetween(19, 65))
            .location(generateLocation(faker))
            .startDate(startDate)
            .endDate(startDate.plusDays(faker.number().numberBetween(1, 7)))
            .maxParticipants(faker.number().numberBetween(50, 500))
            .region(regionMapper.toEntity(region))
            .build();
    }
    
    private String generateEventName(Faker faker) {
        String[] prefixes = {"Чемпионат", "Кубок", "Турнир", "Соревнования"};
        String[] sports = {"по легкой атлетике", "по плаванию", "по лыжным гонкам", "по биатлону"};
        
        return faker.options().option(prefixes) + " " + 
               faker.address().city() + " " +
               faker.options().option(sports);
    }
    
    private String generateLocation(Faker faker) {
        return String.format("%s, %s, %s",
            faker.address().city(),
            faker.address().streetName(),
            faker.address().buildingNumber()
        );
    }
    
    private void createEvent(EventBase eventBase) {
        Event event = Event.builder()
            .base(eventBase)
            .build();
        eventService.save(event);
    }
}