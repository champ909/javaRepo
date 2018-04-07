package techit.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import techit.model.Ticket;
import techit.model.Unit;
import techit.model.User;
import techit.model.User.Type;
import techit.model.dao.TicketDao;
import techit.model.dao.UnitDao;
import techit.model.dao.UserDao;
import techit.rest.error.RestException;

@RestController
public class UnitController {

	@Autowired
	private UnitDao unitDao;

	@Autowired
	private TicketDao ticketDao;

	@Autowired
	private UserDao userDao;

	@RequestMapping(value = "/units", method = RequestMethod.GET)
	public List<Unit> getUnits(@ModelAttribute("currentUser") User currentUser) {
		if (currentUser == null)
			throw new RestException(403, "Unauthorized: Insufficient Privilege");

		return unitDao.getUnits();
	}

	@RequestMapping(value = "/units/{unitId}/technicians", method = RequestMethod.GET)
	public List<User> getTechnicians(@ModelAttribute("currentUser") User currentUser,
			@PathVariable("unitId") Long unitId) {
		if (currentUser == null)
			throw new RestException(403, "Unauthorized: Insufficient Privilege");

		Unit unit = unitDao.getUnit(unitId);
		if (unit == null)
			throw new RestException(404, "Resource Not Found: No such Unit exists");

		return unit.getTechnicians();
	}

	@RequestMapping(value = "/units/{unitId}/tickets", method = RequestMethod.GET)
	public List<Ticket> getTickets(@ModelAttribute("currentUser") User currentUser,
			@PathVariable("unitId") Long unitId) {
		if (currentUser == null)
			throw new RestException(403, "Unauthorized: Insufficient Privilege");

		/* 
		 * 1. admin -> all
		 * 2. supervisor, tech -> all only if they belong to the unit
		 * 3. normal -> only tickets created by current user within the unit
		 */

		/*
		 * Ambiguity for the second condition.
		 *  - User has a unit with many-to-one relationship, therefore to validate:
		 *  	if((currentUser is SUPERVISOR or TECHNICIAN) and (currentUser.unit.id equals unitId)) is enough
		 *  
		 *	- However, Unit has technicians and supervisors with many-to-many relationship.
		 *	  In this case we have to check in both the lists for current user's presence.
		 *	  Therefore,
		 *		if((currentUser is SUPERVISOR and unit.supervisors contains currentUser)
		 *			or (currentUser is TECHNICIAN and unit.technicians contains currentUser))
		 */

		// get real obj from DB
		currentUser = userDao.getUser(currentUser.getId());

		Unit unit = unitDao.getUnit(unitId);
		if (unit == null)
			throw new RestException(404, "Resource Not Found: No such Unit exists");

		if (currentUser.getType() == Type.ADMIN
				|| (currentUser.getType() == Type.SUPERVISOR && unit.getSupervisors().contains(currentUser))
				|| (currentUser.getType() == Type.TECHNICIAN && unit.getTechnicians().contains(currentUser))) {
			return ticketDao.getTicketsAssignedTo(unit);

		} else if (currentUser.getType() == Type.REGULAR) {
			return ticketDao.getTicketsCreatedBy(currentUser, unit);

		} else {
			throw new RestException(403, "Unauthorized: Insufficient Privilege");
		}
	}

	@RequestMapping(value = "/units", method = RequestMethod.POST)
	public Unit addUnit(@ModelAttribute("currentUser") User currentUser, @RequestBody Unit unit) {
		if (currentUser == null || currentUser.getType() != Type.ADMIN)
			throw new RestException(403, "Unauthorized: Insufficient Privilege");

		if (StringUtils.isEmpty(unit.getName()) || StringUtils.isEmpty(unit.getEmail()))
			throw new RestException(400, "Bad Request: Missing Unit name and/or email field.");

		return unitDao.saveUnit(unit);
	}

}