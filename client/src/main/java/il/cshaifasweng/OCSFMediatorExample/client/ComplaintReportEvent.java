package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;

public class ComplaintReportEvent {
    private Complaint complaint;

    public Complaint getComplaint() {
        return complaint;
    }

    public ComplaintReportEvent(Complaint complaint) {
        this.complaint = complaint;
    }
}
