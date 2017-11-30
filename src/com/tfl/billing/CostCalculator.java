package com.tfl.billing;

import java.math.BigDecimal;
import java.time.Period;
import java.util.*;
import java.time.LocalTime;

public class CostCalculator {
    private BigDecimal totalCost = new BigDecimal(0);
    private final int longJourneyDuration = 25;
    private List<Journey> journeys;
    boolean peakTimeTravel = false;

    private static final BigDecimal DAILY_PEAK_CAP = new BigDecimal(9.00);
    private static final BigDecimal DAILY_OFF_PEAK_CAP = new BigDecimal(7.00);

    private static final BigDecimal PEAK_LONG = new BigDecimal(3.80);
    private static final BigDecimal PEAK_SHORT = new BigDecimal(2.90);

    private static final BigDecimal OFF_PEAK_LONG = new BigDecimal(2.70);
    private static final BigDecimal OFF_PEAK_SHORT = new BigDecimal(1.60);

    public CostCalculator(List<Journey> journeys){
        this.journeys = journeys;
    }
    //make public for testing
    private boolean isLongJouney(Journey journey){
        return journey.durationSeconds() / 60 >  longJourneyDuration;
    }
    //same here
    private boolean isPeak(Journey journey){
        return isPeak(journey.startTime().getHour()) ||
                isPeak(journey.endTime().getHour());
    }

    private boolean isPeak (int n){
        return (n >= 6 && n < 10) ||
                (n >= 17 && n < 20);
    }
    //maybe change to one??>

    private void calculateCost(){
        for (Journey journey : journeys){
            if(isPeak(journey)){
                peakTimeTravel = true;
                if (isLongJouney(journey))
                    totalCost = totalCost.add(PEAK_LONG);
                else
                    totalCost = totalCost.add(PEAK_SHORT);
            }
            else{
                if(isLongJouney(journey))
                   totalCost = totalCost.add(OFF_PEAK_LONG);
                else
                   totalCost = totalCost.add(OFF_PEAK_SHORT);
            }
        }
        if (peakTimeTravel){
            if(totalCost.compareTo(DAILY_PEAK_CAP) > 0)
                totalCost = DAILY_PEAK_CAP;
        }
        else{
            if(totalCost.compareTo(DAILY_OFF_PEAK_CAP) > 0)
                totalCost = DAILY_OFF_PEAK_CAP;
        }
    }

    public BigDecimal getCost(){
        calculateCost();
        return totalCost;
    }

}
