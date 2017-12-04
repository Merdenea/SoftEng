package com.tfl.billing;

import com.tfl.external.Customer;
import com.tfl.external.CustomerDatabase;
import com.tfl.external.PaymentsSystem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PaymentProcessor {

    private final List<JourneyEvent> eventLog;
    private ExternalDatabaseAdapter adapter = new ExternalDatabaseAdapter();

    public PaymentProcessor(List<JourneyEvent> eventLog){
        this.eventLog = eventLog;
    }

    public void chargeAccounts(){
        List<Customer> customers = adapter.getCustomer();
        for (Customer customer : customers){
            chargeCustomer(customer);
        }
    }

    private void chargeCustomer(Customer customer){
        List<JourneyEvent> customerJourneyEvents = new ArrayList<JourneyEvent>();
        for (JourneyEvent journeyEvent : eventLog){
            if (journeyEvent.cardId().equals(customer.cardId())){
                customerJourneyEvents.add(journeyEvent);
            }
        }

        List<Journey> journeys = new ArrayList<>();
        JourneyEvent start = null;

        for (JourneyEvent event : customerJourneyEvents){
            if (event instanceof JourneyStart){
                start = event;
            }
            if (event instanceof JourneyEnd && start != null){
                journeys.add(new Journey(start, event));
                start = null;
            }
        }

        CostCalculator costCalculator = CostCalculator.getInstance();
        BigDecimal customerTotal = costCalculator.getCost(journeys);
        adapter.charge(customer, journeys, customerTotal);
    }
}
