package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;

public class PurchaseReport implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;

    private String ReportType = "";
    private List<Purchase> ticketPurchases;

    public List<Purchase> getPurchases() {
        return ticketPurchases;
    }

    public PurchaseReport(List<Purchase> ticketPurchases, String ReportType) {
        this.ticketPurchases = ticketPurchases;
        this.ReportType = ReportType;
    }

    public String getReportType() {
        return ReportType;
    }

    public void setReportType(String reportType) {
        ReportType = reportType;
    }
}
