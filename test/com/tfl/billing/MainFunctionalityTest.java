package com.tfl.billing;

import org.junit.jupiter.api.Test;
import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.underground.Station;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class MainFunctionalityTest {

    private OysterCard testOysterCardSetup (String id) {
        try {
            return new OysterCard(id);
        }
        catch (UnknownOysterCardException e) {
            return testOysterCardSetup(id);
        }
    }

    private OysterCard myCard = testOysterCardSetup("38400000-8cf0-11bd-b23e-10b96e4ef00d");
    private ExternalLibAdapter adapter = new ExternalLibAdapter();
    private OysterCardReader paddingtonReader = adapter.getCardReader(Station.PADDINGTON);
    private OysterCardReader bakerStreetReader = adapter.getCardReader(Station.BAKER_STREET);
    private OysterCardReader victoriaReader = adapter.getCardReader(Station.VICTORIA_STATION);
    private OysterCardReader eustonReader = adapter.getCardReader(Station.EUSTON);
    private OysterCardReader waterlooReader = adapter.getCardReader(Station.WATERLOO);
    private TravelTracker travelTracker = new TravelTracker();

    @Test
    void currentlyTravelingTest(){
        travelTracker.connect(paddingtonReader, bakerStreetReader);
        paddingtonReader.touch(myCard);
        assertThat(travelTracker.getCurrentlyTraveling().contains(myCard.id()), is(true));
        bakerStreetReader.touch(myCard);
        assertThat(travelTracker.getCurrentlyTraveling().contains(myCard.id()), is(false));
    }


    @Test
    void shortOffPeakCorrectCharge(){
        String startTime = "2017/12/04 14:15:00";
        String endTime = "2017/12/04 14:30:00";
        OysterCard testCard = new OysterCard();
   //     adapter.getCustomers().add


    }

    @Test
    void longOffPeakCorrectCharge(){

    }

    @Test
    void shortPeakCorrectCharge(){

    }

    @Test
    void longPeakCorrectCharge(){

    }

    @Test
    void incompleteJourneyCorrectCharge(){

    }

    private long toMillisSinceEpoch(String time){
        try {
            Date date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(time);
            return date.getTime();
        } catch (ParseException e) {
            System.out.println("Failed to convert string");
        }
    }















}