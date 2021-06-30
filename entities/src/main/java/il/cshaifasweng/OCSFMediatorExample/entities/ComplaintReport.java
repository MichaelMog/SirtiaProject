package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;

public class ComplaintReport implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;

    private List<Complaint> complaints;

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public ComplaintReport(List<Complaint> complaints) {
        this.complaints = complaints;
    }
}
