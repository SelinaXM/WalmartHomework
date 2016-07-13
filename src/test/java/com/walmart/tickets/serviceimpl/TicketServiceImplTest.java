package com.walmart.tickets.serviceimpl;

import com.walmart.tickets.Seat;
import com.walmart.tickets.SeatHold;
import com.walmart.tickets.dao.SeatDAO;
import com.walmart.tickets.daoimpl.MockSeatDAOImpl;
import com.walmart.tickets.service.TicketService;
import com.walmart.tickets.serviceimpl.TicketServiceImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.SQLDataException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Test cases for TicketServiceImpl class
 * Created by X. Ma on 7/10/2016.
 */
public class TicketServiceImplTest {
	

	/**
	 * The DAO to access the data source
	 */
	private SeatDAO mockDao=new MockSeatDAOImpl();
    
	@Rule
    public final ExpectedException exception = ExpectedException.none();
    
	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test numSeatsAvailable(Optional<Integer> venueLevel) method with level specified
	 */
	@Test
    public void numSeatsAvailableTest() throws Exception {
    	TicketService service = new TicketServiceImpl(mockDao);
        
        Optional<Integer> levelNumber = Optional.of(2);
        int seatNum = service.numSeatsAvailable(levelNumber);
        assertEquals(seatNum, 2000);
        
    }

	/**
	 * Test numSeatsAvailable(Optional<Integer> venueLevel) method with level not specified
	 */
    @Test
    public void numSeatsAvailableEmptyTest() throws Exception {
    	TicketService service = new TicketServiceImpl(mockDao);

    	int seatNum = service.numSeatsAvailable(Optional.empty());
        assertEquals(seatNum, 6250);
        
    }

	/**
	 * Test numSeatsAvailable(Optional<Integer> venueLevel) method with venueLevel is null
	 */
    @Test
    public void numSeatsAvailableNullTest() throws Exception {
    	TicketService service = new TicketServiceImpl(mockDao);
        
    	int seatNum = service.numSeatsAvailable(Optional.ofNullable(null));
        assertEquals(seatNum, 6250);
    }

	/**
	 * Test numSeatsAvailable(Optional<Integer> venueLevel) method with Exception
	 */
    @Test
    public void numSeatsAvailableExceptioTestn() throws Exception {
    	TicketService service = new TicketServiceImpl(mockDao);
        
    	Optional<Integer> levelNumber = Optional.of(5);
        assertEquals(service.numSeatsAvailable(levelNumber), -1);
    }

	/**
	 * Test findAndHoldSeats method with valid input parameters
	 */
    @Test
    public void findAndHoldSeatsTest() throws Exception {
    	TicketService service = new TicketServiceImpl(mockDao);
    	
    	SeatHold seatHold1= service.findAndHoldSeats(10, Optional.of(1), Optional.empty(), "xyz@hotmail.com");
    	assertEquals(seatHold1.getHoldSet().size(), 10);
        
        int seatNumAvailable1 = service.numSeatsAvailable(Optional.ofNullable(null));
        assertEquals(seatNumAvailable1, 6240);      
        
    }

	/**
	 * Test findAndHoldSeats method with invalid input parameters
	 */
    @Test
    public void findAndHoldSeatsNullTest() throws Exception {
    	TicketService service = new TicketServiceImpl(mockDao);
    	
    	SeatHold seatHold1= service.findAndHoldSeats(10, Optional.of(1), Optional.empty(), "");
    	assertNull(seatHold1);     
        
    }

	/**
	 * Test findAndHoldSeats method with Exception
	 */
    @Test
    public void findAndHoldSeatsExceptionTest() throws Exception {
    	TicketService service = new TicketServiceImpl(mockDao);
    	
    	SeatHold seatHold1= service.findAndHoldSeats(7000, Optional.of(1), Optional.empty(), "xyz@hotmail.com");
    	assertNull(seatHold1);     
        
    }

