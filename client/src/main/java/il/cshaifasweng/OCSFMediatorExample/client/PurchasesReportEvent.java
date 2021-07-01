package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Purchase;

import java.util.List;

public class PurchasesReportEvent {
    private String ReportType = "";

    private List<Purchase> ticketPurchases;

    public List<Purchase> getPurchases() {
        return ticketPurchases;
    }

    public PurchasesReportEvent(List<Purchase> ticketPurchases, String ReportType) {
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
