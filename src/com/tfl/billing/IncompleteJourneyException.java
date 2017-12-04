package com.tfl.billing;

public class IncompleteJourneyException extends RuntimeException{
    public IncompleteJourneyException(){
        super("Customer did not touch in or out, resulting in an incomplete journey!");
    }
}
