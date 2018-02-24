package techit.model.dao;

import java.util.List;

import techit.model.Ticket;
import techit.model.User;

public interface UserDao {

	User getUser(Long id);

	List<User> getUsers();

	User saveUser(User user);

	User getUser(String username);				// validate the password from User object at service class

	// Can be fetched from Ticket obj
//	List<User> getTechnicians(Ticket ticket);
}
