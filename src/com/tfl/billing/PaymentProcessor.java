package com.tfl.billing;

import com.tfl.external.Customer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PaymentProcessor {

    //Make the incomplete journey fare equal to the daily peak cap
    private final BigDecimal INCOMPLETE_JOURNEY_FARE = new BigDecimal(9.00);
    private List<JourneyEvent> eventLog;
    private ExternalLibAdapter adapter = new ExternalLibAdapter();
    private BigDecimal totalDailyCharge;

    private PaymentProcessor(){}
    private static PaymentProcessor instance = new PaymentProcessor();
    public static PaymentProcessor getInstance(){
        return instance;
    }

    public void chargeAccounts(List<JourneyEvent> eventLog){
        totalDailyCharge = new BigDecimal(0);
        this.eventLog = eventLog;
        List<Customer> customers = adapter.getCustomers();
        for (Customer customer : customers){
            chargeCustomer(customer);
        }
    }

    private void chargeCustomer(Customer customer){
        BigDecimal customerTotal;
        CostCalculator costCalculator = CostCalculator.getInstance();
        List <Journey> journeys = new ArrayList<>();
        boolean incompleteJourney = false;
        try{
            journeys = createJourneyList(customer);
        }catch (IncompleteJourneyException e){
            incompleteJourney = true;
        }

        if (incompleteJourney) {
            customerTotal = INCOMPLETE_JOURNEY_FARE;
        }
        else{
            customerTotal =  costCalculator.getCost(journeys);
        }
        totalDailyCharge = totalDailyCharge.add(customerTotal);
        adapter.charge(customer, journeys, customerTotal);
    }

    private List<Journey> createJourneyList (Customer customer) throws IncompleteJourneyException{
        List<JourneyEvent> customerJourneyEvents = new ArrayList<JourneyEvent>();
        for (JourneyEvent journeyEvent : eventLog){
            if (journeyEvent.cardId().equals(customer.cardId())){
                customerJourneyEvents.add(journeyEvent);
            }
        }

        List<Journey> journeys = new ArrayList<>();
        JourneyEvent start = null;

        if (customerJourneyEvents.size() % 2 != 0){
            throw new IncompleteJourneyException();
        }
        for (JourneyEvent event : customerJourneyEvents){
            if (event instanceof JourneyStart){
                if (start != null) {
                    throw new IncompleteJourneyException();
                } else {
                    start = event;
                    continue;
                }
            }
            if (event instanceof JourneyEnd && start != null){
                journeys.add(new Journey(start, event));
                start = null;
            }
            if (start != null) {
                throw new IncompleteJourneyException();
            }
        }
        return journeys;
    }

    public BigDecimal getTotalDailyCharge(){
        return totalDailyCharge;
    }
}
