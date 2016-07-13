package com.walmart.tickets.daoimpl;

import com.walmart.tickets.Seat;
import com.walmart.tickets.SeatHold;
import com.walmart.tickets.dao.SeatDAO;

import java.sql.SQLDataException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


/**
 * Implementation of SeatDAO interface
 * Created by X. Ma on 07/08/2016.
 */

public class MockSeatDAOImpl implements SeatDAO {

	/**
	 * All seats in the venue
	 */
	private Map<String, Seat> allSeats;
	
	/**
	 * All held seats
	 */
	private Map<Integer, SeatHold> allHoldSeats;
	
	/**
	 * All reserved seats
	 */
	private Map<String, SeatHold> allReservedSeats;

    /**
     * Constructor
     * Creates the venue with 4 levels
     */
	public MockSeatDAOImpl(){
		allSeats=new LinkedHashMap<String, Seat>();
		allHoldSeats = new HashMap<Integer, SeatHold>();
		allReservedSeats = new HashMap<String, SeatHold>();
		try{
			for(int i=1; i<=25; i++){
				for(int j=1; j<=50; j++){
					insertSeat(1, "Orchestra", i, j, "available", 100.00);
				}
			}
			
			for(int i=1; i<=20; i++){
				for(int j=1; j<=100; j++){
					insertSeat(2, "Main", i, j, "available", 75.00);
				}
			}
			
			for(int i=1; i<=15; i++){
				for(int j=1; j<=100; j++){
					insertSeat(3, "Balcony 1", i, j, "available", 50.00);
				}
			}
			
			for(int i=1; i<=15; i++){
				for(int j=1; j<=100; j++){
					insertSeat(4, "Balcony 2", i, j, "available", 45.00);
				}
			}
		}catch(SQLDataException e){
			System.out.println("Exception while trying to connect to the data source.");
		}
	}

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
     * @throws SQLDataException if the seat was already inserted, level>4, or level<1
     */
	@Override
	public boolean insertSeat(int level, String levelName, int rowNo, int seatNo, String status, double price) throws SQLDataException {
		String seatID = level+"-"+rowNo+"-"+seatNo;

		boolean insertSuccess = false;
		if(allSeats.containsKey(seatID)){
			System.out.println("The seat is already created");

		}else{
			Seat seat = new Seat(level, levelName, rowNo, seatNo, status, price);
			allSeats.put(seatID, seat);
			insertSuccess = true;

		}

		if (level>4 && level<1) {
			throw new SQLDataException(" MOCKED INSTERT ERROR!");
		}

		return insertSuccess;
	}

    /**
     * Update seat after the seat is held, reserved, or the held expired
     *
     * @param level the level of the seat
     * @param rowNo the row number of the seat
     * @param seatNo the seat number of the seat
     * @param status the status of the seat
     * @return true if update successes; otherwise false
     * @throws SQLDataException if the seat does not exist, or status change is not valid (not from available to held, or held to reserved, or held to available)
     */
	@Override
	public boolean updateSeat(int level, int rowNo, int seatNo, String status) throws SQLDataException{
		
		String seatID = level+"-"+rowNo+"-"+seatNo;
		boolean updateSuccess = false;

		if(!allSeats.containsKey(seatID)){
			System.out.println("There is no such seat.");
			throw new SQLDataException(" MOCKED UPDATE ERROR!");

		}else{
			Seat seat = allSeats.get(seatID);
			String currentStatus = seat.getSeatStatus();
			if((currentStatus.equals("available") && status.equals("held")) || 
					(currentStatus.equals("held") && status.equals("reserved")) || 
					(currentStatus.equals("held") && status.equals("available"))){
				seat.setSeatStatus(status);
				updateSuccess = true;
			}else{
				throw new SQLDataException(" MOCKED UPDATE ERROR!");
			}
		}

		return updateSuccess;

	}

    /**
     * Delete seat 
     *
     * @param level the level of the seat
     * @param rowNo the row number of the seat
     * @param seatNo the seat number of the seat
     * @return true if deletion successes; otherwise false
     * @throws SQLDataException if the seat does not exist
     */
	@Override
	public boolean deleteSeat(int level, int rowNo, int seatNo) throws SQLDataException  {
		String seatID = level+"-"+rowNo+"-"+seatNo;
		boolean success = false;
		
		if(!allSeats.containsKey(seatID)){
			throw new SQLDataException(" MOCKED DELETE ERROR!");
			
			
		}else{
			allSeats.remove(seatID);
			success = true;
		}
		return success;
	}

    /**
     * Get seat  
     *
     * @param level the level of the seat
     * @param rowNo the row number of the seat
     * @param seatNo the seat number of the seat
     * @return Seat 
     * @throws SQLDataException if the seat does not exist, level>4, or level<1
     */
	@Override
	public Seat getSeat(int level, int rowNo, int seatNo) throws SQLDataException {
		String seatID = level+"-"+rowNo+"-"+seatNo;
		Seat seat = null;
		
		if(!allSeats.containsKey(seatID)){
			System.out.println("There is no such seat.");
			throw new SQLDataException(" MOCKED GET ERROR!");
			
			
		}else{
			seat = allSeats.get(seatID);
			
		}
		
		if (level>4 && level<1) {
			throw new SQLDataException(" MOCKED GET ERROR!");
		}
		
		return seat;
		
	}

