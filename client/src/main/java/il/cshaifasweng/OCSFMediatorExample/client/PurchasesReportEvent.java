package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Purchase;

import java.util.List;

public class PurchasesReportEvent {
    private String reportType = ""; // report types are: ticket/link_subscription

    private Purchase Purchase;

    public Purchase getPurchase() {
        return Purchase;
    }

    public PurchasesReportEvent(Purchase Purchase, String ReportType) {
        this.Purchase = Purchase;
        this.reportType = ReportType;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }
}
