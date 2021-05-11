package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "movies")
public class MovieTitle implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int movieId;

    private String hebrewName;

    private String englishName;

    private String producer;

    private String actors;

    private String movieDescription;

    private String imagePath;

    private String genres;

    private String showTimes;


    public int getMovieId() {
        return movieId;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    public String getHebrewName() {
        return hebrewName;
    }

    public void setHebrewName(String hebrewName) {
        this.hebrewName = hebrewName;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getShowTimes() {
        return showTimes;
    }

    public void setShowTimes(String showTimes) {
        this.showTimes = showTimes;
    }

    public MovieTitle(String hebrewName, String englishName, String genres, String producer, String actors, String movieDescription, String imagePath, String showTimes) {
        this.hebrewName = hebrewName;
        this.englishName = englishName;
        this.genres = genres;
        this.producer = producer;
        this.actors = actors;
        this.movieDescription = movieDescription;
        this.imagePath = imagePath;
        this.showTimes = showTimes;
    }

    public MovieTitle() {

    }
}
