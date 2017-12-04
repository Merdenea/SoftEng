package com.tfl.billing;

import org.junit.jupiter.api.Test;
import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.underground.OysterReaderLocator;
import com.tfl.underground.Station;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoField.EPOCH_DAY;
import static java.time.temporal.ChronoField.ERA;
import static java.time.temporal.ChronoField.MILLI_OF_DAY;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class TravelTrackerTest {
    private OysterCard testSetup() {
        try {
             return new OysterCard("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        } catch (UnknownOysterCardException e) {
            return testSetup();
        }
    }

    private OysterCard myCard = testSetup();
    private ExternalDatabaseAdapter adapter = new ExternalDatabaseAdapter();
    private OysterCardReader paddingtonReader = adapter.getCardReader(Station.PADDINGTON);
    private OysterCardReader bakerStreetReader = adapter.getCardReader(Station.BAKER_STREET);
    private OysterCardReader kingsCrossReader = adapter.getCardReader(Station.KINGS_CROSS);
    private TravelTracker travelTracker = new TravelTracker();


    @Test
    void totalCharges() throws InterruptedException {
        travelTracker.connect(paddingtonReader, bakerStreetReader, kingsCrossReader);
        paddingtonReader.touch(myCard);
        bakerStreetReader.touch(myCard);
        paddingtonReader.touch(myCard);
        bakerStreetReader.touch(myCard);
        travelTracker.processPayments();

        //assertThat(4, is(equalTo(4.80)));
    }

    @Test
    void currentlyTraveling() throws InterruptedException {
        travelTracker.connect(paddingtonReader, bakerStreetReader);
        paddingtonReader.touch(myCard);
        assertThat(travelTracker.getCurrentlyTraveling().contains(myCard.id()), is(true));
        bakerStreetReader.touch(myCard);
        //travelTracker.chargeAccounts();
        assertThat(travelTracker.getCurrentlyTraveling().contains(myCard.id()), is(false));
    }
}