package techit.rest.controller;

import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import techit.model.Ticket;
import techit.model.dao.TicketDao;
import techit.rest.error.RestException;
import techit.util.SearchableCriteria;

@RestController
public class SearchController {

	@Autowired
	private TicketDao ticketDao;

	private SearchableCriteria searchUtil = new SearchableCriteria();

	@RequestMapping(value = "/search/ticket/", method = RequestMethod.POST)
	public List<Ticket> searchTickets(@RequestBody List<SearchableCriteria> criteriaList) {

		if (criteriaList == null || criteriaList.isEmpty())
			throw new RestException(404, "Bad Request: Invaid Criteria");

		return getSearchResults(Ticket.class, criteriaList);
	}

	private <T extends Object> List<T> getSearchResults(Class<T> entity, List<SearchableCriteria> criteriaList) {
		String queryString = "from " + entity.getSimpleName() + " where ";

		// "createdForEmail = :email";
		int total = criteriaList.size();
		for (SearchableCriteria criteria : criteriaList) {
			queryString += criteria.getField();
			switch (criteria.getType()) {
			case CONTAINS:
				queryString += " LIKE %:" + criteria.getField() + "%";
				break;

			case EQUALS:
				queryString += " = :" + criteria.getField();
				break;
			}
			total--;
			if (total > 0)
				queryString += " and ";
		}

		// Query query;
		// try {
		// query = entityManager.createQuery(queryString, entity);
		// for (SearchableCriteria criteria : criteriaList) {
		// query.setParameter(criteria.field, criteria.value);
		// }
		//
		// return query.getResultList();
		// }catch(Exception e) {
		// e.printStackTrace();
		// return null;
		// }

		return ticketDao.getSearchResults(queryString, entity, criteriaList);
	}

}
