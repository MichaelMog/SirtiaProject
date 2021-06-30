package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;


@Entity
@Table(name = "cancelled_purchases")
public class CancelledPurchases {
    private static long serialVersionUID = -8224097662914849956L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cancelledPurchaseID;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "purchase_id")
    private int purchaseId;
    private String status;
    private String movieDetail;

    private int refund;

    public  CancelledPurchases(int purchaseId, int sumrefund, String status, String movieDetail){
        this.purchaseId = purchaseId;
        this.refund = sumrefund;
        this.status = status;
        this.movieDetail = movieDetail;
    }

    public CancelledPurchases() {

    }

//    public Purchase getPurchase() {
//        return purchase;
//    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMovieDetail() {
        return movieDetail;
    }

    public void setMovieDetail(String movieDetail) {
        this.movieDetail = movieDetail;
    }

    public int getRefund() {
        return refund;
    }

    public int getCancelledPurchaseID() {
        return cancelledPurchaseID;
    }

    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }
    //    public void setPurchase(Purchase purchase) {
//        this.purchase = purchase;
//    }

    public void setRefund(int refund) {
        this.refund = refund;
    }
}