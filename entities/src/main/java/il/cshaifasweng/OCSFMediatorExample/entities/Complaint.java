package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

enum Result{
    RESOLVED,
    REFUNDED,
    REJECTED,
    ACTIVE
}

@Entity
@Table(name = "complaints")
public class Complaint implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int complaintId;

    private String customer_name;
    private String time_registration;
    private int refunded;
    @Column(columnDefinition="blob")
    private String complaint_details;

    private Result result;
    private String closing_complaint_time;

    public Complaint(){}
    public Complaint(String customer_name, String time_registration, String complaint_details){
        this.customer_name = customer_name;
        this.time_registration = time_registration;
        this.complaint_details = complaint_details;
        this.result = Result.ACTIVE;
        this.refunded = 0;
    }

    public String getCustomer_name() {
        return customer_name;
    }
    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }
    public int getComplaintId() {
        return complaintId;
    }

    public void setClosing_complaint_time(String closing_complaint_time) {
        this.closing_complaint_time = closing_complaint_time;
    }

    public String getComplaint_details(){
        return this.complaint_details;
    }
    public void setComplaint_details(String complaint_details) {
        this.complaint_details = complaint_details;
    }

    public void setResult(String result) {
        switch (result){
            case "Resolved":
                this.result = Result.RESOLVED;
                break;
            case "Refunded":
                this.result = Result.REFUNDED;
                break;
            case "Rejected":
                this.result = Result.REJECTED;
                break;

            default:
                break;
        }

    }

    public int getResult() {

        switch (this.result){
            case RESOLVED:
                return 0;
            case REFUNDED:
                return 1;
            case REJECTED:
                return 2;
            case ACTIVE:
                return 3;
            default:
                break;
        }
        return -1;
    }

    public String getTime_registration() {
        return time_registration;
    }
    public void setTime_registration(String time_registration) {
        this.time_registration = time_registration;
    }

    public int getRefunded() {
        return refunded;
    }

    public void setRefunded(int refunded) {
        this.refunded = refunded;
    }
}