package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Purchase;

import java.util.List;

public class TicketReportEvent {
    private List<Purchase> ticketPurchases;

    public List<Purchase> getTicketPurchases() {
        return ticketPurchases;
    }

    public TicketReportEvent(List<Purchase> ticketPurchases) {
        this.ticketPurchases = ticketPurchases;
    }
}
