package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;


@Entity
@Table(name = "CancelledPurchases")
public class CancelledPurchases {
    private static long serialVersionUID = -8224097662914849956L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cancelledPurchaseID;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;


    private int sumrefund;

    public  CancelledPurchases(Purchase purchase, int sumrefund){
        this.purchase = purchase;
        this.sumrefund = sumrefund;
    }

    public CancelledPurchases() {

    }

    public Purchase getPurchase() {
        return purchase;
    }

    public int getSumrefund() {
        return sumrefund;
    }

    public int getCancelledPurchaseID() {
        return cancelledPurchaseID;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public void setSumrefund(int sumrefund) {
        this.sumrefund = sumrefund;
    }
}
