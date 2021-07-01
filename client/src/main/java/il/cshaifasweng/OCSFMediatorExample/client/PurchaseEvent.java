package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Purchase;

public class PurchaseEvent {

    private int purchase_id;
    private String payment_info;
    private String customer_name;
    private String purchase_time;
    private String movieDetail;
    private int price;

    public PurchaseEvent(Purchase purchase) {
        this.purchase_id = purchase.getPurchaseId();
        this.payment_info = purchase.getPaymentInfo();
        this.customer_name = purchase.getCustomerName();
        this.purchase_time = purchase.getPurchaseTime();
        this.movieDetail = purchase.getMovieDetail();
        this.price = purchase.getPrice();
    }

    public int getPurchase_id() {
        return purchase_id;
    }

    public String getPayment_info() {
        return payment_info;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getPurchase_time() {
        return purchase_time;
    }

    public String getMovieDetail() {
        return movieDetail;
    }

    public int getPrice() {
        return price;
    }
}