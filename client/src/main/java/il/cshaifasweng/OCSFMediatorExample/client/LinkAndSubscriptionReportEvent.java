package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Purchase;

import java.util.List;

public class LinkAndSubscriptionReportEvent {
    private List<Purchase> linkPurchases;
    private List<Purchase> subscriptionPurchases;

    public List<Purchase> getLinkPurchases() {
        return linkPurchases;
    }

    public List<Purchase> getSubscriptionPurchases() {
        return subscriptionPurchases;
    }

    public LinkAndSubscriptionReportEvent(List<Purchase> linkPurchases, List<Purchase> subscriptionPurchases) {
        this.linkPurchases = linkPurchases;
        this.subscriptionPurchases = subscriptionPurchases;
    }
}
