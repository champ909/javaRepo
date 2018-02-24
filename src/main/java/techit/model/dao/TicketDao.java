package techit.model.dao;

import java.util.List;

import techit.model.Ticket;
import techit.model.Unit;
import techit.model.User;

public interface TicketDao {

	Ticket getTicket(Long id);

	List<Ticket> getTickets();

	Ticket saveTicket(Ticket ticket);
	
	List<Ticket> getTickets(Unit unit);

	// Can be fetched from User obj
//	List<Ticket> getTickets(User technician);
}
