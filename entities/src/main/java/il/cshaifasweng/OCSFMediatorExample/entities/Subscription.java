package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "subscriptions")
public class Subscription implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subscriptionId;

    private String full_name;

    private int entries_left;

    public Subscription() {

    }

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public int getEntries_left() {
        return entries_left;
    }

    public void setEntries_left(int entries_left) {
        this.entries_left = entries_left;
    }

    public Subscription(String full_name) {
        this.full_name = full_name;
        this.entries_left = 20;
    }

    public Subscription() {
    }
}
