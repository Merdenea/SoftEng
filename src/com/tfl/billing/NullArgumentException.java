package com.tfl.billing;

public class NullArgumentException extends RuntimeException {
    public NullArgumentException(){
        super("You cannot create a journey with null ID");
    }
}
