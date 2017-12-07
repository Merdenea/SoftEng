package com.tfl.billing;

import com.oyster.*;
import com.tfl.external.CustomerDatabase;

import java.math.BigDecimal;
import java.util.*;

public class TravelTracker implements ScanListener {

    private List<JourneyEvent> eventLog = new ArrayList<>();
    private Set<UUID> currentlyTravelling = new HashSet<>();
    private PaymentProcessor paymentProcessor;
    private CustomerDatabase customerDatabase;

    /*The constructor takes in the database instance and passes it to the
    * paymentprocessor constructor*/
    public TravelTracker (CustomerDatabase customerDatabase){
        this.customerDatabase = customerDatabase;
        this.paymentProcessor = new PaymentProcessor(customerDatabase);
    }

    public void connect(OysterCardReader... cardReaders) {
        for (OysterCardReader cardReader : cardReaders) {
            cardReader.register(this);
        }
    }

    /*As the external classes can not be changed, we have to leave this method here
      but should not be used
     */
    @Deprecated
    @Override
    public void cardScanned(UUID cardId, UUID readerId){
        if (currentlyTravelling.contains(cardId)) {
            eventLog.add(new JourneyEnd(cardId, readerId));
            currentlyTravelling.remove(cardId);
        } else {
            if (customerDatabase.isRegisteredId(cardId)) {
                currentlyTravelling.add(cardId);
                eventLog.add(new JourneyStart(cardId, readerId));
            } else {
                throw new UnknownOysterCardException(cardId);
            }
        }
    }
  /* This implementation of cardScanned should be used from now on*/
    public void cardScanned(UUID cardId, UUID readerId, long time,boolean isTouchIn){
        if(isTouchIn){
            if (customerDatabase.isRegisteredId(cardId)) {
                currentlyTravelling.add(cardId);
                eventLog.add(new JourneyStart(cardId, readerId, time));
            } else{
                throw new UnknownOysterCardException(cardId);
            }
        } else{
            eventLog.add(new JourneyEnd(cardId, readerId, time));
            if(currentlyTravelling.contains(cardId))
                currentlyTravelling.remove(cardId);
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
