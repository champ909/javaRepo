package techit.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;
import techit.model.Update;

@Test(groups = "UpdateDaoTest")
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class UpdateDaoTest extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	UpdateDao updateDao;

	@Test
	public void getUpdate()
	{
		assert updateDao.getUpdate( 1L ).getModifier().equalsIgnoreCase( "admin" );
	}

	@Test
	public void getUpdates()
	{
		assert updateDao.getUpdates().size() >= 2;
	}
	
	@Test
	public void saveUpdate()
	{
		Update update = new Update();
		update.setModifier("modifierTest");
		update = updateDao.saveUpdate( update );

		assert update.getId() != 0;
	}
	
}