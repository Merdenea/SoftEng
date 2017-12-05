package com.tfl.billing;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.lang.Integer.parseInt;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

class JourneyTest {
    //Seconds to wait for the timed journeys
    private int waitTime = 0;
    public static void wait(int n) {
        try {
            Thread.sleep(n * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private UUID originUUID = UUID.randomUUID();
    private UUID destinationUUID = UUID.randomUUID();
    private UUID custUUID = randomUUID();

    private final Journey createTimedJourney(){

        JourneyStart journeyStart = new JourneyStart(custUUID, originUUID);
        wait(waitTime);
        JourneyEnd journeyEnd = new JourneyEnd(custUUID, destinationUUID);
        return new Journey(journeyStart, journeyEnd);
    }

    JourneyStart journeyStart = new JourneyStart(custUUID, originUUID);
    JourneyEnd journeyEnd = new JourneyEnd(custUUID, destinationUUID);

    private final Journey journey = new Journey(journeyStart, journeyEnd);
    private final Journey timedJourney = createTimedJourney();


    @Test
    void nullOrigin() {
        boolean thrown = false;
        try{
            JourneyStart nullstart = new JourneyStart(custUUID, null);
        } catch (NullArgumentException e){
            thrown = true;
        }
        assertThat(thrown, is(true));
    }

    @Test
    void nullDestination(){
        boolean thrown = false;
        try{
            JourneyEnd nullend = new JourneyEnd(custUUID,null);
        } catch  (NullArgumentException e){
            thrown = true;
        }
        assertThat(thrown, is(true));
    }


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
        assertThat(journey.formattedStartTime() instanceof java.lang.String, is(true));
    }

    @Test
    void startTime() {
        assertThat(journey.startTime() instanceof LocalDateTime, is(true));
    }

    @Test
    void formattedEndTime() {
        assertThat(journey.formattedEndTime() instanceof java.lang.String, is(true));
    }

    @Test
    void endTime() {
        assertThat(journey.endTime() instanceof LocalDateTime, is(true));
    }

    @Test
    void durationSeconds() {
        assertThat(timedJourney.durationSeconds(), is(equalTo(waitTime)));
    }

    @Test
    void durationMinutes() {
        String duration = timedJourney.durationMinutes();
        String[] parts = duration.split(":");
        assertThat(parseInt(parts[0]) * 60 + parseInt(parts[1]), is(equalTo(waitTime)));
    }
}