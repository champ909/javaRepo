package techit.model.dao;

import java.util.List;

import techit.model.Ticket;
import techit.model.Unit;
import techit.model.User;
import techit.util.SearchableCriteria;

public interface TicketDao {

	Ticket getTicket(Long id);

	List<Ticket> getTickets();

	List<Ticket> getTicketsCreatedBy(User user);

	List<Ticket> getTicketsCreatedFor(String email);

	List<Ticket> getTicketsAssignedTo(Unit unit);

	List<Ticket> getTocketsAssignedTo(User technician);

	Ticket saveTicket(Ticket ticket);

	<T extends Object> List<T> getSearchResults(String query, Class<T> entity, List<SearchableCriteria> criteriaList);
}
