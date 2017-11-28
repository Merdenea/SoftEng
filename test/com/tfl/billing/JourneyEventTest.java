package com.tfl.billing;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class JourneyEventTest {
    private JourneyEvent event = new JourneyEvent(UUID.fromString("5ebb4720-86fd-435e-bc06-cbf24defbe38"), UUID.fromString("5ecb4720-86fd-435e-bc06-cbf24defbe38")){};
    @Test
    void cardId() {
        assertThat(event.cardId(),is(equalTo(UUID.fromString("5ebb4720-86fd-435e-bc06-cbf24defbe38"))));
    }

    @Test
    void readerId() {
        assertThat(event.readerId(),is(equalTo(UUID.fromString("5ecb4720-86fd-435e-bc06-cbf24defbe38"))));
    }

    @Test
    void time() {
        assertThat(event.time(),is(equalTo(System.currentTimeMillis())));
    }

}