package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;

public class TicketReport implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;

    private List<Purchase> ticketPurchases;

    public List<Purchase> getTicketPurchases() {
        return ticketPurchases;
    }

    public TicketReport(List<Purchase> ticketPurchases) {
        this.ticketPurchases = ticketPurchases;
    }
}
