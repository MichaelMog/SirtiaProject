package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "coming_soon_movies")
public class ComingSoonMovie implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int movieId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movieTitleId")
    private MovieTitle movieTitle;

    private String price;

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

    public ComingSoonMovie(MovieTitle movieTitle, String price) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.price = price;
    }

    public ComingSoonMovie() {
    }
}
