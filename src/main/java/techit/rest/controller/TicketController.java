package techit.rest.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import techit.model.Ticket;
import techit.model.Ticket.Priority;
import techit.model.Ticket.Status;
import techit.model.Unit;
import techit.model.Update;
import techit.model.User;
import techit.model.User.Type;
import techit.model.dao.TicketDao;
import techit.model.dao.UpdateDao;
import techit.model.dao.UserDao;
import techit.rest.error.RestException;

@RestController
public class TicketController {

	@Autowired
	private TicketDao ticketDao;

	@Autowired
	private UpdateDao updateDao;

	@Autowired
	private UserDao userDao;

	@RequestMapping(value = "/ticket/{ticketId}", method = RequestMethod.GET)
	public Ticket getTicket(@ModelAttribute("currentUser") User currentUser, @PathVariable("ticketId") Long id) {
		if (currentUser == null)
			throw new RestException(403, "Unauthorized: Insufficient Privilege");

		Ticket ticket = ticketDao.getTicket(id);
		if (ticket == null)
			throw new RestException(404, "Resource Not Found: No such Ticket exists");

		currentUser = userDao.getUser(currentUser.getId());
		Unit unit = ticket.getUnit();
		if (currentUser.getType() == Type.ADMIN
				|| (currentUser.getType() == Type.SUPERVISOR && unit.getSupervisors().contains(currentUser))
				|| (currentUser.getType() == Type.TECHNICIAN && unit.getTechnicians().contains(currentUser))
				|| (currentUser.getType() == Type.REGULAR && ticket.getCreatedBy().getId() == currentUser.getId())) {
			return ticket;

		} else {
			throw new RestException(403, "Unauthorized: Insufficient Privilege");
		}
	}

	@RequestMapping(value = "/ticket/", method = RequestMethod.GET)
	public List<Ticket> getTickets(@ModelAttribute("currentUser") User currentUser) {
		if (currentUser == null)
			throw new RestException(403, "Unauthorized: Insufficient Privilege");

		/*
		 * 1. admin -> all 2. normal -> only tickets created by current user
		 */

		if (currentUser.getType() == Type.ADMIN) {
			return ticketDao.getTickets();

		} else if (currentUser.getType() == Type.REGULAR) {
			return ticketDao.getTicketsCreatedBy(currentUser);

		} else {
			throw new RestException(403, "Unauthorized: Insufficient Privilege");
		}
	}

	@RequestMapping(value = "/ticket/", method = RequestMethod.POST)
	public Ticket addTicket(@ModelAttribute("currentUser") User currentUser, @RequestBody Ticket ticket) {
		if (currentUser == null)
			throw new RestException(403, "Unauthorized: Insufficient Privilege");

		if (StringUtils.isEmpty(ticket.getCreatedForEmail()) || StringUtils.isEmpty(ticket.getSubject())
				|| StringUtils.isEmpty(ticket.getUnit()))
			throw new RestException(400, "Missing CreatedBy, CreatedForEmail, Subject or Unit.");

		ticket.setCreatedBy(currentUser);
		return ticketDao.saveTicket(ticket);
	}

	@RequestMapping(value = "/ticket/{ticketId}", method = RequestMethod.PUT)
	public Ticket updateTicket(@ModelAttribute("currentUser") User currentUser, @PathVariable("ticketId") Long ticketId,
			@RequestBody Ticket ticket) {

		if (currentUser == null)
			throw new RestException(403, "Unauthorized: Insufficient Privilege");

		Ticket t = ticketDao.getTicket(ticketId);

		if (t == null)
			throw new RestException(404, "Resource Not Found: No such Ticket exists");

		if (currentUser.getType() != Type.ADMIN && t.getCreatedBy().getId() != currentUser.getId())
			throw new RestException(403, "Unauthorized: Insufficient Privilege");

		if (ticket == null || StringUtils.isEmpty(ticket.getCreatedForEmail())
				|| StringUtils.isEmpty(ticket.getSubject()))
			throw new RestException(400, "Bad Request: createdForEmail, subject or Ticket not found");

		// replace only selected fields into original ticket to prevent user from
		// changing fields like technicians, updates, priority, status, dates, etc.
		t.setSubject(ticket.getSubject());
		t.setDetails(ticket.getDetails());
		t.setCreatedForDepartment(ticket.getCreatedForDepartment());
		t.setCreatedForEmail(ticket.getCreatedForEmail());
		t.setCreatedForName(ticket.getCreatedForName());
		t.setCreatedForPhone(ticket.getCreatedForPhone());
		// t.setDateUpdated(Calendar.getInstance().getTime());
		t.setLocation(ticket.getLocation());

		return ticketDao.saveTicket(t);
	}

	@RequestMapping(value = "/ticket/{ticketId}/technicians", method = RequestMethod.GET)
	public List<User> getTechnicians(@ModelAttribute("currentUser") User currentUser,
			@PathVariable("ticketId") Long ticketId) {
		if (currentUser == null)
			throw new RestException(403, "Unauthorized: Insufficient Privilege");

		Ticket t = ticketDao.getTicket(ticketId);
		if (t == null)
			throw new RestException(404, "Resource Not Found: No such Ticket exists");

		currentUser = userDao.getUser(currentUser.getId());

		if (currentUser.getType() == Type.ADMIN
				|| (currentUser.getType() == Type.SUPERVISOR && t.getUnit().getSupervisors().contains(currentUser))
				|| (currentUser.getType() == Type.TECHNICIAN && t.getUnit().getTechnicians().contains(currentUser))
				|| (currentUser.getType() == Type.REGULAR && t.getCreatedBy().equals(currentUser))) {
			return t.getTechnicians();
		} else
			throw new RestException(403, "Unauthorized: Insufficient Privilege");
	}

