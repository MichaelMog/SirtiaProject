package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;

public class ComplaintReport implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;

    private Complaint complaint;

    public Complaint getComplaint() {
        return complaint;
    }

    public ComplaintReport(Complaint complaint) {
        this.complaint = complaint;
    }
}
