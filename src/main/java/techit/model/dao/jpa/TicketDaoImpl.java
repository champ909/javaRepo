package techit.model.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import techit.model.Ticket;
import techit.model.Unit;
import techit.model.User;
import techit.model.dao.TicketDao;

@Repository
public class TicketDaoImpl implements TicketDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Ticket getTicket(Long id) {
		return entityManager.find(Ticket.class, id);
	}

	@Override
	public List<Ticket> getTickets() {
		return entityManager.createQuery("from Ticket order by id", Ticket.class).getResultList();
	}

	@Override
	@Transactional
	public Ticket saveTicket(Ticket ticket) {
		return entityManager.merge(ticket);
	}

	@Override
	public List<Ticket> getTickets(Unit unit) {
		return entityManager.createQuery("from Ticket where unit=:unit", Ticket.class)
				.setParameter("unit", unit).getResultList();
	}

	// Can be fetched from User obj
//	@Override
//	public List<Ticket> getTickets(User technician) {
//		return entityManager.createQuery("from Ticket t join fetch t.technicians u where u=:technician", Ticket.class)
//				.setParameter("technician", technician).getResultList();
//	}



}