	@RequestMapping(value = "/ticket/{ticketId}/technicians/{userId}", method = RequestMethod.PUT)
	public Ticket assignTechnicians(@ModelAttribute("currentUser") User currentUser,
			@PathVariable("ticketId") Long ticketId, @PathVariable("userId") Long userId) {
		if (currentUser == null)
			throw new RestException(403, "Unauthorized: Insufficient Privilege");

		Ticket t = ticketDao.getTicket(ticketId);
		if (t == null)
			throw new RestException(404, "Resource Not Found: No such Ticket exists");

		currentUser = userDao.getUser(currentUser.getId());

		if ((currentUser.getType() == Type.SUPERVISOR && t.getUnit().getSupervisors().contains(currentUser))
				|| (currentUser.getType() == Type.TECHNICIAN && t.getUnit().getTechnicians().contains(currentUser))) {

			User tech = userDao.getUser(userId);
			if (tech == null || !t.getUnit().getTechnicians().contains(tech))
				throw new RestException(404, "Resource Not Found: No such Technician exists within the unit");

			t.getTechnicians().add(tech);
			return t;
		} else
			throw new RestException(403, "Unauthorized: Insufficient Privilege");
	}

	@RequestMapping(value = "/ticket/{ticketId}/status/{status}", method = RequestMethod.PUT)
	public Update setStatus(@ModelAttribute("currentUser") User currentUser, @PathVariable("ticketId") Long ticketId,
			@PathVariable("status") String statusVal, @RequestBody Map<String, String> map) {
		if (currentUser == null)
			throw new RestException(403, "Unauthorized: Insufficient Privilege");

		Ticket t = ticketDao.getTicket(ticketId);
		if (t == null)
			throw new RestException(404, "Resource Not Found: No such Ticket exists");

		currentUser = userDao.getUser(currentUser.getId());

		if ((currentUser.getType() == Type.SUPERVISOR && t.getUnit().getSupervisors().contains(currentUser))
				|| (currentUser.getType() == Type.TECHNICIAN && t.getTechnicians().contains(currentUser))) {

			Update update = new Update();
			update.setDate(Calendar.getInstance().getTime());
			update.setTechnician(currentUser);
			update.setTicket(t);

			Status status;
			try {
				status = Status.valueOf(statusVal.toUpperCase());
			} catch (Exception e) {
				throw new RestException(400,
						"Bad Request: Invalid status. Expected COMPLETED, CLOSED, OPEN, ASSIGNED or ONHOLD");
			}

			switch (status) {
			case COMPLETED:
			case CLOSED:
				// message check
				if (!map.containsKey("message") || StringUtils.isEmpty(map.get("message")))
					throw new RestException(400, "Bad Request: Please provide message to change to this status");

				update.setDetails("Status updated to :" + status + ". Message:" + map.get("message"));
				break;

			case OPEN:
			case ASSIGNED:
			case ONHOLD:
				// no message check, set default message
				update.setDetails("Status updated to :" + status);
				break;
			}

			return updateDao.saveUpdate(update);
		} else
			throw new RestException(403, "Unauthorized: Insufficient Privilege");
	}

	@RequestMapping(value = "/ticket/{ticketId}/priority/{priority}", method = RequestMethod.PUT)
	public Ticket setPriority(@ModelAttribute("currentUser") User currentUser, @PathVariable("ticketId") Long ticketId,
			@PathVariable("priority") String priorityVal) {
		if (currentUser == null)
			throw new RestException(403, "Unauthorized: Insufficient Privilege");

		Ticket t = ticketDao.getTicket(ticketId);
		if (t == null)
			throw new RestException(404, "Resource Not Found: No such Ticket exists");

		currentUser = userDao.getUser(currentUser.getId());

		if (currentUser.getType() == Type.SUPERVISOR && t.getUnit().getSupervisors().contains(currentUser)) {
			Priority priority;
			try {
				priority = Priority.valueOf(priorityVal.toUpperCase());
			} catch (Exception e) {
				throw new RestException(400, "Bad Request: Invalid priority. Expected HIGH, LOW or MEDIUM");
			}

			switch (priority) {
			case HIGH:
			case LOW:
			case MEDIUM:
				t.setPriority(priority);
				break;
			}
			return ticketDao.saveTicket(t);
		} else
			throw new RestException(403, "Unauthorized: Insufficient Privilege");
	}

	@RequestMapping(value = "/ticket/{ticketId}/updates", method = RequestMethod.POST)
	public Update addUpdate(@ModelAttribute("currentUser") User currentUser, @PathVariable("ticketId") Long ticketId,
			@RequestBody Update update) {
		if (currentUser == null)
			throw new RestException(403, "Unauthorized: Insufficient Privilege");

		Ticket t = ticketDao.getTicket(ticketId);
		if (t == null)
			throw new RestException(404, "Resource Not Found: No such Ticket exists");

		currentUser = userDao.getUser(currentUser.getId());

		if (currentUser.getType() == Type.ADMIN
				|| (currentUser.getType() == Type.SUPERVISOR && t.getUnit().getSupervisors().contains(currentUser))
				|| (currentUser.getType() == Type.TECHNICIAN && t.getTechnicians().contains(currentUser))
				|| (currentUser.getType() == Type.REGULAR && t.getCreatedBy().equals(currentUser))) {

			if (update == null || StringUtils.isEmpty(update.getDetails()))
				throw new RestException(400, "Bad Request: Please provide details for the update");

			update.setDate(Calendar.getInstance().getTime());
			update.setTechnician(currentUser);
			update.setTicket(t);

			return updateDao.saveUpdate(update);
		} else
			throw new RestException(403, "Unauthorized: Insufficient Privilege");
	}

}
