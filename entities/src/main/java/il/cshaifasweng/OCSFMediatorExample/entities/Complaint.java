package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;
import java.io.Serializable;

enum Result{
    INPROCESS,
    REFUNDED,
    REJECTED
}

@Entity
@Table(name = "complaints")
public class Complaint {

    private static long serialVersionUID = -8224097662914849956L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int complaintId;

    private String customer_name;
    private String time_registration;
    private String complaint_details;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "purchaseId")
    private Purchase purchase;


    public Complaint(){}
    public Complaint(String customer_name, String time_registration, String complaint_details, Purchase purchase){
        this.customer_name = customer_name;
        this.time_registration = time_registration;
        this.complaint_details = complaint_details;
        this.purchase = purchase;
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

    public void setComplaint_details(String complaint_details) {
        this.complaint_details = complaint_details;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public String getTime_registration() {
        return time_registration;
    }

    public void setTime_registration(String time_registration) {
        this.time_registration = time_registration;
    }
}
