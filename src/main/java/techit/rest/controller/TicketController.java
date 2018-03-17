package techit.rest.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import techit.model.Ticket;
import techit.model.Update;
import techit.model.User;
import techit.model.dao.TicketDao;
import techit.model.dao.UpdateDao;
import techit.model.dao.UserDao;
import techit.rest.error.RestException;
import techit.util.GsonUtil;

@RestController
public class TicketController {

	@Autowired
	private TicketDao ticketDao;

	@Autowired
	private UpdateDao updateDao;

	@Autowired
	private UserDao userDao;

	@RequestMapping(value = "/ticket/{id}", method = RequestMethod.GET)
	public Ticket getTicket(@PathVariable Long id) {
		return ticketDao.getTicket(id);
	}

	@RequestMapping(value = "/ticket/", method = RequestMethod.GET)
	public List<Ticket> getTickets() {
		return ticketDao.getTickets();
	}

	@RequestMapping(value = "/ticket/assign", method = RequestMethod.POST)
	public Object assignTickets(@RequestBody Map<String, Object> request) {
		if (StringUtils.isEmpty(request.get("ticket")) || StringUtils.isEmpty(request.get("technician")))
			throw new RestException(404, "Bad Request: Technician or Ticket not found");

		Ticket ticket = ticketDao.getTicket(Long.parseLong(request.get("ticket").toString()));
		User tech = userDao.getUser(Long.parseLong(request.get("technician").toString()));

		if (ticket == null || tech == null)
			throw new RestException(404, "Bad Request: No such Technician or Ticket found");

		List<User> techs = ticket.getTechnicians();
		techs.add(tech);
		ticket.setTechnicians(techs);

		Update update = new Update();
		Date date = Calendar.getInstance().getTime();
		update.setDate(date);
		update.setDetails("Ticket ID:" + ticket.getId() + " assigned to User:" + tech.getUsername());
		update.setTechnician(tech);
		update.setTicket(ticket);

		ticketDao.saveTicket(ticket);
		updateDao.saveUpdate(update);

		return new ResponseEntity<Object>("Operation Successful", HttpStatus.OK);
	}

	@RequestMapping(value = "/ticket/", method = RequestMethod.PUT)
	public Update updateTicket(@RequestBody Map<String, Object> update) {

		if (StringUtils.isEmpty(update.get("details")) || StringUtils.isEmpty(update.get("modifiedBy"))
				|| StringUtils.isEmpty(update.get("ticket")))
			throw new RestException(404, "Bad Request: Details, Technician or Ticket not found");

		User user = userDao.getUser(Long.parseLong(update.get("modifiedBy").toString()));
		Ticket newTicket = GsonUtil.getInstance().fromJson(update.get("ticket").toString(), Ticket.class);

		Ticket ticket = ticketDao.getTicket(newTicket.getId());

		if (user == null || ticket == null)
			throw new RestException(404, "Bad Request: No such User or Ticket found");
		if (StringUtils.isEmpty(newTicket.getCreatedForName()) && StringUtils.isEmpty(newTicket.getCreatedForEmail())
				&& StringUtils.isEmpty(newTicket.getCreatedForPhone())
				&& StringUtils.isEmpty(newTicket.getCreatedForDepartment())
				&& StringUtils.isEmpty(newTicket.getSubject()) && StringUtils.isEmpty(newTicket.getDetails())
				&& StringUtils.isEmpty(newTicket.getLocation()) && StringUtils.isEmpty(newTicket.getUnit())
				&& StringUtils.isEmpty(newTicket.getStatus()) && StringUtils.isEmpty(newTicket.getPriority())) {
			throw new RestException(404, "Bad Request: No Modification provided");
		} else {
			ticket.setCreatedForName(StringUtils.isEmpty(newTicket.getCreatedForName()) ? ticket.getCreatedForName()
					: newTicket.getCreatedForName());
			ticket.setCreatedForEmail(StringUtils.isEmpty(newTicket.getCreatedForEmail()) ? ticket.getCreatedForEmail()
					: newTicket.getCreatedForEmail());
			ticket.setCreatedForPhone(StringUtils.isEmpty(newTicket.getCreatedForPhone()) ? ticket.getCreatedForPhone()
					: newTicket.getCreatedForPhone());
			ticket.setCreatedForDepartment(
					StringUtils.isEmpty(newTicket.getCreatedForDepartment()) ? ticket.getCreatedForDepartment()
							: newTicket.getCreatedForDepartment());
			ticket.setSubject(
					StringUtils.isEmpty(newTicket.getSubject()) ? ticket.getSubject() : newTicket.getSubject());
			ticket.setDetails(
					StringUtils.isEmpty(newTicket.getDetails()) ? ticket.getDetails() : newTicket.getDetails());
			ticket.setLocation(
					StringUtils.isEmpty(newTicket.getLocation()) ? ticket.getLocation() : newTicket.getLocation());
			ticket.setUnit(StringUtils.isEmpty(newTicket.getUnit()) ? ticket.getUnit() : newTicket.getUnit());
			ticket.setStatus(StringUtils.isEmpty(newTicket.getStatus()) ? ticket.getStatus() : newTicket.getStatus());
			ticket.setPriority(
					StringUtils.isEmpty(newTicket.getPriority()) ? ticket.getPriority() : newTicket.getPriority());
		}

		Date date = Calendar.getInstance().getTime();
		ticket.setDateUpdated(date);

		Update newUpdate = new Update();
		newUpdate.setDate(date);
		newUpdate.setDetails(update.get("details").toString());
		newUpdate.setTechnician(user);
		newUpdate.setTicket(ticket);

		ticketDao.saveTicket(ticket);
		return updateDao.saveUpdate(newUpdate);
	}
}
