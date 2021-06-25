package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;
import java.io.Serializable;

enum Status {
    PURCHASED,
    CANCELLED,
    RETURNED
};

@Entity
@Table(name = "purchases")
public class Purchase implements Serializable{

    private static long serialVersionUID = -8224097662914849956L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int purchaseId;

//    private int orderID; decide later

    private String payment_info;
    private String customer_name;
    private String purchase_time;

//    private Screening screening; TODO add later when cinema classes are ready
    private int price;
    private int seat;
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private MovieTitle movie_ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_link_id")
    private LinkMovie movie_link;

    //TODO add later when subscription classes are ready
//    @ManyToOne
//    @JoinColumn(name = "subscription_id")
//    private SubscriptionMovies subsmovies;

   public Purchase(String customer_name, String payment_info, String purchase_time, int price, int seat, MovieTitle movie_ticket,
             LinkMovie movie_link){
        this.customer_name = customer_name;
        this.payment_info = payment_info;
        this.purchase_time = purchase_time;
        this.price = price;
        this.seat = seat;
        this.movie_link = movie_link;
        this.movie_ticket = movie_ticket;
        this.status = Status.PURCHASED;
    }

    public Purchase(){}

    public Status getStatus() {
        return status;
    }

    public String getPayment_info() {
        return payment_info;
    }

    public int getPrice() {
        return price;
    }

    public int getPurchaseId() {
        return purchaseId;
    }

    public int getSeat() {
        return seat;
    }

    public LinkMovie getMovie_link() {
        return movie_link;
    }

    public MovieTitle getMovie_ticket() {
        return movie_ticket;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getPurchase_time() {
        return purchase_time;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public void setMovie_link(LinkMovie movie_link) {
        this.movie_link = movie_link;
    }

    public void setMovie_ticket(MovieTitle movie_ticket) {
        this.movie_ticket = movie_ticket;
    }

    public void setPayment_info(String payment_info) {
        this.payment_info = payment_info;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setPurchase_time(String purchase_time) {
        this.purchase_time = purchase_time;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

}
