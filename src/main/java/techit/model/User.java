package techit.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "users")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, unique = true)
	private String username;

	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(nullable = false)
	private String pass;

	@Column(nullable = false)
	private boolean enabled = true;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;
	
	@Column()
	private String phone;
	
	@Column()
	private String department;
	
	@Column()
	private String email;
	
	@Column(nullable = false)
	private Position status;
	
	@ManyToOne
	private Unit unit;

	@ManyToMany
	private List<Ticket> tickets;

	// Types of users on the system.
	private enum Position {
		SYS_ADMIN(0), SUPERVISING_TECHNICIAN(1), TECHNICIAN(2), USER(3);

		private int positionValue;

		Position(int position_value) {
			this.positionValue = position_value;
		}

		public int getValue() {
			return positionValue;
		}
	};

	public User() {
	}

	// Simple constructor for regular users ( students )
	public User(long id, String firstname, String lastname, String username, String password) {
		this.id = id;
		this.firstName = firstname;
		this.lastName = lastname;
		this.username = username;
		this.status = Position.USER;
//		this.unitId = 0; // User does not belongs to any unit\
		this.pass = password;
		this.enabled = true;
	}

	// Full user paramenter constructor
	public User(long id, String firstname, String lastname, String username, String password, String phone,
			String email, String department, int position, Unit unit) {
		this.id = id;
		this.firstName = firstname;
		this.lastName = lastname;
		this.username = username;
		this.phone = phone;
		this.email = email;
		this.department = department;
		switch (position) {
		case 0:
			this.status = Position.SYS_ADMIN;
			break;
		case 1:
			this.status = Position.SUPERVISING_TECHNICIAN;
			break;
		case 2:
			this.status = Position.TECHNICIAN;
			break;
		case 3: // These assume that you want to enter a new technician
		default: // and somehow enter a number thats not intended
			this.status = Position.USER;
			break;
		}

		this.unit = unit;

		this.pass = password;
		this.enabled = true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String password) {
		this.pass = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getEmailFromUsername() {
		return username + "@calstatela.edu"; // Since username has the same
												// header as email with the
												// "@calstatela.edu" domain
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getStatus() {
		return status.getValue();
	}

	public void setStatus(int position) {
		switch (position) {
		case 0:
			this.status = Position.SYS_ADMIN;
			break;
		case 1:
			this.status = Position.SUPERVISING_TECHNICIAN;
			break;
		case 2:
			this.status = Position.TECHNICIAN;
			break;
		case 3:
			this.status = Position.USER;
			break;
		}
	}

	public String getStatusString() {
		String status = "";
		switch (getStatus()) {
		case 0:
			status = "SYSTEM ADMINISTRATOR";
			break;
		case 1:
			status = "SUPERVISING TECHNICIAN";
			break;
		case 2:
			status = "TECHNICIAN";
			break;
		case 3:
		default:
			status = "USER";
			break;
		}

		return status;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnitId(Unit unit) {
		this.unit = unit;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public void setStatus(Position status) {
		this.status = status;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	@Override
	public String toString() {
		return "[" + id + ", " + username + ", " + pass + ", " + firstName + ", " + lastName + ", " + phone
				+ ", " + email + ", " + department + ", " + status + ", " + unit.getId() + "]";
	}

}
