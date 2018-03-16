package techit.model.dao;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

import techit.model.User;

@Test(groups = "UserDaoTest")
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserDaoTest extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	UserDao userDao;

	@Test
	public void getUser() {
		assert userDao.getUser(1L).getUsername().equalsIgnoreCase("techit");
	}

	@Test
	public void getUserWithUsername() {
		assert userDao.getUser("techit").getId() != null;
	}

	@Test
	public void getUsers() {
		assert userDao.getUsers().size() >= 1;
	}

	@Test
	public void saveUser() {
		User user = new User();
		user.setUsername("Tom" + new Date().toString());
		user.setPassword("abcd");
		user.setEmail("googl@csajsj.com");
		user.setDepartment("Cs departmetnasda");
		user.setEnabled(true);
		user.setPhone("6262029379");
		user.setFirstName("jay");
		user.setLastName("patel");
		user = userDao.saveUser(user);
		assert user.getId() != null;
	}

}
