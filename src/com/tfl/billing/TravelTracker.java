package com.tfl.billing;

import com.oyster.*;
import com.tfl.external.CustomerDatabase;

import java.math.BigDecimal;
import java.util.*;

public class TravelTracker implements ScanListener {

    private List<JourneyEvent> eventLog = new ArrayList<>();
    private Set<UUID> currentlyTravelling = new HashSet<>();
    private CustomerDatabase customerDatabase;
    private PaymentProcessor paymentProcessor;

    public TravelTracker (CustomerDatabase customerDatabase){
        this.customerDatabase = customerDatabase;
        this.paymentProcessor = new PaymentProcessor(customerDatabase);
    }

    public void connect(OysterCardReader... cardReaders) {
        for (OysterCardReader cardReader : cardReaders) {
            cardReader.register(this);
        }
    }

    @Override
    public void cardScanned(UUID cardId, UUID readerId){
        if (currentlyTravelling.contains(cardId)) {
            eventLog.add(new JourneyEnd(cardId, readerId));
            currentlyTravelling.remove(cardId);
        } else {
            if (CustomerDatabase.getInstance().isRegisteredId(cardId)) {
                currentlyTravelling.add(cardId);
                eventLog.add(new JourneyStart(cardId, readerId));
            } else {
                throw new UnknownOysterCardException(cardId);
            }
        }
    }

    public void cardScanned(UUID cardId, UUID readerId, long time, CustomerDatabase customerDatabase){
        if (currentlyTravelling.contains(cardId)) {
            eventLog.add(new JourneyEnd(cardId, readerId, time));
            currentlyTravelling.remove(cardId);
        } else {
            if (customerDatabase.isRegisteredId(cardId)) {
                currentlyTravelling.add(cardId);
                eventLog.add(new JourneyStart(cardId, readerId, time));
            } else {
                throw new UnknownOysterCardException(cardId);
            }
        }

    }

    public void processPayments(){
        paymentProcessor.chargeAccounts(eventLog);
    }

    public BigDecimal getTotalDailyCharges(){
        return paymentProcessor.getTotalDailyCharge();
    }

    public Set getCurrentlyTraveling(){
        return currentlyTravelling;
    }

}
