package techit.model.dao;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

import techit.model.Ticket;
import techit.model.Unit;

@Test(groups = "TicketDaoTest")
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class TicketDaoTest extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	TicketDao ticketDao;

	@Autowired
	UnitDao unitDao;

	@Test
	public void getTicket() {
		assert ticketDao.getTicket(1L).getUsername().equalsIgnoreCase("jojo");
	}

	@Test
	public void getTickets() {
		assert ticketDao.getTickets().size() >= 1;
	}

	@Test
	public void getTicketsWithUnit() {
		Unit unit = new Unit();
		unit.setName("testunit");
		unit = unitDao.saveUnit(unit);
		Ticket ticket = new Ticket();
		ticket.setUsername("testUsername");
		ticket.setUserFirstName("testuser");
		ticket.setUserLastName("testuserln");
		ticket.setCurrentProgress(1);
		ticket.setEmail("googl@csajsj.com");
		ticket.setSubject("subject");
		ticket.setStartDate(new Date());
		ticket.setUnit(unit);
		ticket = ticketDao.saveTicket(ticket);
		assert ticketDao.getTickets(unit).size()>=1;
	}
	
	@Test
	public void saveTicket() {
		Ticket ticket = new Ticket();
		ticket.setUsername("testUsername");
		ticket.setUserFirstName("testuser");
		ticket.setUserLastName("testuserln");
		ticket.setCurrentProgress(1);
		ticket.setEmail("googl@csajsj.com");
		ticket.setSubject("subject");
		ticket.setStartDate(new Date());
		ticket = ticketDao.saveTicket(ticket);
		assert ticket.getId() != 0;
	}

}