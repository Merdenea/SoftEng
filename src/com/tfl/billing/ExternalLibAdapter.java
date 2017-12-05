package com.tfl.billing;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.external.Customer;
import com.tfl.external.CustomerDatabase;
import com.tfl.external.PaymentsSystem;
import com.tfl.underground.OysterReaderLocator;
import com.tfl.underground.Station;

import java.math.BigDecimal;
import java.util.List;

public class ExternalLibAdapter implements CustomerLibInterface {


    @Override
    public List<Customer> getCustomers() {
        CustomerDatabase customerDatabase = CustomerDatabase.getInstance();
        List<Customer> customers = customerDatabase.getCustomers();
        return customers;
    }
    @Override
    public OysterCard getOysterCard(String id) {
        OysterCard oysterCard = new OysterCard(id);
        return oysterCard;
    }

    @Override
    public OysterCardReader getCardReader(Station station) {
        OysterCardReader reader =  OysterReaderLocator.atStation(station);
        return reader;
    }

    @Override
    public void charge(Customer customer, List<Journey> journeys, BigDecimal cost) {
        PaymentsSystem.getInstance().charge(customer, journeys, cost);
    }

    @Override
    public CustomerDatabase getCustomerDatabase() {
        return CustomerDatabase.getInstance();
    }
}
