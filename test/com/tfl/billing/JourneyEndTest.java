package com.tfl.billing;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

class JourneyEndTest {

    private UUID cardUUID = UUID.randomUUID();
    private UUID readerUUID = UUID.randomUUID();

    private JourneyEnd journeyEndEvent = new JourneyEnd(cardUUID,readerUUID);


    @Test
    void cardId(){
        assertThat(journeyEndEvent.cardId(),is(equalTo(cardUUID)));
    }

    @Test
    void readerId() {
        assertThat(journeyEndEvent.readerId(), is(equalTo(readerUUID)));
    }

    @Test
    void time() {
        assertThat(lessThanOrEqual(journeyEndEvent.time(), System.currentTimeMillis()), is(true));
    }

    private boolean lessThanOrEqual(long x, long y){
        return x <= y;
    }

}