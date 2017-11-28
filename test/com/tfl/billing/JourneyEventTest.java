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
    private JourneyEvent journeyEvent = new JourneyEvent(UUID.fromString("5ebb4720-86fd-435e-bc06-cbf24defbe30"), UUID.fromString("5ecb4720-86fd-435e-bc06-cbf24defbe38")){};
    @Test
    void cardId(){
        assertThat(journeyEvent.cardId(),is(equalTo(UUID.fromString("5ebb4720-86fd-435e-bc06-cbf24defbe30"))));
    }

    @Test
    void readerId() {
        assertThat(journeyEvent.readerId(),is(equalTo(UUID.fromString("5ecb4720-86fd-435e-bc06-cbf24defbe38"))));
    }

    @Test
    void time() {
        assertThat(lessThanOrEqual(journeyEvent.time(), System.currentTimeMillis()),is(true));
    }

    private boolean lessThanOrEqual(long x, long y){
        return x <= y;
    }

}