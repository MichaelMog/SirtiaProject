package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "link_movies")
public class LinkMovie implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int movieId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movieTitleId")
    private MovieTitle movieTitle;

    private String price;

    private String link;

    private String watchHours;

    public int getMovieId() {
        return movieId;
    }

    public MovieTitle getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(MovieTitle movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getWatchHours() {
        return watchHours;
    }

    public void setWatchHours(String watchHours) {
        this.watchHours = watchHours;
    }

    public LinkMovie(MovieTitle movieTitle, String price, String link, String watchHours) {
        this.movieTitle = movieTitle;
        this.price = price;
        this.link = link;
        this.watchHours = watchHours;
    }

    public LinkMovie() {
    }
}
