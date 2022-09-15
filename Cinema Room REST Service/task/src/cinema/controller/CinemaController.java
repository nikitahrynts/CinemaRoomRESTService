package cinema.controller;

import cinema.entity.Cinema;
import cinema.entity.OrderedSeat;
import cinema.entity.Seat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static cinema.entity.Cinema.getAllSeats;

@RestController
public class CinemaController {
    private final Cinema cinema;

    public CinemaController() {
        this.cinema = getAllSeats(9, 9);
    }

    @GetMapping("/seats")
    public Cinema getSeats() {
        return cinema;
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseTicket(@RequestBody Seat seat) {
        if (seat.getColumn() > cinema.getTotal_columns()
                || seat.getRow() > cinema.getTotal_rows()
                || seat.getRow() < 1
                || seat.getColumn() < 1) {
            return new ResponseEntity<>(Map.of("error",
                    "The number of a row or a column is out of bounds!"),
                    HttpStatus.BAD_REQUEST);
        }
        for (int i = 0; i < cinema.getAvailable_seats().size(); i++) {
            Seat s = cinema.getAvailable_seats().get(i);
            if (s.equals(seat)) {
                OrderedSeat orderedSeat = new OrderedSeat(UUID.randomUUID(), s);
                cinema.getOrderedSeats().add(orderedSeat);
                cinema.getAvailable_seats().remove(i);
                return new ResponseEntity<>(orderedSeat, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(Map.of("error",
                "The ticket has been already purchased!"),
                HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/return")
    public ResponseEntity<?> returnTicket(@RequestBody Token token) {
        ArrayList<OrderedSeat> orderedSeats = cinema.getOrderedSeats();
        for (OrderedSeat orderedSeat : orderedSeats) {
            if (orderedSeat.getToken().equals(token.getToken())) {
                orderedSeats.remove(orderedSeat);
                cinema.getAvailable_seats().add(orderedSeat.getTicket());
                return new ResponseEntity<>(Map.of("returned_ticket",
                        orderedSeat.getTicket()), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(Map.of("error", "Wrong token!"),
                HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/stats")
    public ResponseEntity<?> statistics(@RequestParam(required = false)
                                                String password) {
        if (password != null && password.equals("super_secret")) {
            Map<String, Integer> stats = new HashMap<>();
            int currentIncome = 0;
            int availableSeats = cinema.getAvailable_seats().size();
            int purchasedTickets = cinema.getOrderedSeats().size();
            for (OrderedSeat orderedSeat : cinema.getOrderedSeats()) {
                currentIncome += orderedSeat.getTicket().getPrice();
            }
            stats.put("current_income", currentIncome);
            stats.put("number_of_available_seats", availableSeats);
            stats.put("number_of_purchased_tickets", purchasedTickets);
            return new ResponseEntity<>(stats, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Map.of("error",
                    "The password is wrong!"), HttpStatus.valueOf(401));
        }
    }
}

class Token {
    UUID token;

    public Token() {
    }

    public Token(UUID token) {
        this.token = token;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }
}