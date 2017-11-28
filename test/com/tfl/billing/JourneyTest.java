package com.tfl.billing;

import com.oyster.OysterCardReader;
import com.tfl.underground.OysterReaderLocator;
import com.tfl.underground.Station;
import jdk.nashorn.internal.scripts.JO;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class JourneyTest {
    private UUID originUUID = UUID.randomUUID();
    private UUID destinationUUID = UUID.randomUUID();
    private UUID custUUID = randomUUID();
    private JourneyStart journeyStart = new JourneyStart(custUUID, originUUID);
    private JourneyEnd journeyEnd = new JourneyEnd(custUUID, destinationUUID);

    private Journey journey = new Journey(journeyStart, journeyEnd);

    @Test
    void originId() {
        assertThat(journey.originId(),is(equalTo(originUUID)));
    }

    @Test
    void destinationId() {
        assertThat(journey.destinationId(),is(equalTo(destinationUUID)));
    }

    @Test
    void formattedStartTime() {

    }

    @Test
    void formattedEndTime() {


    }

    @Test
    void startTime() {
    }

    @Test
    void endTime() {
    }

    @Test
    void durationSeconds() {
    }

    @Test
    void durationMinutes() {
    }



}