package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;

public class PurchaseReport implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;

    private String ReportType = "";
    private Purchase ticketPurchase;

    public Purchase getPurchase() {
        return ticketPurchase;
    }

    public PurchaseReport(Purchase ticketPurchases, String ReportType) {
        this.ticketPurchase = ticketPurchases;
        this.ReportType = ReportType;
    }

    public String getReportType() {
        return ReportType;
    }

    public void setReportType(String reportType) {
        ReportType = reportType;
    }
}
