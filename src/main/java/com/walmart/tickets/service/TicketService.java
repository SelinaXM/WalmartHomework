package com.walmart.tickets.service;


import java.util.Optional;

import com.walmart.tickets.SeatHold;

/**
 * Created by X. Ma on 07/08/2016.
 */
public interface TicketService {

    /**
     * The number of tickets in the requested level that are neither held nor reserved
     *
     * @param venueLevel a numeric venue level identifier to limit the search
     * @return the number of tickets available on the provided level
     */
    int numSeatsAvailable(Optional<Integer> venueLevel);


    /**
     * Find and hold the best available tickets for a customer
     *
     * @param numSeats the number of tickets to find and hold
     * @param minLevel the minimum venue level
     * @param maxLevel the maximum venue level
     * @param customerEmail unique identifier for the customer
     * @return a SeatHold object identifying the specific tickets and related information
     */
    SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel, String customerEmail);


    /**
     * Commit tickets held for a specific customer
     *
     * @param seatHoldId the seat hold identifier
     * @param customerEmail the email address of the customer to which the seat hold is assigned
     * @return a reservation confirmation code
     */

    String reserveSeats(int seatHoldId, String customerEmail);


}
