package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.MovieTitle;

public class MovieTitleEvent {
    private MovieTitle movie;

    public MovieTitle getMovie() {
        return movie;
    }

    public MovieTitleEvent(MovieTitle movie) {
        this.movie = movie;
    }
}
