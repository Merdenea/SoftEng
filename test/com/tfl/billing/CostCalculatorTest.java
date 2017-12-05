package com.tfl.billing;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CostCalculatorTest {

    private CostCalculator calculator = CostCalculator.getInstance();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private String offPeakTime = "2017/12/04 12:30:00";
    private String peakTime = "2017/12/04 18:30:00";

    private Journey mockedJourney = mock(Journey.class);

    @Test
    void shortOffPeakJourneyTest() {
        when(mockedJourney.durationSeconds()).thenReturn(10 * 60);
        when(mockedJourney.startTime()).thenReturn(LocalDateTime.parse(offPeakTime, formatter));
        when(mockedJourney.endTime()).thenReturn(LocalDateTime.parse(offPeakTime, formatter));
        List<Journey> list= new ArrayList<>();
        list.add(mockedJourney);
        assertThat(calculator.getCost(list).doubleValue(), is(equalTo(1.60)));
    }

    @Test
    void longOffPeakJourneyTest(){
        when(mockedJourney.durationSeconds()).thenReturn(26 * 60);
        when(mockedJourney.startTime()).thenReturn(LocalDateTime.parse(offPeakTime, formatter));
        when(mockedJourney.endTime()).thenReturn(LocalDateTime.parse(offPeakTime, formatter));
        List<Journey> list= new ArrayList<>();
        list.add(mockedJourney);
        assertThat(calculator.getCost(list).doubleValue(), is(equalTo(2.70)));
    }

    @Test
    void shortPeakTest(){
        when(mockedJourney.durationSeconds()).thenReturn(15 * 60);
        when(mockedJourney.startTime()).thenReturn(LocalDateTime.parse(peakTime, formatter));
        when(mockedJourney.endTime()).thenReturn(LocalDateTime.parse(peakTime, formatter));
        List<Journey> list= new ArrayList<>();
        list.add(mockedJourney);
        assertThat(calculator.getCost(list).doubleValue(), is(equalTo(2.90)));
    }

    @Test
    void longPeakTest(){
        when(mockedJourney.durationSeconds()).thenReturn(30 * 60);
        when(mockedJourney.startTime()).thenReturn(LocalDateTime.parse(peakTime, formatter));
        when(mockedJourney.endTime()).thenReturn(LocalDateTime.parse(peakTime, formatter));
        List<Journey> list= new ArrayList<>();
        list.add(mockedJourney);
        assertThat(calculator.getCost(list).doubleValue(), is(equalTo(3.80)));
    }

}