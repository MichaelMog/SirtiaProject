package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;

public class SendPriceChangesList implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;
    private List<StagedPriceChange> priceChangeList;

    public List<StagedPriceChange> getPriceChangeList() {
        return priceChangeList;
    }

    public SendPriceChangesList(List<StagedPriceChange> priceChangeList) {
        this.priceChangeList = priceChangeList;
    }
}
