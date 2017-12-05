package com.tfl.billing;

import com.tfl.external.Customer;
import org.junit.jupiter.api.Test;
import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.underground.Station;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


class MainFunctionalityTest {

    private OysterCard myCard = new OysterCard("38400000-8cf0-11bd-b23e-10b96e4ef00d");
    private ExternalLibAdapter adapter = new ExternalLibAdapter();
    private OysterCardReader paddingtonReader = adapter.getCardReader(Station.PADDINGTON);
    private OysterCardReader bakerStreetReader = adapter.getCardReader(Station.BAKER_STREET);
    private OysterCardReader victoriaReader = adapter.getCardReader(Station.VICTORIA_STATION);
    private OysterCardReader eustonReader = adapter.getCardReader(Station.EUSTON);
    private OysterCardReader waterlooReader = adapter.getCardReader(Station.WATERLOO);
    private TravelTracker travelTracker = new TravelTracker();
    private OysterCard testCard = new OysterCard();

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
        adapter.getCustomers().clear();
        adapter.getCustomers().add(new Customer("vlad@testing", testCard));
        travelTracker.connect(paddingtonReader, bakerStreetReader);
        travelTracker.cardScanned(testCard.id(), paddingtonReader.id(), toMillisSinceEpoch(startTime));
        travelTracker.cardScanned(testCard.id(), bakerStreetReader.id(), toMillisSinceEpoch(endTime));
        travelTracker.processPayments();
        assertThat(travelTracker.getTotalDailyCharges().doubleValue(), is(equalTo(1.60)));
    }

    @Test
    void longOffPeakCorrectCharge(){
        String startTime = "2017/12/04 14:15:00";
        String endTime = "2017/12/04 14:45:00";
        adapter.getCustomers().clear();
        adapter.getCustomers().add(new Customer("vlad@testing", testCard));
        travelTracker.connect(paddingtonReader, bakerStreetReader);
        travelTracker.cardScanned(testCard.id(), paddingtonReader.id(), toMillisSinceEpoch(startTime));
        travelTracker.cardScanned(testCard.id(), bakerStreetReader.id(), toMillisSinceEpoch(endTime));
        travelTracker.processPayments();
        assertThat(travelTracker.getTotalDailyCharges().doubleValue(), is(equalTo(2.70)));
    }

    @Test
    void shortPeakCorrectCharge(){
        String startTime = "2017/12/04 18:15:00";
        String endTime = "2017/12/04 18:35:00";
        adapter.getCustomers().clear();
        adapter.getCustomers().add(new Customer("vlad@testing", testCard));
        travelTracker.connect(paddingtonReader, bakerStreetReader);
        travelTracker.cardScanned(testCard.id(), paddingtonReader.id(), toMillisSinceEpoch(startTime));
        travelTracker.cardScanned(testCard.id(), bakerStreetReader.id(), toMillisSinceEpoch(endTime));
        travelTracker.processPayments();
        assertThat(travelTracker.getTotalDailyCharges().doubleValue(), is(equalTo(2.90)));
    }

    @Test
    void longPeakCorrectCharge(){
        String startTime = "2017/12/04 18:15:00";
        String endTime = "2017/12/04 18:50:00";
        adapter.getCustomers().clear();
        adapter.getCustomers().add(new Customer("vlad@testing", testCard));
        travelTracker.connect(paddingtonReader, bakerStreetReader);
        travelTracker.cardScanned(testCard.id(), paddingtonReader.id(), toMillisSinceEpoch(startTime));
        travelTracker.cardScanned(testCard.id(), bakerStreetReader.id(), toMillisSinceEpoch(endTime));
        travelTracker.processPayments();
        assertThat(travelTracker.getTotalDailyCharges().doubleValue(), is(equalTo(3.80)));
    }

    @Test
    void dailyOffPeakCapCorrectCharge(){
        String startTime1 = "2017/12/04 12:01:10";
        String endTime1 = "2017/12/04 12:30:00";
        String startTime2 = "2017/12/04 12:35:00";
        String endTime2 = "2017/12/04 13:02:00";
        String startTime3 = "2017/12/04 13:05:45";
        String endTime3 = "2017/12/04 14:02:12";
        adapter.getCustomers().clear();
        adapter.getCustomers().add(new Customer("vlad@testing", testCard));
        travelTracker.connect(paddingtonReader, bakerStreetReader, victoriaReader, eustonReader, waterlooReader);
        travelTracker.cardScanned(testCard.id(), paddingtonReader.id(), toMillisSinceEpoch(startTime1));
        travelTracker.cardScanned(testCard.id(), eustonReader.id(), toMillisSinceEpoch(endTime1));
        travelTracker.cardScanned(testCard.id(), bakerStreetReader.id(), toMillisSinceEpoch(startTime2));
        travelTracker.cardScanned(testCard.id(), victoriaReader.id(), toMillisSinceEpoch(endTime2));
        travelTracker.cardScanned(testCard.id(), eustonReader.id(), toMillisSinceEpoch(startTime3));
        travelTracker.cardScanned(testCard.id(), waterlooReader.id(), toMillisSinceEpoch(endTime3));
        travelTracker.processPayments();
        assertThat(travelTracker.getTotalDailyCharges().doubleValue(), is(equalTo(7.00)));
    }

    @Test
    void dailyPeakCapCorrectCharge(){
        String startTime1 = "2017/12/04 12:01:10";
        String endTime1 = "2017/12/04 12:30:00";
        String startTime2 = "2017/12/04 12:35:00";
        String endTime2 = "2017/12/04 13:02:00";
        String startTime3 = "2017/12/04 13:05:45";
        String endTime3 = "2017/12/04 14:02:12";
        String startTime4 = "2017/12/04 17:02:01";
        String endTime4 = "2017/12/04 17.35:09";
        adapter.getCustomers().clear();
        adapter.getCustomers().add(new Customer("vlad@testing", testCard));
        travelTracker.connect(paddingtonReader, bakerStreetReader, victoriaReader, eustonReader, waterlooReader);
        travelTracker.cardScanned(testCard.id(), paddingtonReader.id(), toMillisSinceEpoch(startTime1));
        travelTracker.cardScanned(testCard.id(), eustonReader.id(), toMillisSinceEpoch(endTime1));
        travelTracker.cardScanned(testCard.id(), bakerStreetReader.id(), toMillisSinceEpoch(startTime2));
        travelTracker.cardScanned(testCard.id(), victoriaReader.id(), toMillisSinceEpoch(endTime2));
        travelTracker.cardScanned(testCard.id(), eustonReader.id(), toMillisSinceEpoch(startTime3));
        travelTracker.cardScanned(testCard.id(), waterlooReader.id(), toMillisSinceEpoch(endTime3));
        travelTracker.cardScanned(testCard.id(), paddingtonReader.id(), toMillisSinceEpoch(startTime4));
        travelTracker.cardScanned(testCard.id(), waterlooReader.id(), toMillisSinceEpoch(endTime4));
        travelTracker.processPayments();
        assertThat(travelTracker.getTotalDailyCharges().doubleValue(), is(equalTo(9.00)));
    }

    @Test
    void incompleteJourneyCorrectCharge(){
        travelTracker.connect(victoriaReader, eustonReader, waterlooReader);
        victoriaReader.touch(myCard);
        eustonReader.touch(myCard);
        waterlooReader.touch(myCard);
        travelTracker.processPayments();
        assertThat(travelTracker.getTotalDailyCharges().doubleValue(), is(equalTo(9.00)));
    }

    private long toMillisSinceEpoch(String time){
        try {
            Date date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(time);
            return date.getTime();
        } catch (ParseException e) {
            System.out.println("Failed to convert string");
        }
        return 0;
    }















}