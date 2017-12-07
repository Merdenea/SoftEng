package com.tfl.billing;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.external.Customer;
import com.tfl.external.CustomerDatabase;
import com.tfl.underground.Station;

import java.math.BigDecimal;
import java.util.List;

public interface ExternalLibInterface {
    List<Customer> getCustomers();

    OysterCardReader getCardReader(Station station);

    void charge(Customer customer, List<Journey> journeys, BigDecimal cost);

    OysterCard getOysterCard(String id);

    CustomerDatabase getCustomerDatabase();

}
