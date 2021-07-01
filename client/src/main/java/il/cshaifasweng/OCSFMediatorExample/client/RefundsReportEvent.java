package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.CancelledPurchase;

import java.util.List;

public class RefundsReportEvent {
    private List<CancelledPurchase> refunds;

    public List<CancelledPurchase> getRefunds() {
        return refunds;
    }

    public RefundsReportEvent(List<CancelledPurchase> refunds) {
        this.refunds = refunds;
    }
}
