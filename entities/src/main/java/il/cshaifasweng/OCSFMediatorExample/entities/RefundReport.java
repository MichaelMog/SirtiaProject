package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;

public class RefundReport implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;
    
    private CancelledPurchase refund;

    public CancelledPurchase getRefund() {
        return refund;
    }

    public RefundReport(CancelledPurchase refund) {
        this.refund = refund;
    }
}
