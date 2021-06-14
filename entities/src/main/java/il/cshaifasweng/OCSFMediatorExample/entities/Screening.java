package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "screenings")
public class Screening implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int screeningId;

    private String screeningTime;

    private String screeningLocation;

    private int screeningRows;

    private int screeningColumns;

    private int screeningAvailableSeats;

    private String screeningTakenSeats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private MovieTitle movie;

    public int getScreeningId() {
        return screeningId;
    }

    public String getTime() {
        return screeningTime;
    }

    public void setTime(String time) {
        this.screeningTime = time;
    }

    public String getLocation() {
        return screeningLocation;
    }

    public void setLocation(String location) {
        this.screeningLocation = location;
    }

    public int getRows() {
        return screeningRows;
    }

    public void setRows(int rows) {
        this.screeningRows = rows;
    }

    public int getColumns() {
        return screeningColumns;
    }

    public void setColumns(int columns) {
        this.screeningColumns = columns;
    }

    public int getAvailableSeats() {
        return screeningAvailableSeats;
    }

    public String getTakenSeats() {
        return screeningTakenSeats;
    }

    public void addTakenSeat(String takenSeat) {
        this.screeningTakenSeats += takenSeat;
    }

    public MovieTitle getMovie() {
        return movie;
    }

    public void setMovie(MovieTitle movie) {
        this.movie = movie;
    }

    public Screening(String time, String location, int rows, int columns) {
        this.screeningTime = time;
        this.screeningLocation = location;
        this.screeningRows = rows;
        this.screeningColumns = columns;
        this.screeningTakenSeats = "";
        this.screeningAvailableSeats = this.screeningRows * this.screeningColumns;
    }
}
