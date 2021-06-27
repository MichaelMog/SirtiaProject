package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Purchase;

public class PurchaseEvent {

    private String payment_info;
    private String customer_name;
    private String purchase_time;
    private String movieDetail;
    private int price;

    public PurchaseEvent(Purchase purchase) {
        this.payment_info = purchase.getPayment_info();
        this.customer_name = purchase.getCustomer_name();
        this.purchase_time = purchase.getPurchase_time();
        this.movieDetail = purchase.getMovieDetail();
        this.price = purchase.getPrice();
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