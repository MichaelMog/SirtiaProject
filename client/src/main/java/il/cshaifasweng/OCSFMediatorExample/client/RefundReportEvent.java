package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.CancelledPurchase;

import java.util.List;

public class RefundReportEvent {
    private CancelledPurchase refund;

    public CancelledPurchase getRefund() {
        return refund;
    }

    public RefundReportEvent(CancelledPurchase refund) {
        this.refund = refund;
    }
}
