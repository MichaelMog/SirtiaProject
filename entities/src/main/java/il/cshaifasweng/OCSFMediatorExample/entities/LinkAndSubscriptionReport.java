package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;

public class LinkAndSubscriptionReport implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;
    private List<Purchase> linkPurchases;
    private List<Purchase> subscriptionPurchases;

    public List<Purchase> getLinkPurchases() {
        return linkPurchases;
    }

    public List<Purchase> getSubscriptionPurchases() {
        return subscriptionPurchases;
    }

    public LinkAndSubscriptionReport(List<Purchase> linkPurchases, List<Purchase> subscriptionPurchases) {
        this.linkPurchases = linkPurchases;
        this.subscriptionPurchases = subscriptionPurchases;
    }
}
