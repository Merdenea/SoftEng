package com.tfl.billing;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.external.Customer;
import com.tfl.external.CustomerDatabase;
import com.tfl.underground.Station;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class MainFunctionalityTest {
    private OysterCard myCard = new OysterCard("38400000-8cf0-11bd-b23e-10b96e4ef00d");
    private ExternalLibAdapter adapter = new ExternalLibAdapter();
    private OysterCardReader paddingtonReader = adapter.getCardReader(Station.PADDINGTON);
    private OysterCardReader bakerStreetReader = adapter.getCardReader(Station.BAKER_STREET);
    private OysterCardReader victoriaReader = adapter.getCardReader(Station.VICTORIA_STATION);
    private OysterCardReader eustonReader = adapter.getCardReader(Station.EUSTON);
    private OysterCardReader waterlooReader = adapter.getCardReader(Station.WATERLOO);
    //Mock databse to avoid unknownoystercard exceptions
    private CustomerDatabase mockdb = mock(CustomerDatabase.class);
    private TravelTracker travelTracker = new TravelTracker(mockdb);
    private OysterCard testCard = new OysterCard();
    private List<Customer> customersTestList = new ArrayList<>();
    private OysterCard testCard2 = new OysterCard();


    private void setupMockery(){
        customersTestList.add(new Customer("radu@testing", myCard));
        customersTestList.add(new Customer("vlad@testing", testCard));
        when(mockdb.isRegisteredId(testCard.id())).thenReturn(true);
        when(mockdb.isRegisteredId(myCard.id())).thenReturn(true);
        when(mockdb.getCustomers()).thenReturn(customersTestList);
        when(mockdb.isRegisteredId(testCard2.id())).thenReturn(true);
        customersTestList.add(new Customer("vlad@testing2", testCard2));
    }

    @Test
    void currentlyTravelingTest(){
        setupMockery();
        travelTracker.connect(paddingtonReader, bakerStreetReader);
        travelTracker.cardScanned(myCard.id(), paddingtonReader.id(), System.currentTimeMillis(), mockdb);
        assertThat(travelTracker.getCurrentlyTraveling().contains(myCard.id()), is(true));
        travelTracker.cardScanned(myCard.id(), bakerStreetReader.id(), System.currentTimeMillis(), mockdb);
        assertThat(travelTracker.getCurrentlyTraveling().contains(myCard.id()), is(false));
    }

    @Test
    void shortOffPeakCorrectCharge(){
        setupMockery();
        String startTime = "2017/12/04 14:15:00";
        String endTime   = "2017/12/04 14:30:00";
        travelTracker.connect(paddingtonReader, bakerStreetReader);
        travelTracker.cardScanned(testCard.id(), paddingtonReader.id(), toMillisSinceEpoch(startTime), mockdb);
        travelTracker.cardScanned(testCard.id(), bakerStreetReader.id(), toMillisSinceEpoch(endTime), mockdb);
        travelTracker.processPayments();
        assertThat(travelTracker.getTotalDailyCharges().doubleValue(), is(equalTo(1.60)));
    }

    @Test
    void longOffPeakCorrectCharge(){
        setupMockery();
        String startTime = "2017/12/04 14:15:00";
        String endTime   = "2017/12/04 14:45:00";
        travelTracker.connect(paddingtonReader, bakerStreetReader);
        travelTracker.cardScanned(testCard.id(), paddingtonReader.id(), toMillisSinceEpoch(startTime), mockdb);
        travelTracker.cardScanned(testCard.id(), bakerStreetReader.id(), toMillisSinceEpoch(endTime), mockdb);
        travelTracker.processPayments();
        assertThat(travelTracker.getTotalDailyCharges().doubleValue(), is(equalTo(2.70)));
    }

    @Test
    void shortPeakCorrectCharge(){
        setupMockery();
        String startTime = "2017/12/04 18:15:00";
        String endTime   = "2017/12/04 18:35:00";
        travelTracker.connect(paddingtonReader, bakerStreetReader);
        travelTracker.cardScanned(testCard.id(), paddingtonReader.id(), toMillisSinceEpoch(startTime), mockdb);
        travelTracker.cardScanned(testCard.id(), bakerStreetReader.id(), toMillisSinceEpoch(endTime), mockdb);
        travelTracker.processPayments();
        assertThat(travelTracker.getTotalDailyCharges().doubleValue(), is(equalTo(2.90)));
    }

    @Test
    void longPeakCorrectCharge(){
        setupMockery();
        String startTime = "2017/12/04 18:15:00";
        String endTime   = "2017/12/04 18:50:00";
        travelTracker.connect(paddingtonReader, bakerStreetReader);
        travelTracker.cardScanned(testCard.id(), paddingtonReader.id(), toMillisSinceEpoch(startTime), mockdb);
        travelTracker.cardScanned(testCard.id(), bakerStreetReader.id(), toMillisSinceEpoch(endTime), mockdb);
        travelTracker.processPayments();
        assertThat(travelTracker.getTotalDailyCharges().doubleValue(), is(equalTo(3.80)));
    }

    @Test
    void dailyPeakCapCorrectCharge(){
        setupMockery();
        String startTime1 = "2017/12/04 12:01:10";
        String endTime1   = "2017/12/04 12:30:00";
        String startTime2 = "2017/12/04 12:35:00";
        String endTime2   = "2017/12/04 13:02:00";
        String startTime3 = "2017/12/04 13:05:45";
        String endTime3   = "2017/12/04 14:02:12";
        String startTime4 = "2017/12/04 17:02:01";
        String endTime4   = "2017/12/04 17:35:09";
        travelTracker.connect(paddingtonReader, bakerStreetReader, victoriaReader, eustonReader, waterlooReader);
        travelTracker.cardScanned(testCard.id(), paddingtonReader.id(), toMillisSinceEpoch(startTime1), mockdb);
        travelTracker.cardScanned(testCard.id(), eustonReader.id(), toMillisSinceEpoch(endTime1), mockdb);
        travelTracker.cardScanned(testCard.id(), bakerStreetReader.id(), toMillisSinceEpoch(startTime2), mockdb);
        travelTracker.cardScanned(testCard.id(), victoriaReader.id(), toMillisSinceEpoch(endTime2), mockdb);
        travelTracker.cardScanned(testCard.id(), eustonReader.id(), toMillisSinceEpoch(startTime3), mockdb);
        travelTracker.cardScanned(testCard.id(), waterlooReader.id(), toMillisSinceEpoch(endTime3), mockdb);
        travelTracker.cardScanned(testCard.id(), paddingtonReader.id(), toMillisSinceEpoch(startTime4), mockdb);
        travelTracker.cardScanned(testCard.id(), waterlooReader.id(), toMillisSinceEpoch(endTime4), mockdb);
        travelTracker.processPayments();
        assertThat(travelTracker.getTotalDailyCharges().doubleValue(), is(equalTo(9.00)));
    }


    @Test
    void dailyOffPeakCapCorrectCharge(){
        setupMockery();
        String startTime1 = "2017/12/04 12:01:10";
        String endTime1   = "2017/12/04 12:30:00";
        String startTime2 = "2017/12/04 12:35:00";
        String endTime2   = "2017/12/04 13:02:00";
        String startTime3 = "2017/12/04 13:05:45";
        String endTime3   = "2017/12/04 14:02:12";
        travelTracker.connect(paddingtonReader, bakerStreetReader, victoriaReader, eustonReader, waterlooReader);
        travelTracker.cardScanned(testCard.id(), paddingtonReader.id(), toMillisSinceEpoch(startTime1), mockdb);
        travelTracker.cardScanned(testCard.id(), eustonReader.id(), toMillisSinceEpoch(endTime1), mockdb);
        travelTracker.cardScanned(testCard.id(), bakerStreetReader.id(), toMillisSinceEpoch(startTime2), mockdb);
        travelTracker.cardScanned(testCard.id(), victoriaReader.id(), toMillisSinceEpoch(endTime2), mockdb);
        travelTracker.cardScanned(testCard.id(), eustonReader.id(), toMillisSinceEpoch(startTime3), mockdb);
        travelTracker.cardScanned(testCard.id(), waterlooReader.id(), toMillisSinceEpoch(endTime3), mockdb);
        travelTracker.processPayments();
        assertThat(travelTracker.getTotalDailyCharges().doubleValue(), is(equalTo(7.00)));
    }


    @Test
    void incompleteJourneyCorrectCharge(){
        setupMockery();
        travelTracker.connect(victoriaReader, eustonReader, waterlooReader);
        travelTracker.cardScanned(testCard.id(), victoriaReader.id(), System.currentTimeMillis(), mockdb);
        travelTracker.cardScanned(testCard.id(), eustonReader.id(), System.currentTimeMillis(), mockdb);
        travelTracker.cardScanned(testCard.id(), waterlooReader.id(), System.currentTimeMillis(), mockdb);
        travelTracker.processPayments();
        assertThat(travelTracker.getTotalDailyCharges().doubleValue(), is(equalTo(9.00)));
    }

    @Test
    void multipleCustomersTest(){
        setupMockery();
        String startTime1 = "2017/12/04 12:01:10";
        String endTime1   = "2017/12/04 12:30:00";
        String startTime2 = "2017/12/04 12:15:00";
        String endTime2   = "2017/12/04 12:28:00";
        travelTracker.connect(paddingtonReader, bakerStreetReader, victoriaReader, eustonReader);
        travelTracker.cardScanned(testCard2.id(), paddingtonReader.id(), toMillisSinceEpoch(startTime1), mockdb);
        travelTracker.cardScanned(testCard.id(), victoriaReader.id(), toMillisSinceEpoch(startTime2), mockdb);
        travelTracker.cardScanned(testCard.id(), paddingtonReader.id(), toMillisSinceEpoch(endTime2), mockdb);
        travelTracker.cardScanned(testCard2.id(), eustonReader.id(), toMillisSinceEpoch(endTime1), mockdb);
        travelTracker.processPayments();
        assertThat(travelTracker.getTotalDailyCharges().doubleValue(), is(equalTo(4.30)));
    }

    private long toMillisSinceEpoch(String time){
        try {
            Date date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(time);
            return date.getTime();
        } catch (ParseException e) {
            System.out.println("Failed to convert string");
            return 0;
        }
    }
}