package techit.rest.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import techit.model.Update;
import techit.model.dao.TicketDao;
import techit.model.dao.UpdateDao;

@RestController
public class UpdateController {

	@Autowired
	UpdateDao updateDao;

	@Autowired
	TicketDao ticketDao;

	@RequestMapping(value = "/update/{ticketId}", method = RequestMethod.GET)
	public List<Update> getUpdate(@PathVariable Long ticketId) {
		return updateDao.getUpdates(ticketDao.getTicket(ticketId), true);
	}

	@RequestMapping(value = "/update/{ticketId}/{orderBy}", method = RequestMethod.GET)
	public List<Update> getUpdateOrderBy(@PathVariable Long ticketId, @PathVariable boolean orderBy) {
		return updateDao.getUpdates(ticketDao.getTicket(ticketId), orderBy);
	}

}