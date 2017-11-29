package com.tfl.billing;

import com.tfl.external.CustomerDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.underground.OysterReaderLocator;
import com.tfl.underground.Station;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class TravelTrackerTest {
    private OysterCard myCard = new OysterCard("38400000-8cf0-11bd-b23e-10b96e4ef00d");
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
        travelTracker.chargeAccounts();
        BigDecimal actualValue = travelTracker.getTotalCharges();
        assertThat(actualValue.doubleValue(), is(equalTo(4.80)));
    }

    @Test
    void connect() throws InterruptedException {

    }

    @Test
    void cardScanned() {
    }

}