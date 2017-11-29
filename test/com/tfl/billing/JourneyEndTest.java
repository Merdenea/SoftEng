package com.tfl.billing;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

class JourneyEndTest {

    private UUID cardUUID = UUID.randomUUID();
    private UUID readerUUID = UUID.randomUUID();

    private JourneyEnd journeyEndEvent = new JourneyEnd(cardUUID,readerUUID);


    @Test
    void cardId(){
        assertThat(journeyEndEvent.cardId(), Is.is(equalTo(cardUUID)));
    }

    @Test
    void readerId() {
        assertThat(journeyEndEvent.readerId(), Is.is(equalTo(readerUUID)));
    }

    @Test
    void time() {
        assertThat(lessThanOrEqual(journeyEndEvent.time(), System.currentTimeMillis()), Is.is(true));
    }

    private boolean lessThanOrEqual(long x, long y){
        return x <= y;
    }

}