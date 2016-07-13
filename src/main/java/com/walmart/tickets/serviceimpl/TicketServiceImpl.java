
package com.walmart.tickets.serviceimpl;

import java.sql.SQLDataException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import com.walmart.tickets.dao.SeatDAO;
import com.walmart.tickets.Seat;
import com.walmart.tickets.SeatHold;
import com.walmart.tickets.service.TicketService;


/**
 * Created by X. Ma on 07/08/2016.
 */
public class TicketServiceImpl implements TicketService {

	/**
	 * The DAO to access the data source
	 */
	final private SeatDAO seatDAO;
	
    /**
     * Constructor
     *
     * @param seatDAO the DAO to access the data source
     */
	public TicketServiceImpl(SeatDAO seatDAO){

		this.seatDAO = seatDAO;
		
	}
	 
    /**
     * The number of seats in the requested level that are neither held nor reserved
     *
     * @param venueLevel a numeric venue level identifier to limit the search
     * @return the number of tickets available on the provided level
     */
	@Override
	public int numSeatsAvailable(Optional<Integer> venueLevel) {
		removeExpiredSeatHold();
		
		int availableNum = 0;
		
		if(venueLevel==null || !venueLevel.isPresent()){
			try{
        		for(int i=1; i<=4; i++){
        			availableNum = availableNum+seatDAO.getLevelAvailableSeats(i).size();
        		}
        	}catch(SQLDataException e){
        		System.out.println("Unable to get the number of available seats.");
        		availableNum = -1;
        	}
		}else{
			int level = venueLevel.get();
        	try {
				availableNum = seatDAO.getLevelAvailableSeats(level).size();
			} catch (SQLDataException e) {
				System.out.println("Invalide level number. Unable to get the number of available seats.");
				availableNum = -1;
			}
		}

        return availableNum;
	}
	
	/**
	 * Clean up expired SeatHold
	 * SeatHold expires after 30 seconds
	 */
	private synchronized void removeExpiredSeatHold() {
		try{
			if(seatDAO.getHoldID()!=null){
				for (Integer holdID : seatDAO.getHoldID()) {
		            if ((System.currentTimeMillis() - seatDAO.getHoldSeat(holdID).getTimeOfHold()) > 30000) {
		                SeatHold releaseSeatHold = seatDAO.getHoldSeat(holdID);
		                Set<Seat> releaseSet = releaseSeatHold.getHoldSet();
		                for (Seat seat : releaseSet) {
		                	seatDAO.updateSeat(seat.getLevel(), seat.getRowNo(), seat.getSeatNo(), "available");
		                }
		                seatDAO.deleteHoldSeat(holdID);
		                System.out.println("SeatHold: "+holdID+" is removed.");
		                
		            }
		        }
			}
		}catch(SQLDataException e){
			System.out.println("Exception in removing expired seat.");
		}
	}

