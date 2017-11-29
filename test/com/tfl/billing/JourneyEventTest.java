package com.tfl.billing;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class JourneyEventTest {

    private UUID cardUUID = UUID.randomUUID();
    private UUID readerUUID = UUID.randomUUID();


    private JourneyEvent journeyEvent = new JourneyEvent(cardUUID, readerUUID){};

    @Test
    void cardId(){
        assertThat(journeyEvent.cardId(),is(equalTo(cardUUID)));
    }

    @Test
    void readerId() {
        assertThat(journeyEvent.readerId(),is(equalTo(readerUUID)));
    }

    @Test
    void time() {

        assertThat(lessThanOrEqual(journeyEvent.time(), System.currentTimeMillis()),is(true));
    }

    private boolean lessThanOrEqual(long x, long y){
        return x <= y;
    }

}