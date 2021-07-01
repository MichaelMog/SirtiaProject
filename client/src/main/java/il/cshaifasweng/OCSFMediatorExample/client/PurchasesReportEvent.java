package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Purchase;

import java.util.List;

public class PurchasesReportEvent {
    private String reportType = ""; // report types are: ticket/link_subscription

    private List<Purchase> ticketPurchases;

    public List<Purchase> getPurchases() {
        return ticketPurchases;
    }

    public PurchasesReportEvent(List<Purchase> ticketPurchases, String ReportType) {
        this.ticketPurchases = ticketPurchases;
        this.reportType = ReportType;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }
}
