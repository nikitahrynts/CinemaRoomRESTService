package cinema.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

public class Cinema {

    private int total_rows;
    private int total_columns;
    private ArrayList<Seat> available_seats;
    @JsonIgnore
    private ArrayList<OrderedSeat> orderedSeats;

    public Cinema(int total_rows, int total_columns,
                  ArrayList<Seat> available_seats) {
        this.total_rows = total_rows;
        this.total_columns = total_columns;
        this.available_seats = available_seats;
        this.orderedSeats = new ArrayList<>();
    }

    public int getTotal_rows() {
        return total_rows;
    }

    public void setTotal_rows(int total_rows) {
        this.total_rows = total_rows;
    }

    public int getTotal_columns() {
        return total_columns;
    }

    public void setTotal_columns(int total_columns) {
        this.total_columns = total_columns;
    }

    public ArrayList<Seat> getAvailable_seats() {
        return available_seats;
    }

    public void setAvailable_seats(ArrayList<Seat> available_seats) {
        this.available_seats = available_seats;
    }

    public static Cinema getAllSeats(int rows, int columns) {
        ArrayList<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= columns; j++) {
                seats.add(new Seat(i, j));
            }
        }
        return new Cinema(rows, columns, seats);
    }

    public ArrayList<OrderedSeat> getOrderedSeats() {
        return orderedSeats;
    }

    public void setOrderedSeats(ArrayList<OrderedSeat> orderedSeats) {
        this.orderedSeats = orderedSeats;
    }
}
