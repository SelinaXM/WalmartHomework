package com.walmart.tickets;

import java.util.HashSet;
import java.util.Set;

/**
 * Description of a set of held seats
 * Created by X. Ma on 07/08/2016.
 */
public class SeatHold {
	
	/**
	 * The Set of seats held
	 */
	private Set<Seat> holdSet;
    
	/**
	 * The holdID
	 */
	
	private int holdId;
    
	/**
	 * Time of hold
	 */
	private Long timeOfHold;
	
	/**
	 * Customer email address
	 */
    private String customerEmail;

    /**
     * Constructor
     *
     * @param holdSet the set of seats held
     * @param holdId the hold ID
     * @param timeOfHold time of hold
     * @param customerEmail customer email address
     */
    public SeatHold(Set<Seat> holdSet, int holdId, Long timeOfHold, String customerEmail){

        this.holdSet = holdSet;
        this.holdId = holdId;
        this.timeOfHold = timeOfHold;
        this.customerEmail = customerEmail;

    }

    /**
     * Get time of hold
     *
     * @return time of hold
     */
    public Long getTimeOfHold(){
        return timeOfHold;
    }

    /**
     * Get hold ID
     *
     * @return hold ID
     */
    public int getHoldId(){
        return holdId;
    }

    /**
     * Set the hold ID
     *
     * @param holdId hold ID
     */
    public void setHoldId(int holdId){
        this.holdId = holdId;
    }

    /**
     * Get the set of hold seats
     *
     * @return the set of hold seats
     */
    public Set<Seat> getHoldSet() {
        return holdSet;
    }

    /**
     * Set the set of seats held
     *
     * @param holdSet the set of seats held
     */
    public void setHoldSet(Set<Seat> holdSet) {
        this.holdSet = holdSet;
    }

    /**
     * Set the time of hold
     *
     * @param timeOfHold the time of hold
     */
    public void setTimeOfHold(Long timeOfHold) {
        this.timeOfHold = timeOfHold;
    }

    /**
     * Get the customer email address
     *
     * @return customer email address
     */
    public String getCustomerEmail() {
        return customerEmail;
    }

    /**
     * Set the customer email address
     *
     * @param customerEmail customer email address
     */
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

}
