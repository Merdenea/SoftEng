package com.tfl.billing;

import java.util.UUID;

public class JourneyStart extends JourneyEvent {

    public JourneyStart(UUID cardId, UUID readerId) throws NullArgumentException {
        super(cardId, readerId);
    }


    public JourneyStart(UUID cardId, UUID readerId, long time) throws NullArgumentException {
        super(cardId, readerId, time);
    }

}
