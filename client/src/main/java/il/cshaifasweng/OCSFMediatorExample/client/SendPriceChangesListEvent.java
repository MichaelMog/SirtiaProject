package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.StagedPriceChange;

import java.util.List;

public class SendPriceChangesListEvent {
    // Kinda abusing the notation here.
    // Sending this class as a message from the server to the client then bouncing it straight back as an event.
    private List<StagedPriceChange> priceChangeList;

    public List<StagedPriceChange> getPriceChangeList() {
        return priceChangeList;
    }

    public SendPriceChangesListEvent(List<StagedPriceChange> priceChangeList) {
        this.priceChangeList = priceChangeList;
    }
}
