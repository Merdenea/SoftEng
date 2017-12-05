package com.tfl.billing;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.math.BigDecimal;
import java.util.*;

public class CostCalculator {
    private final int longJourneyDuration = 25;

    private static final BigDecimal DAILY_PEAK_CAP = new BigDecimal(9.00);
    private static final BigDecimal DAILY_OFF_PEAK_CAP = new BigDecimal(7.00);

    private static final BigDecimal PEAK_LONG = new BigDecimal(3.80);
    private static final BigDecimal PEAK_SHORT = new BigDecimal(2.90);

    private static final BigDecimal OFF_PEAK_LONG = new BigDecimal(2.70);
    private static final BigDecimal OFF_PEAK_SHORT = new BigDecimal(1.60);


    private static CostCalculator instance = new CostCalculator();
    public static CostCalculator getInstance() {
        return instance;
    }
    private CostCalculator(){}


    private boolean isLongJourney(Journey journey){
        return journey.durationSeconds() / 60 >  longJourneyDuration;
    }

    private boolean isPeak(Journey journey){
        return isPeak(journey.startTime().getHour()) ||
                isPeak(journey.endTime().getHour());
    }

    private boolean isPeak (int n){
        return (n >= 6 && n < 10) ||
                (n >= 17 && n < 20);
    }

    private BigDecimal calculateCost(List<Journey> journeys){
        boolean peakTimeTravel = false;
        BigDecimal totalCost = new BigDecimal(0);
        for (Journey journey : journeys){
            if(isPeak(journey)){
                peakTimeTravel = true;
                if (isLongJourney(journey))
                    totalCost = totalCost.add(PEAK_LONG);
                else
                    totalCost = totalCost.add(PEAK_SHORT);
            }
            else{
                if(isLongJourney(journey))
                   totalCost = totalCost.add(OFF_PEAK_LONG);
                else
                   totalCost = totalCost.add(OFF_PEAK_SHORT);
            }
            System.out.println(totalCost);
            System.out.println(peakTimeTravel);
        }
        if (peakTimeTravel){
            if(totalCost.compareTo(DAILY_PEAK_CAP) > 0) {
                totalCost = DAILY_PEAK_CAP;
            }
        }
        else{
            if(totalCost.compareTo(DAILY_OFF_PEAK_CAP) > 0) {
                totalCost = DAILY_OFF_PEAK_CAP;
            }
        }
        return roundToNearestPenny(totalCost);
    }

    private BigDecimal roundToNearestPenny(BigDecimal poundsAndPence) {
        return poundsAndPence.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getCost(List<Journey> journeys){
        return calculateCost(journeys);
    }

}
