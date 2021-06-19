package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.ComingSoonMovie;
import il.cshaifasweng.OCSFMediatorExample.entities.LinkMovie;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieTitle;
import il.cshaifasweng.OCSFMediatorExample.entities.Screening;

public class SendMovieEvent {
    private MovieTitle movieTitle;
    private ComingSoonMovie comingSoonMovie;
    private LinkMovie linkMovie;
    private Screening screening;
    private String movieType;  // Either "MovieTitle", "ComingSoonMovie", "LinkMovie" or "Screening".

    public SendMovieEvent(MovieTitle movieTitle) {
        this.movieTitle = movieTitle;
        this.movieType = "MovieTitle";
    }

    public SendMovieEvent(ComingSoonMovie comingSoonMovie) {
        this.comingSoonMovie = comingSoonMovie;
        this.movieType = "ComingSoonMovie";
    }

    public SendMovieEvent(LinkMovie linkMovie) {
        this.linkMovie = linkMovie;
        this.movieType = "LinkMovie";
    }

    public SendMovieEvent(Screening screening) {
        this.screening = screening;
        this.movieType = "Screening";
    }

    public MovieTitle getMovieTitle() {
        return movieTitle;
    }

    public ComingSoonMovie getComingSoonMovie() {
        return comingSoonMovie;
    }

    public LinkMovie getLinkMovie() {
        return linkMovie;
    }

    public Screening getScreening() {
        return screening;
    }

    public String getMovieType() {
        return movieType;
    }
}
