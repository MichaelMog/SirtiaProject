package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.MovieTitle;
import il.cshaifasweng.OCSFMediatorExample.entities.Subscription;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;

public class SimpleClient extends AbstractClient {

    private static SimpleClient client = null;

    private SimpleClient(String host, int port) {
        super(host, port);
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        if (msg.getClass().equals(Warning.class)) {
            EventBus.getDefault().post(new WarningEvent((Warning) msg));
        } else if (msg.getClass().equals(MovieTitle.class)) {
            MovieTitle movie = (MovieTitle) msg;
            EventBus.getDefault().post(new MovieTitleEvent(movie));
        } else if (msg.getClass().equals(Subscription.class)){
            Subscription sub = (Subscription) msg;
            EventBus.getDefault().post(new SubscriptionEvent(sub));
        }

    }

    public static SimpleClient getClient() {
        if (client == null) {
            client = new SimpleClient("localhost", 3000);
        }
        return client;
    }

}
