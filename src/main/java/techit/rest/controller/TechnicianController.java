package techit.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
public class TechnicianController {

	@Autowired
	TicketDao ticketDao;
	
	@Autowired
	UserDao userDao;
	
	@RequestMapping(value = "/technicians/{userId}/tickets", method = RequestMethod.GET)
	public List<Ticket> getTickets(@ModelAttribute("currentUser") User currentUser, @PathVariable("userId") Long userId) {
		if (currentUser == null || (currentUser.getType() != Type.ADMIN && currentUser.getId()!=userId))
			throw new RestException(403, "Unauthorized: Insufficient Privilege");

		return ticketDao.getTicketsAssignedTo(userDao.getUser(userId));
	}

}
