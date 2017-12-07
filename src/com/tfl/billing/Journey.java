package com.tfl.billing;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Journey {

    private final JourneyEvent start;
    private final JourneyEvent end;

    public Journey(JourneyEvent start, JourneyEvent end) {
        this.start = start;
        this.end = end;
    }

    public UUID originId() {
        return start.readerId();
    }

    public UUID destinationId() {
        return end.readerId();
    }

    public String formattedStartTime() {
        return format(startTime());
    }

    public String formattedEndTime() {
        return format(endTime());
    }

    public LocalDateTime startTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(start.time()), ZoneId.systemDefault());
    }

    public LocalDateTime endTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(end.time()), ZoneId.systemDefault());
    }

    public int durationSeconds() {
        return (int) ((end.time() - start.time()) / 1000);
    }

    public String durationMinutes() {
        return "" + durationSeconds() / 60 + ":" + durationSeconds() % 60;
    }

    private String format(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return time.format(formatter);
    }
}
