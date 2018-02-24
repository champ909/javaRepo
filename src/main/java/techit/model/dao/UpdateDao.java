package techit.model.dao;

import java.util.List;

import techit.model.Ticket;
import techit.model.Update;
import techit.model.User;

public interface UpdateDao {

	Update getUpdate(Long id);

	List<Update> getUpdates();

	List<Update> getUpdates(Ticket ticket, boolean orderByLastUpdate);

	Update saveUpdate(Update update);
}
