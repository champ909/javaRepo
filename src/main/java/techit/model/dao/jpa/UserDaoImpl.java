package techit.model.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import techit.model.Ticket;
import techit.model.User;
import techit.model.dao.UserDao;

@Repository
public class UserDaoImpl implements UserDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public User getUser(Long id) {
		return entityManager.find(User.class, id);
	}

	@Override
	public List<User> getUsers() {
		return entityManager.createQuery("from User order by id", User.class).getResultList();
	}

	@Override
	@Transactional
	public User saveUser(User user) {
		return entityManager.merge(user);
	}

	@Override
	public User getUser(String username) {
		return entityManager.createQuery("from User where username =:username", User.class)
				.setParameter("username", username).getSingleResult();
	}

	// can be fetched from Ticket obj
//	@Override
//	public List<User> getTechnicians(Ticket ticket) {
//		return entityManager.createQuery("from User u join fetch u.tickets t where t=:ticket", User.class)
//				.setParameter("ticket", ticket).getResultList();
//	}

}
