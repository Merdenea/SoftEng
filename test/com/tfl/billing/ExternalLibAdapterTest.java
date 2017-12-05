package com.tfl.billing;

import com.oyster.OysterCard;
import com.tfl.external.CustomerDatabase;
import com.tfl.underground.OysterReaderLocator;
import com.tfl.underground.Station;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class ExternalLibAdapterTest {

    ExternalLibAdapter adapter = new ExternalLibAdapter();

    @Test
    void getCustomers() {
        assertThat(adapter.getCustomers(), is(CustomerDatabase.getInstance().getCustomers()));
    }

    @Test
    void getOysterCard() {
        String id = "" + UUID.randomUUID();
        assertThat(adapter.getOysterCard(id) instanceof OysterCard, is(true));
        assertThat(adapter.getOysterCard(id).id(), is(equalTo(UUID.fromString(id))));
    }

    @Test
    void getCardReader() {
        assertThat(adapter.getCardReader(Station.VICTORIA_STATION), is(equalTo(OysterReaderLocator.atStation(Station.VICTORIA_STATION))));
        assertThat(adapter.getCardReader(Station.EUSTON), is(equalTo(OysterReaderLocator.atStation(Station.EUSTON))));
    }
}