	/**
	 * Test findAndHoldSeats method with expired held
	 */
    @Test
    public void findAndHoldSeatsExpireTest() throws Exception {
    	TicketService service = new TicketServiceImpl(mockDao);
    	
    	SeatHold seatHold1= service.findAndHoldSeats(10, Optional.of(1), Optional.of(4), "xyz@hotmail.com");
    	assertEquals(seatHold1.getHoldSet().size(), 10);
        
        int seatNumAvailable1 = service.numSeatsAvailable(Optional.ofNullable(null));
        assertEquals(seatNumAvailable1, 6240);      
        
        try {
			Thread.sleep(40000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        int seatNumAvailable2 = service.numSeatsAvailable(Optional.ofNullable(null));
        assertEquals(seatNumAvailable2, 6250); 
    }
    
	/**
	 * Test findAndHoldSeats method with concurrent holds and Exception
	 */
    @Test
    public void findAndHoldSeatsConcurrentExceptionTest() throws Exception {
    	TicketService service1 = new TicketServiceImpl(mockDao);
    	TicketService service2 = new TicketServiceImpl(mockDao);
    	
    	SeatHold seatHold1= service1.findAndHoldSeats(6000, Optional.of(1), Optional.empty(), "xyz@hotmail.com");
    	SeatHold seatHold2= service2.findAndHoldSeats(1000, Optional.of(1), Optional.empty(), "xyz@hotmail.com");
    	assertEquals(seatHold1.getHoldSet().size(), 6000);
    	assertNull(seatHold2);     
        
    }
    
	/**
	 * Test findAndHoldSeats method with concurrent holds
	 */
    @Test
    public void findAndHoldSeatsConcurrentTest() throws Exception {
    	TicketService service1 = new TicketServiceImpl(mockDao);
    	TicketService service2 = new TicketServiceImpl(mockDao);
    	
    	SeatHold seatHold1= service1.findAndHoldSeats(6000, Optional.of(1), Optional.empty(), "xyz@hotmail.com");
    	SeatHold seatHold2= service2.findAndHoldSeats(100, Optional.of(1), Optional.empty(), "xyz@hotmail.com");
    	assertEquals(seatHold1.getHoldSet().size(), 6000);
    	assertEquals(seatHold2.getHoldSet().size(), 100);     
        
    }

	/**
	 * Test reserveSeats method with valid input
	 */
    @Test
    public void reserveSeatsTest() throws Exception {
        TicketService service = new TicketServiceImpl(mockDao);
        SeatHold seatHold= service.findAndHoldSeats(60, Optional.of(1), Optional.empty(), "xyz@hotmail.com");
		String confirmation = service.reserveSeats(seatHold.getHoldId(), seatHold.getCustomerEmail());
		assertNotNull(confirmation);

    }
    
	/**
	 * Test reserveSeats method with Exception
	 */
    @Test
    public void reserveSeatsExceptionTest() throws Exception {
        TicketService service = new TicketServiceImpl(mockDao);
        SeatHold seatHold= service.findAndHoldSeats(60, Optional.of(1), Optional.empty(), "xyz@hotmail.com");
		String confirmation = service.reserveSeats(seatHold.getHoldId(), "abc@hotmail.com");
		assertNull(confirmation);

    }
    
	/**
	 * Test reserveSeats method with duplicate reservations
	 */
    @Test
    public void reserveSeatsRepeatTest() throws Exception {
        TicketService service = new TicketServiceImpl(mockDao);
        SeatHold seatHold= service.findAndHoldSeats(60, Optional.of(1), Optional.empty(), "xyz@hotmail.com");
		String confirmation1 = service.reserveSeats(seatHold.getHoldId(), seatHold.getCustomerEmail());
		String confirmation2 = service.reserveSeats(seatHold.getHoldId(), seatHold.getCustomerEmail());
		assertNotNull(confirmation1);
		assertNull(confirmation2);

    }

	/**
	 * Test reserveSeats method with concurrent reservations
	 */
    @Test
    public void reserveSeatsConcurrentExceptionTest() throws Exception {
    	TicketService service1 = new TicketServiceImpl(mockDao);
    	TicketService service2 = new TicketServiceImpl(mockDao);
    	
    	SeatHold seatHold= service1.findAndHoldSeats(60, Optional.of(1), Optional.empty(), "xyz@hotmail.com");
    	
    	String confirmation1 = service1.reserveSeats(seatHold.getHoldId(), seatHold.getCustomerEmail());
		String confirmation2 = service2.reserveSeats(seatHold.getHoldId(), seatHold.getCustomerEmail());
		assertNotNull(confirmation1);
		assertNull(confirmation2);     
        
    }

}