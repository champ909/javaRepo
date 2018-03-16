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

	@Autowired
	UserDao userDao;

	@Test
	public void getTicket() {
		assert ticketDao.getTicket(1L).getCreatedBy().getUsername().equalsIgnoreCase("jojo");
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
		ticket.setCreatedBy(userDao.getUser(1l));
		ticket.setSubject("subject");
		ticket.setCreatedForEmail("googl@csajsj.com");
		ticket.setDateCreated(new Date());
		ticket.setUnit(unit);
		ticket = ticketDao.saveTicket(ticket);
		assert ticketDao.getTicketsAssignedTo(unit).size() >= 1;
	}
}