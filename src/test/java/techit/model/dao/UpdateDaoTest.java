package techit.model.dao;

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

	Long updateId;
	@Test
	public void getUpdate() {
		assert updateDao.getUpdate(updateId).getModifier().equalsIgnoreCase("modifierTest");
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
		update.setModifier("modifierTest");
		update.setUpdateDetails("test details");
		update.setModifiedDate("12-08-2017");
		update.setTicket(ticketDao.getTicket(1L));
		update = updateDao.saveUpdate(update);
		updateId = update.getId();
		assert updateId != 0;
	}

}