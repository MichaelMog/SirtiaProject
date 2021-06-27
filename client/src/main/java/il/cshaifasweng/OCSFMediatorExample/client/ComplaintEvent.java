package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;

public class ComplaintEvent {

    private String name;
    private String content;
    private int id;

    public ComplaintEvent(Complaint msg){
        this.name = msg.getCustomer_name();
        this.content = msg.getComplaint_details();
        this.id = msg.getComplaintId();
    }
    public ComplaintEvent(String name, String content, int id){
        this.name = name;
        this.content = content;
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}