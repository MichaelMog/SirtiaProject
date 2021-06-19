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

    @Column(name = "screening_time")
    private String time;

    private String location;

    @Column(name = "screening_rows")
    private int rows;

    @Column(name = "screening_columns")
    private int columns;

    private int availableSeats;

    private String takenSeats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movieTitleId")
    private MovieTitle movieTitle;

    private String price;

    public int getScreeningId() {
        return screeningId;
    }

    public String getPrice() {
        return price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getTakenSeats() {
        return takenSeats;
    }

    public void setTakenSeats(String takenSeats) {
        this.takenSeats = takenSeats;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void addTakenSeat(String takenSeat) {
        this.takenSeats += takenSeat;
    }

    public MovieTitle getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(MovieTitle movieTitle) {
        this.movieTitle = movieTitle;
    }

    public Screening(MovieTitle movieTitle, String price, String time, String location, int rows, int columns) {
        this.movieTitle = movieTitle;
        this.time = time;
        this.location = location;
        this.rows = rows;
        this.columns = columns;
        this.takenSeats = "";
        this.availableSeats = this.rows * this.columns;
        this.price = price;
    }

    public Screening() {

    }
}
