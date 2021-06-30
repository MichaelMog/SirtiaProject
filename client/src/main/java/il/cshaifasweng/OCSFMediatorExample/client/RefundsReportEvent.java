package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.CancelledPurchases;

import java.util.List;

public class RefundsReportEvent {
    private List<CancelledPurchases> refunds;

    public List<CancelledPurchases> getRefunds() {
        return refunds;
    }

    public RefundsReportEvent(List<CancelledPurchases> refunds) {
        this.refunds = refunds;
    }
}
