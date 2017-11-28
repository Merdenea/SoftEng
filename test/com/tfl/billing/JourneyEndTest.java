package com.tfl.billing;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

class JourneyEndTest {

    private JourneyEnd journeyEndEvent = new JourneyEnd(UUID.fromString("2cdf6bca-010d-482f-9e78-b8a52bff7515"), UUID.fromString("e2f35f09-2f25-401c-a375-8c045c1d1c92"));


    @Test
    void cardId(){
        assertThat(journeyEndEvent.cardId(), Is.is(equalTo(UUID.fromString("2cdf6bca-010d-482f-9e78-b8a52bff7515"))));
    }

    @Test
    void readerId() {
        assertThat(journeyEndEvent.readerId(), Is.is(equalTo(UUID.fromString("e2f35f09-2f25-401c-a375-8c045c1d1c92"))));
    }

    @Test
    void time() {
        assertThat(lessThanOrEqual(journeyEndEvent.time(), System.currentTimeMillis()), Is.is(true));
    }

    private boolean lessThanOrEqual(long x, long y){
        return x <= y;
    }

}