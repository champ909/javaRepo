package techit.model.dao;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import techit.model.Update;

@Test(groups = "UpdateDaoTest")
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UpdateDaoTest extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	UpdateDao updateDao;

	@Autowired
	TicketDao ticketDao;

	@Autowired
	UserDao userDao;

	Long updateId;

	@Test
	public void getUpdate() {
		assert updateDao.getUpdate(updateId).getTechnician().getUsername().equalsIgnoreCase("techit");
	}

	@Test
	public void getUpdates() {
		assert updateDao.getUpdates().size() >= 1;
	}

	@Test
	public void getUpdatesForTickets() {
		assert updateDao.getUpdates(ticketDao.getTicket(1L), false).size() >= 1;
	}

	@BeforeClass
	public void saveUpdate() {
		Update update = new Update();
		update.setTechnician(userDao.getUser(1l));
		update.setDetails("test details");
		update.setDate(new Date());
		update.setTicket(ticketDao.getTicket(1L));
		update = updateDao.saveUpdate(update);
		updateId = update.getId();
		assert updateId != 0;
	}

}