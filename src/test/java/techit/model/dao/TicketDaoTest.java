package techit.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;
import techit.model.Ticket;

@Test(groups = "TicketDaoTest")
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class TicketDaoTest extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	TicketDao ticketDao;

	@Test
	public void getTicket()
	{
		assert ticketDao.getTicket( 1L ).getUsername().equalsIgnoreCase( "admin" );
	}

	@Test
	public void getTickets()
	{
		assert ticketDao.getTickets().size() >= 2;
	}
	
	@Test
	public void saveTicket()
	{
		Ticket ticket = new Ticket();
		ticket.setUsername("testUsername");
		ticket = ticketDao.saveTicket( ticket );

		assert ticket.getId() != 0;
	}
	
}