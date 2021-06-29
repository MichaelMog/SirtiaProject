package il.cshaifasweng.OCSFMediatorExample.client;

public class MessageEvent {

    private String msg;

    public MessageEvent(String msg){
        if(msg.startsWith("#ShowAllComplaints#")){
            this.msg = msg.split("#ShowAllComplaints#")[1];
        }

        else {
            this.msg = msg;
        }
    }

    public String getMsg() {
        return msg;
    }
}