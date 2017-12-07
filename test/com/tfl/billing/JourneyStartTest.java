package com.tfl.billing;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

class JourneyStartTest {
    private UUID cardUUID = UUID.randomUUID();
    private UUID readerUUID = UUID.randomUUID();

    private JourneyStart journeyStartEvent = new JourneyStart(cardUUID,readerUUID);

    @Test
    void cardId(){
        assertThat(journeyStartEvent.cardId(), is(equalTo(cardUUID)));
    }

    @Test
    void readerId() {
        assertThat(journeyStartEvent.readerId(), is(equalTo(readerUUID))); }

    @Test
    void time() {
        assertThat(lessThanOrEqual(journeyStartEvent.time(), System.currentTimeMillis()), is(true));
    }

    private boolean lessThanOrEqual(long x, long y){
        return x <= y;
    }

}