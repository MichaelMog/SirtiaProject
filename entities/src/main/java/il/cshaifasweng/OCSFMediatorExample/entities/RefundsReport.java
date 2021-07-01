package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;

public class RefundsReport implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;
    
    private List<CancelledPurchase> refunds;

    public List<CancelledPurchase> getRefunds() {
        return refunds;
    }

    public RefundsReport(List<CancelledPurchase> refunds) {
        this.refunds = refunds;
    }
}
