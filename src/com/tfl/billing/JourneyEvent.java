package com.tfl.billing;

import java.util.UUID;

public abstract class JourneyEvent {

    private final UUID cardId;
    private final UUID readerId;
    private final long time;

    public JourneyEvent(UUID cardId, UUID readerId) throws NullArgumentException {
        if(readerId != null && cardId != null) {
            this.cardId = cardId;
            this.readerId = readerId;
            this.time = System.currentTimeMillis();
        }
        else {
            throw new NullArgumentException();
        }



    }

    public UUID cardId() {
        return cardId;
    }

    public UUID readerId() {
        return readerId;
    }

    public long time() {
        return time;
    }
}
