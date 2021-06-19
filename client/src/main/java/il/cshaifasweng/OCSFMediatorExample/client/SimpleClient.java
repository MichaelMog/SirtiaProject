package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

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
            EventBus.getDefault().post(new SendMovieEvent(movie));
        } else if (msg.getClass().equals(ComingSoonMovie.class)) {
            ComingSoonMovie comingSoonMovie = (ComingSoonMovie) msg;
            EventBus.getDefault().post(new SendMovieEvent(comingSoonMovie));
        } else if (msg.getClass().equals(LinkMovie.class)) {
            LinkMovie linkMovie = (LinkMovie) msg;
            EventBus.getDefault().post(new SendMovieEvent(linkMovie));
        } else if (msg.getClass().equals(Screening.class)) {
            Screening screening = (Screening) msg;
            EventBus.getDefault().post(new SendMovieEvent(screening));
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
