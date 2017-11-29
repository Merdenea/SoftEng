package com.tfl.billing;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class JourneyStartTest {
    private UUID cardUUID = UUID.randomUUID();
    private UUID readerUUID = UUID.randomUUID();

    private JourneyStart journeyStartEvent = new JourneyStart(cardUUID,readerUUID);


    @Test
    void cardId(){
        assertThat(journeyStartEvent.cardId(), Is.is(equalTo(cardUUID)));
    }

    @Test
    void readerId() {
        assertThat(journeyStartEvent.readerId(), Is.is(equalTo(readerUUID))); }

    @Test
    void time() {
        assertThat(lessThanOrEqual(journeyStartEvent.time(), System.currentTimeMillis()), Is.is(true));
    }

    private boolean lessThanOrEqual(long x, long y){
        return x <= y;
    }

}