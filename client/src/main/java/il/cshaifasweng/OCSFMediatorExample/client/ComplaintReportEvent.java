package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;

import java.util.List;

public class ComplaintReportEvent {
    private List<Complaint> complaints;

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public ComplaintReportEvent(List<Complaint> complaints) {
        this.complaints = complaints;
    }
}