    /**
     * Find and hold the best available seats for a customer
     *
     * @param numSeats the number of seats to find and hold
     * @param minLevel the minimum venue level
     * @param maxLevel the maximum venue level
     * @param customerEmail unique identifier for the customer
     * @return a SeatHold object identifying the specific seats and related information
     */
	@Override
	public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel,
			String customerEmail) {
		if(minLevel == null || maxLevel == null || numSeats <0 || customerEmail == ""){
            if(customerEmail == "")
                System.out.println("Please enter a valid email. No seats were reserved.");
            if (numSeats < 0)
                System.out.println("Please enter a positive seat number. No seats were reserved.");
            return null;
        }
		
		removeExpiredSeatHold();
		
		Optional<Integer> valueOptional = Optional.ofNullable(null);
        if (numSeats > this.numSeatsAvailable(valueOptional)) {
        	System.out.println("No enough available seats. Please try to reserve less seats.");
            return null;
        }
        
        Integer venueLevelMin = minLevel.orElse(1);
        Integer venueLevelMax = maxLevel.orElse(4);
        
        //swapping max and min levels if max level is smaller than min level.
        if(venueLevelMax < venueLevelMin){
            Integer temp = venueLevelMin;
            venueLevelMin = venueLevelMax;
            venueLevelMax = temp;
        }

        SeatHold seatHold = holdSeats(numSeats, venueLevelMin, venueLevelMax, customerEmail);

        return seatHold;
	}
	
    /**
     * It is a helper function helping findAndHoldSeats in putting holds onto the seats.
     * @param numSeats number of seats to be held.
     * @param minLevel minimum level for reserving seats
     * @param maxLevel max level for reserving the seats
     * @param customerEmail customer email provided.
     * @return a SeatHold object identifying the specific tickets and related information
     */
    private synchronized SeatHold holdSeats(int numSeats, Integer minLevel, Integer maxLevel, String customerEmail) {

    	int availableSeatsNum = 0;
        
        try{
        	for(int i=minLevel; i<=maxLevel; i++){
        		availableSeatsNum = availableSeatsNum+seatDAO.getLevelAvailableSeats(i).size();
        	}
        }catch(SQLDataException e){
        	System.out.println("Unable to get available seats.");
			return null;
        }
        
        if (numSeats > availableSeatsNum) {
        	System.out.println("No enough available seats. Please try to reserve less seats.");
            return null;
        }
        
        Long time = System.currentTimeMillis();
        SeatHold seatHold = null;
        Set<Seat> seatToHold = new HashSet<Seat>();

        int numSeatsNeeded = numSeats;
        
        int holdId = generateHoldId();
        try{
        	for(int i=minLevel; i<=maxLevel; i++){
            	if(numSeatsNeeded>0){
            		int numInLevel = seatDAO.getLevelAvailableSeats(i).size();
            		Set<Seat> availableSeats = seatDAO.getLevelAvailableSeats(i);
            		
            		if(numInLevel<=numSeatsNeeded){
            			seatToHold.addAll(availableSeats);
            			for (Seat seat : availableSeats){
            				seatDAO.updateSeat(i, seat.getRowNo(), seat.getSeatNo(), "held");
            			}
            			numSeatsNeeded = numSeatsNeeded - numInLevel;
                		
            		}else{
            			int j = 1;
            			for (Seat seat : availableSeats) {       
            			    if (j > numSeatsNeeded) {
            			    	numSeatsNeeded = 0;
            			    	break; 
            			    }
            			    seatToHold.add(seat);
            			    seatDAO.updateSeat(i, seat.getRowNo(), seat.getSeatNo(), "held");
            			    j++;
            			}

            		}
            	}else{
            		break;
            	}
            }
        	seatHold = new SeatHold(seatToHold, holdId, time, customerEmail);
        	seatDAO.insertHoldSeat(holdId, seatHold);
        }catch(SQLDataException e){
        	System.out.println("Unable to get available seats.");
			return null;
        }
        
        return seatHold;
        
    }
    
    /**
     * this generates a random hold Id created for a SeatHold.
     * @return random hold Id created for a SeatHold
     */
    private int generateHoldId() {

        Random random = new Random();
        Integer holdId = random.nextInt((Integer.MAX_VALUE));
        try{
        	while (seatDAO.getHoldID().contains(holdId)) {
                holdId = random.nextInt((Integer.MAX_VALUE));
            }
        }catch(SQLDataException e){
        	System.out.println("No holdID exists.");
        	holdId = random.nextInt((Integer.MAX_VALUE));
        }
        System.out.println("holdID: "+holdId);
        return holdId;
    }

    /**
     * Commit seats held for a specific customer
     *
     * @param seatHoldId the seat hold identifier
     * @param customerEmail the email address of the customer to which the seat hold is assigned
     * @return a reservation confirmation code
     */
	@Override
	public synchronized String reserveSeats(int seatHoldId, String customerEmail) {
		String str = null;
		
		Set<String> reservedID;
		try {
			reservedID = seatDAO.getReserveID();
			if(reservedID!=null && !reservedID.isEmpty()){
				for(String confirmationCode: reservedID){
					SeatHold seatHold = seatDAO.getReserveSeat(confirmationCode);
					if(seatHold.getHoldId()==seatHoldId){
						System.out.println("The held seat set was already reserved.");
						return str;
					}
				}
			}
		} catch (SQLDataException e1) {
			System.out.println("Reservation failed. ");
		}
		
		
		try{
			SeatHold seatHold= seatDAO.getHoldSeat(seatHoldId);
			if(seatHold != null){
				
				if(seatHold.getCustomerEmail().equals(customerEmail)){
					str = generateConfirmationCode();
					seatDAO.insertReserveSeat(str, seatHold);
					seatDAO.deleteHoldSeat(seatHoldId);
					for(Seat seat: seatHold.getHoldSet()){
						seatDAO.updateSeat(seat.getLevel(), seat.getRowNo(), seat.getSeatNo(), "reserved");
					}
				}else{
					System.out.println("Customer email does not match the one on record with the held seats.");
				}
				
			}
		}catch(SQLDataException e){
			System.out.println("Reservation failed. ");
		}
		
		return str;
	}
	
	/**
     * This method generates a reservation confirmation code for the customer
     *
     * @return String which is the confirmation code for reservation.
     */
    private String generateConfirmationCode() {

        String confirmationCode = null;
        try {
			while(seatDAO.getReserveID().contains(confirmationCode) || confirmationCode.equals("")){
			    confirmationCode = UUID.randomUUID().toString();
			}
		} catch (SQLDataException e) {
			System.out.println("No confirmation code exists.");
			confirmationCode = UUID.randomUUID().toString();
		}
        return confirmationCode;

    }

}
