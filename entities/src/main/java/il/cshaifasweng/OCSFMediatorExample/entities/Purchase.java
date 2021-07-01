package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "purchases")
public class Purchase implements Serializable {

    private static long serialVersionUID = -8224097662914849956L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int purchaseId;


    private String paymentInfo;
    private String customerName;
    private String purchaseTime;
    private String movieDetail;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "screening_id")
    private Screening screening;
    private int price;
    @Column(columnDefinition="TEXT")
    private String seats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private MovieTitle title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_link_id")
    private LinkMovie linkMovie;

    //TODO add later when subscription classes are ready

    public Purchase(String customerName, String paymentInfo, String purchaseTime, int price, String seats, MovieTitle title,
                    LinkMovie linkMovie, String movieDetail, Screening screening) {
        this.customerName = customerName;
        this.paymentInfo = paymentInfo;
        this.purchaseTime = purchaseTime;
        this.price = price;
        this.seats = seats;
        this.linkMovie = linkMovie;
        this.title = title;
        this.movieDetail = movieDetail;
        this.screening = screening;
    }

    public Purchase(String customerName, String paymentInfo, String purchaseTime, int price, MovieTitle title,
                    LinkMovie linkMovie, String movieDetail) {
        this.customerName = customerName;
        this.paymentInfo = paymentInfo;
        this.purchaseTime = purchaseTime;
        this.price = price;
        this.linkMovie = linkMovie;
        this.title = title;
        this.movieDetail = movieDetail;
    }

    public Purchase(String customerName, String paymentInfo, String purchaseTime, int price, MovieTitle title,
                    LinkMovie linkMovie) {
        this.customerName = customerName;
        this.paymentInfo = paymentInfo;
        this.purchaseTime = purchaseTime;
        this.price = price;
        this.linkMovie = linkMovie;
        this.title = title;
    }

    public Purchase() {
    }

    public String getMovieDetail() {
        return movieDetail;
    }

    public void setMovieDetail(String movieDetail) {
        this.movieDetail = movieDetail;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getPaymentInfo() {
        return paymentInfo;
    }

    public int getPrice() {
        return price;
    }

    public int getPurchaseId() {
        return purchaseId;
    }

    public LinkMovie getLinkMovie() {
        return linkMovie;
    }

    public Screening getScreening() {
        return screening;
    }

    public MovieTitle getTitle() {
        return title;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPurchaseTime() {
        return purchaseTime;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setLinkMovie(LinkMovie linkMovie) {
        this.linkMovie = linkMovie;
    }

    public void setTitle(MovieTitle title) {
        this.title = title;
    }

    public void setPaymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setPurchaseTime(String purchaseTime) {
        this.purchaseTime = purchaseTime;
    }
}
