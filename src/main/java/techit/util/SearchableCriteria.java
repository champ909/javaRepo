package techit.util;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import techit.model.Ticket;
import techit.model.dao.TicketDao;

@Repository
public class SearchableCriteria {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	TicketDao ticketDao;

	public enum Type {
		CONTAINS, EQUALS
	}

	private String value;
	private String field;
	private Type type;

	public SearchableCriteria() {

	}

	public SearchableCriteria(String field, String value, Type type) {
		super();
		this.value = value;
		this.field = field;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

}