    /**
     * Get available seats in the level  
     *
     * @param level the level of the seat
     * @return All available seats in the level 
     * @throws SQLDataException if level>4, or level<1
     */
	@Override
	public Set<Seat> getLevelAvailableSeats(int level) throws SQLDataException {
		
		if(level<=4 && level>=1){
			Set<Seat> availableSeats = new LinkedHashSet<Seat>();
			int rowNum = 0;
			int seatNum = 0;
			
			if(level==1){
				rowNum = 25;
				seatNum = 50;
			}else if(level==2){
				rowNum = 20;
				seatNum = 100;
			}else if(level==3){
				rowNum = 15;
				seatNum = 100;
			}else if(level==4){
				rowNum = 15;
				seatNum = 100;
			}
			
			for(int i=1; i<=rowNum; i++){
				for(int j=1; j<=seatNum; j++){
					String seatID = level+"-"+i+"-"+j;
					Seat seat = allSeats.get(seatID);
					if(seat.getSeatStatus().equals("available")){
						availableSeats.add(seat);
					}
				}
			}
			return availableSeats;
		}else{
			throw new SQLDataException(" MOCKED GET ERROR!");
		}
		
		
	}

    /**
     * Get all hold IDs
     *
     * @return All hold IDs
     * @throws SQLDataException if no SeatHold
     */
	@Override
	public Set<Integer> getHoldID() throws SQLDataException{
		if(allHoldSeats.isEmpty()){
			throw new SQLDataException(" No seats were held.");
		}else{
			return allHoldSeats.keySet();
		}
		
		
		
	}

    /**
     * Get SeatHold with the holdID  
     *
     * @param holdID the holdID of the held seats
     * @return SeatHold with the holdID
     * @throws SQLDataException if the holdID does not exists
     */
	@Override
	public SeatHold getHoldSeat(Integer holdID) throws SQLDataException {
		
		SeatHold seatHold= null;
		
		if (!allHoldSeats.containsKey(holdID)) {
			throw new SQLDataException(" Does not have a such hold.");
		}
		
		seatHold = allHoldSeats.get(holdID);
		return seatHold;
	}

    /**
     * Delete SeatHold with the holdID  
     *
     * @param holdID the holdID of the held seats
     * @return true if deletion successes; otherwise false
     * @throws SQLDataException if the holdID does not exists
     */
	@Override
	public boolean deleteHoldSeat(Integer holdID) throws SQLDataException {
		boolean success = false;
		
		if (!allHoldSeats.containsKey(holdID)) {
			throw new SQLDataException(" Does not have a such hold.");
		}else{
			allHoldSeats.remove(holdID);
			success = true;
		}
		
		return success;
	}

    /**
     * Insert SeatHold with the holdID  
     *
     * @param seatHold the SeatHold to insert
     * @param holdID the holdID of the held seats
     * @return true if insertion successes; otherwise false
     * @throws SQLDataException if the holdID already exists
     */
	@Override
	public boolean insertHoldSeat(Integer holdID, SeatHold seatHold) throws SQLDataException {
		boolean success = false;
		
		if (allHoldSeats.containsKey(holdID)) {
			throw new SQLDataException(" holdID exists.");
		}else{
			allHoldSeats.put(holdID, seatHold);
			success = true;
		}
		
		return success;
	}

    /**
     * Get all reservation confirmation codes  
     *
     * @return all reservation confirmation codes
     * @throws SQLDataException if no reservation was made
     */
	@Override
	public Set<String> getReserveID() throws SQLDataException{
		if(allReservedSeats.isEmpty()){
			throw new SQLDataException(" No seats were reserved.");
			//return null;
		}
		
		return allReservedSeats.keySet();
		
	}

    /**
     * Insert reserved SeatHold with the confirmation code  
     *
     * @param confirmationCode the confirmationCode of the held seats
     * @param seatHold the reserved SeatHold to insert
     * @return true if insertion successes; otherwise false
     * @throws SQLDataException if the confirmationCode already exists
     */
	@Override
	public boolean insertReserveSeat(String confirmationCode, SeatHold seatHold) throws SQLDataException {
		boolean success = false;
		
		if (allReservedSeats.containsKey(confirmationCode)) {
			throw new SQLDataException(" confirmation code exists.");
		}else{
			allReservedSeats.put(confirmationCode, seatHold);
			success = true;
		}
		
		return success;
	}

    /**
     * Get reserved SeatHold with the confirmation code  
     *
     * @param confirmationCode the confirmationCode of the held seats
     * @return SeatHold with the confirmationCode
     * @throws SQLDataException if the confirmationCode does not exists
     */
	@Override
	public SeatHold getReserveSeat(String confirmationCode) throws SQLDataException {
		if (!allReservedSeats.containsKey(confirmationCode)) {
			throw new SQLDataException(" Does not have a such reservation.");
		}
		
		return allReservedSeats.get(confirmationCode);
	}

    /**
     * Delete reserved SeatHold with the confirmation code  
     *
     * @param confirmationCode the confirmationCode of the held seats
     * @return true if deletion successes; false otherwise
     * @throws SQLDataException if the confirmationCode does not exists
     */
	@Override
	public boolean deleteReserveSeat(String confirmationCode) throws SQLDataException {
		boolean success = false;
		
		if (!allReservedSeats.containsKey(confirmationCode)) {
			throw new SQLDataException(" Does not have a such reservation.");
		}else{
			allReservedSeats.remove(confirmationCode);
			success = true;
		}
		
		return success;
	}

}
