package com.tfl.billing;

import java.util.UUID;

public class JourneyEnd extends JourneyEvent {

    public JourneyEnd(UUID cardId, UUID readerId)throws NullArgumentException {
        super(cardId, readerId);
    }

    public JourneyEnd(UUID cardId, UUID readerId, long time)throws NullArgumentException {
        super(cardId, readerId, time);
    }
}
