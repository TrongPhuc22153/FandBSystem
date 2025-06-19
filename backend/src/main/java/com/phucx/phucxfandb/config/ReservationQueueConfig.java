package com.phucx.phucxfandb.config;

import com.phucx.phucxfandb.entity.Reservation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

@Configuration
public class ReservationQueueConfig {
    @Bean
    public ConcurrentSkipListMap<LocalTime, List<Reservation>> reservationQueue() {
        return new ConcurrentSkipListMap<>();
    }
}
