package techit.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import techit.model.Ticket;
import techit.model.User;
import techit.model.User.Type;
import techit.model.dao.TicketDao;
import techit.model.dao.UserDao;
import techit.rest.error.RestException;

@RestController
public class UserController {

	@Autowired
	private UserDao userDao;

	@Autowired
	private TicketDao ticketDao;

	@RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
	public User getUser(@PathVariable("userId") Long id, @ModelAttribute("currentUser") User user) {
		if (user == null || (user.getType() != Type.ADMIN && user.getId() != id))
			throw new RestException(403, "Unauthorized: Insufficient Privilege");
		return userDao.getUser(id);
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<User> getUsers(@ModelAttribute("currentUser") User user) {
		if (user == null || user.getType() != Type.ADMIN)
			throw new RestException(403, "Unauthorized: Insufficient Privilege");
		return userDao.getUsers();
	}

	@RequestMapping(value = "/users/{userId}", method = RequestMethod.PUT)
	public User updateUser(@ModelAttribute("currentUser") User currentUser, @RequestBody User user, @PathVariable("userId") Long userId) {
		if (user == null || userId == null)
			throw new RestException(400, "Bad Request: Missing id or username");

		if (currentUser == null || (currentUser.getType() != Type.ADMIN && currentUser.getId() != userId))
			throw new RestException(403, "Unauthorized: Insufficient Privilege");

		User userObj = userDao.getUser(userId);
		if (userObj == null)
			throw new RestException(404, "Resource Not Found");

		user.setHash(userObj.getHash());
		user.setId(userObj.getId());
		user.setUsername(userObj.getUsername());
		user.setEmail(userObj.getEmail());
		return userDao.saveUser(user);
	}

	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public User addUser(@ModelAttribute("currentUser") User currentUser, @RequestBody User user) {
		if (currentUser == null || currentUser.getType() != Type.ADMIN)
			throw new RestException(403, "Unauthorized: Insufficient Privilege");

		if (user.getUsername() == null || user.getPassword() == null)
			throw new RestException(400, "Bad Request: Missing username and/or password.");

		if (StringUtils.isEmpty(user.getEmail())) {
			user.setEmail(user.getUsername() + "@calstatela.edu");
		}

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
		user.setHash(encoder.encode(user.getPassword()));
		return userDao.saveUser(user);
	}

	@RequestMapping(value = "/users/{userId}/tickets", method = RequestMethod.GET)
	public List<Ticket> getTicketsSubmittedByUser(@ModelAttribute("currentUser") User currentUser,
			@PathVariable("userId") Long id) {
		if (currentUser == null || (currentUser.getType() != Type.ADMIN && currentUser.getId() != id))
			throw new RestException(403, "Unauthorized: Insufficient Privilege");

		return ticketDao.getTicketsCreatedBy(userDao.getUser(id));
	}

}