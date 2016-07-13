package com.walmart.tickets;

import java.sql.SQLDataException;

/**
 * Description of a seat
 * Created by X. Ma on 07/08/2016.
 */
public class Seat {
	
	/**
	 * The level of the seat
	 * 1: Orchestra
	 * 2: Main
	 * 3: Balcony 1
	 * 4: balcony 2
	 */
	private int level;
	
	/**
	 * The level name of the seat
	 * is one of the following: Orchestra, Main, Balcony 1, balcony 2
	 */
	private String levelName;
	
	/**
	 * The row number of the seat
	 */
	private int rowNo;

	/**
	 * The seat number in the row
	 */
	private int seatNo;
	
	/**
	 * The status of the seat
	 * is one of the following: available; held; reserved
	 */
	private String seatStatus;

	/**
	 * The price of the seat
	 */
	private double seatPrice;
	
    /**
     * Constructor
     *
     * @param level the level of the seat
     * @param levelName the levelName of the seat
     * @param rowNo the the rowNo of the seat
     * @param seatNo the the seatNo of the seat
     * @param status the the status of the seat
     * @param price the the price of the seats
     */
	public Seat(int level, String levelName, int rowNo, int seatNo, String status, double price){
		this.level = level;
		this.levelName = levelName;
		this.seatNo = seatNo;
		this.rowNo = rowNo;
		this.seatStatus = status;
		this.setSeatPrice(price);
	}

	/**
	 * Constructor
	 */
	public Seat() {
	}

    /**
     * Get the row number
     *
     * @return row number
     */
	public int getRowNo() {
		return rowNo;
	}

    /**
     * Set the row number
     *
     * @param rowNo the row number
     */
	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}
	
    /**
     * Get the seat number
     *
     * @return seat number
     */
	public int getSeatNo() {
		return seatNo;
	}

    /**
     * Set the seat number
     *
     * @param seatNo the seat number
     */
	public void setSeatNo(int seatNo) {
		this.seatNo = seatNo;
	}

    /**
     * Get the level
     *
     * @return level
     */
	public int getLevel() {
		return level;
	}

    /**
     * Set the level
     *
     * @param level the level of the seat
     */
	public void setLevel(int level) {
		this.level = level;
	}

    /**
     * Get the status
     *
     * @return status
     */
	public String getSeatStatus() {
		return seatStatus;
	}

    /**
     * Set the status
     *
     * @param seatStatus the status of the seat
     */
	public void setSeatStatus(String seatStatus) {
		this.seatStatus = seatStatus;
	}

    /**
     * Get the price
     *
     * @return price
     */
	public double getSeatPrice() {
		return seatPrice;
	}

    /**
     * Set the price
     *
     * @param seatPrice the price of the seat
     */
	public void setSeatPrice(double seatPrice) {
		this.seatPrice = seatPrice;
	}

    /**
     * Get the level name
     *
     * @return level name
     */
	public String getLevelName() {
		return levelName;
	}

    /**
     * Set the level name
     *
     * @param levelName the level name
     */
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

    /**
     * Compares the specified object with this seat for equality. 
     * Returns true if the given object is also a seat and the two seat represent the same seats; otherwise false
     *
     * @return true if the given object is also a seat and the two seat represent the same seats; otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Seat seat = (Seat) o;

        if (level != seat.getLevel()) return false;
        if (rowNo != seat.getRowNo()) return false;
        return seatNo == seat.getSeatNo();

    }

    /**
     * Calculate the hashcode 
     *
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        int result = level;
        result = 31 * result + rowNo;
        result = 31 * result + seatNo;
        return result;
    }

    /**
     * A string description of the seat
     *
     * @return a string description of the seat
     */
    @Override
    public String toString() {
        String str = "This seat is at level: "+levelName
        		+", row: "+rowNo
        		+", seat: "+seatNo
        		+", and the price is: "+seatPrice;
    	
        return str;
    }



}
