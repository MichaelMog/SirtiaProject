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
        } else if (msg.getClass().equals(Subscription.class)) {
            Subscription sub = (Subscription) msg;
            EventBus.getDefault().post(new SubscriptionEvent(sub));
        } else if (msg.getClass().equals(ForceClear.class)) {
            ForceClear forceClear = (ForceClear) msg;
            EventBus.getDefault().post(new ForceClearEvent(forceClear));
        } else if(msg.toString().startsWith("#ShowAllComplaints#")){
            EventBus.getDefault().post(new MessageEvent(msg.toString()));
        } else if (msg.toString().startsWith("#ShowComplaint")){
            String[] str = msg.toString().split("\t");
            int id = Integer.parseInt(str[1]);
            String name = str[2];
            String cont = str[3];
            EventBus.getDefault().post(new ComplaintEvent(name,cont, id));
        } else if (msg.getClass().equals(Purchase.class)) {
            Purchase p = (Purchase) msg;
            EventBus.getDefault().post(new PurchaseEvent(p));
        } else if (msg.getClass().equals(SendPriceChangesList.class)) {
            SendPriceChangesListEvent event = new SendPriceChangesListEvent(((SendPriceChangesList) msg).getPriceChangeList());
            EventBus.getDefault().post(event);
        }else if(msg.toString().startsWith("#cancelorder\t")) {
            EventBus.getDefault().post(new MessageEvent(msg.toString()));
        } else if(msg.getClass().equals(SystemUser.class)){
            SystemUserEvent event= new SystemUserEvent((SystemUser) msg);
            EventBus.getDefault().post(event);
        }
    }

    public static SimpleClient getClient() {
        if (client == null) {
            client = new SimpleClient("localhost", 3000);
        }
        return client;
    }

}
