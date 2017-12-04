package com.tfl.billing;

import org.junit.jupiter.api.Test;
import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.underground.OysterReaderLocator;
import com.tfl.underground.Station;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class TravelTrackerTest {
    private OysterCard testSetup() {
        try {
            OysterCard myCard = new OysterCard("38400000-8cf0-11bd-b23e-10b96e4ef00d");
            return myCard;
        } catch (UnknownOysterCardException e) {
            return testSetup();
        }
    }

    private OysterCard myCard = testSetup();
    private OysterCardReader paddingtonReader = OysterReaderLocator.atStation(Station.PADDINGTON);
    private OysterCardReader bakerStreetReader = OysterReaderLocator.atStation(Station.BAKER_STREET);
    private OysterCardReader kingsCrossReader = OysterReaderLocator.atStation(Station.KINGS_CROSS);
    private TravelTracker travelTracker = new TravelTracker();


    @Test
    void totalCharges() throws InterruptedException {
        travelTracker.connect(paddingtonReader, bakerStreetReader, kingsCrossReader);
        paddingtonReader.touch(myCard);
        bakerStreetReader.touch(myCard);
        bakerStreetReader.touch(myCard);
        kingsCrossReader.touch(myCard);
        bakerStreetReader.touch(myCard);
        kingsCrossReader.touch(myCard);
        bakerStreetReader.touch(myCard);
        kingsCrossReader.touch(myCard);
        travelTracker.processPayments();
        assertThat(4, is(equalTo(4.80)));
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