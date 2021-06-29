package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "staged_price_changes")
public class StagedPriceChange implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int changeId;

    private String movieType;

    private int movieId;

    private String newPrice;

    public int getChangeId() {
        return changeId;
    }

    public String getMovieType() {
        return movieType;
    }

    public void setMovieType(String movieType) {
        this.movieType = movieType;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public StagedPriceChange(String movieType, int movieId, String newPrice) {
        this.movieType = movieType;
        this.movieId = movieId;
        this.newPrice = newPrice;
    }

    public StagedPriceChange() {

    }
}
