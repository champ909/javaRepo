package techit.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "units")
public class Unit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id; // Unit's unique id.
	
	@Column(nullable = false)
	private String name; // Name of the department.

	// A unit may have more than one supervisor. This will allow them to assign
	// temporary leads when they are gone.
	@Column()
	private String phone;
	
	@Column()
	private String location;
	
	@Column()
	private String email;

	@Column()
	private String description;
	
	@OneToMany(mappedBy = "unit")
	private List<Ticket> tickets;

	public Unit() {
		
	}

	public Unit(String name, String phone, String location, String email, String description) {
		super();
		this.name = name;
		this.phone = phone;
		this.location = location;
		this.email = email;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}
	
}
