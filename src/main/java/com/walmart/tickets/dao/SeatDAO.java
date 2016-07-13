package com.walmart.tickets.dao;

import java.sql.SQLDataException;
import java.util.Set;

import com.walmart.tickets.Seat;
import com.walmart.tickets.SeatHold;

/**
 * Created by X. Ma on 07/08/2016.
 */

public interface SeatDAO {
	
    /**
     * Insert seats to the data source
     *
     * @param level the level of the seat
     * @param levelName the levelName of the seat
     * @param rowNo the row number of the seat
     * @param seatNo the seat number of the seat
     * @param status the status of the seat
     * @param price the price of the seat
     * @return true if insertion successes; otherwise false
     * @throws SQLDataException 
     */
    boolean insertSeat(int level, String levelName, int rowNo, int seatNo, String status, double price) throws SQLDataException;
    
    /**
     * Update seat after the seat is held, reserved, or the held expired
     *
     * @param level the level of the seat
     * @param rowNo the row number of the seat
     * @param seatNo the seat number of the seat
     * @param status the status of the seat
     * @return true if update successes; otherwise false
     * @throws SQLDataException 
     */
    boolean updateSeat(int level, int rowNo, int seatNo, String status) throws SQLDataException;
    
    /**
     * Delete seat 
     *
     * @param level the level of the seat
     * @param rowNo the row number of the seat
     * @param seatNo the seat number of the seat
     * @return true if deletion successes; otherwise false
     * @throws SQLDataException 
     */
    boolean deleteSeat(int level, int rowNo, int seatNo) throws SQLDataException;
    
    /**
     * Get seat  
     *
     * @param level the level of the seat
     * @param rowNo the row number of the seat
     * @param seatNo the seat number of the seat
     * @return Seat 
     * @throws SQLDataException
     */
    Seat getSeat(int level, int rowNo, int seatNo) throws SQLDataException;
    
    /**
     * Get available seats in the level  
     *
     * @param level the level of the seat
     * @return All available seats in the level 
     * @throws SQLDataException 
     */
    Set<Seat> getLevelAvailableSeats(int level) throws SQLDataException;
    
    /**
     * Get all hold IDs
     *
     * @return All hold IDs
     * @throws SQLDataException
     */
    Set<Integer> getHoldID() throws SQLDataException;
    
    /**
     * Get SeatHold with the holdID  
     *
     * @param holdID the holdID of the held seats
     * @return SeatHold with the holdID
     * @throws SQLDataException
     */
    SeatHold getHoldSeat(Integer holdID) throws SQLDataException;
    
    /**
     * Delete SeatHold with the holdID  
     *
     * @param holdID the holdID of the held seats
     * @return true if deletion successes; otherwise false
     * @throws SQLDataException 
     */
    boolean deleteHoldSeat(Integer holdID) throws SQLDataException;
    
    /**
     * Insert SeatHold with the holdID  
     *
     * @param seatHold the SeatHold to insert
     * @param holdID the holdID of the held seats
     * @return true if insertion successes; otherwise false
     * @throws SQLDataException 
     */
    boolean insertHoldSeat(Integer holdID, SeatHold seatHold) throws SQLDataException;
	
    /**
     * Get reserved SeatHold with the confirmation code  
     *
     * @param confirmationCode the confirmationCode of the held seats
     * @return SeatHold with the confirmationCode
     * @throws SQLDataException
     */
    SeatHold getReserveSeat(String confirmationCode) throws SQLDataException;
    
    /**
     * Delete reserved SeatHold with the confirmation code  
     *
     * @param confirmationCode the confirmationCode of the held seats
     * @return true if deletion successes; false otherwise
     * @throws SQLDataException
     */
    boolean deleteReserveSeat(String confirmationCode) throws SQLDataException;
    
    /**
     * Insert reserved SeatHold with the confirmation code  
     *
     * @param confirmationCode the confirmationCode of the held seats
     * @param seatHold the reserved SeatHold to insert
     * @return true if insertion successes; otherwise false
     * @throws SQLDataException
     */
    boolean insertReserveSeat(String confirmationCode, SeatHold seatHold) throws SQLDataException;
    
    /**
     * Get all reservation confirmation codes  
     *
     * @return all reservation confirmation codes
     * @throws SQLDataException if no reservation was made
     */
    Set<String> getReserveID() throws SQLDataException;

}
