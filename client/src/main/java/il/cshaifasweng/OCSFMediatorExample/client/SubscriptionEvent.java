package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Subscription;

public class SubscriptionEvent {

    private Subscription subscription;

    public Subscription getSubscription() {
        return subscription;
    }

    public SubscriptionEvent(Subscription subscription) {
        this.subscription = subscription;
    }
}
