package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cancelled_purchases")
public class CancelledPurchase implements Serializable {
    private static long serialVersionUID = -8224097662914849956L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cancelledPurchaseID;

    private int purchaseId;
    private String purchaseTime;
    private String customerName;
    private String paymentInfo;
    private String movieDetail;

    private String status;


    private int refund;

    public CancelledPurchase(int purchaseId, int sumRefund, String status, String movieDetail, String paymentInfo, String customerName, String purchaseTime) {
        this.purchaseId = purchaseId;
        this.refund = sumRefund;
        this.status = status;
        this.movieDetail = movieDetail;
        this.customerName = customerName;
        this.paymentInfo = paymentInfo;
        this.purchaseTime = purchaseTime;
    }

    public CancelledPurchase() {

    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPurchaseTime() {
        return purchaseTime;
    }

    public String getPaymentInfo() {
        return paymentInfo;
    }

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
