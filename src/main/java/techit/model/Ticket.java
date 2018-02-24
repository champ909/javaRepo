package techit.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tickets")
public class Ticket implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id; // Ticket's unique id.

	@Column(nullable = false)
	private String username; // The user who requested the ticket.

	@Column(nullable = false)
	private String userFirstName; // Requester's first name

	@Column(nullable = false)
	private String userLastName; // Requester's last name

	@ManyToMany(mappedBy = "tickets")
	private List<User> technicians; // List of all technicians

	@Column(nullable = false)
	private Progress currentProgress; // Current progress of the ticket

	@Column()
	private Priority currentPriority; // Importance or level of urgency of the ticket

	@Column()
	private String phone; // Requestor's phone

	@Column(nullable = false)
	private String email; // Requestor's email. May be different from the User's login email.

	@Column()
	private String department; // Department that is related to the ticket or the person who created the ticket

	@ManyToOne
	private Unit unit; // The unit that was assigned to the ticket.

	@Column(nullable = false)
	private String subject; // Subject of the ticket.

	@Column()
	private String details; // Text concerning the project.

	@Column(nullable = false)
	private Date startDate; // Project's starting date.

	@Column()
	private String startDateTime; // Time of when the ticket was created.

	@Column()
	private Date endDate; // When the project was completed.

	@Column()
	private Date lastUpdated; // Last date where changes were made to the ticket (edits, technician updates,
								// etc.)

	@Column()
	private String lastUpdatedTime; // Same as lastUpdated but this is for the time changes were made.

	@Column()
	private String ticketLocation; // Location where the project is.

	@OneToMany(mappedBy = "ticket")
	private List<Update> updates; // List of all updates that was made to the ticket.
	// Needs more work...

	@Column()
	private String completionDetails; // Information pertaining vendors, cost,
										// materials used.
	// Type of progresses

	private enum Progress {
		OPEN(0), INPROGRESS(1), ONHOLD(2), COMPLETED(3), CLOSED(4);

		private int progress;

		Progress(int progress) {
			this.progress = progress;
		};

		public String getProgressValue() {
			String progress = "";
			switch (this.progress) {
			case 0:
				progress = "OPEN";
				break;
			case 1:
				progress = "IN PROGRESS";
				break;
			case 2:
				progress = "ON HOLD";
				break;
			case 3:
				progress = "COMPLETED";
				break;
			case 4:
				progress = "CLOSED";
				break;
			}
			return progress;
		}

	};

	// Type of priority
	private enum Priority {
		NA(0), LOW(1), MEDIUM(2), HIGH(3);
		private int priority;

		Priority(int priority) {
			this.priority = priority;
		}

		public String getPriorityValue() {
			String priority = "";
			switch (this.priority) {
			case 0:
				priority = "NOT ASSIGNED";
				break;
			case 1:
				priority = "LOW";
				break;
			case 2:
				priority = "MEDIUM";
				break;
			case 3:
				priority = "HIGH";
				break;
			}
			return priority;
		}

		public int getPriorityNumericValue() {
			return this.priority;
		}
	};

	public Ticket() {

	}

	// Full constructor for every field, probably need when pulling existing
	// data from database
	public Ticket(Long id, String username, String firstName, String lastName, List<User> technician, String phone,
			String email, String department, int progress, int priority, Unit unit, String subject, String details,
			Date startDate, String startDateTime, Date endDate, Date lastUpdated, String lastUpdatedTime,
			String ticketLocation, List<Update> updates, String completionDetails) {
		this.id = id;
		this.username = username;
		this.userFirstName = firstName;
		this.userLastName = lastName;
		this.technicians = technician;
		this.phone = phone;
		this.email = email;
		this.department = department;

		switch (progress) {
		case 0:
		default:
			this.currentProgress = Progress.OPEN;
			break;
		case 1:
			this.currentProgress = Progress.INPROGRESS;
			break;
		case 2:
			this.currentProgress = Progress.ONHOLD;
			break;
		case 3:
			this.currentProgress = Progress.COMPLETED;
			break;
		case 4:
			this.currentProgress = Progress.CLOSED;
			break;
		}

		switch (priority) {
		case 0:
		default:
			this.currentPriority = Priority.NA;
			break;
		case 1:
			this.currentPriority = Priority.LOW;
			break;
		case 2:
			this.currentPriority = Priority.MEDIUM;
			break;
		case 3:
			this.currentPriority = Priority.HIGH;
			break;
		}

		this.unit = unit;
		this.subject = subject;
		this.details = details;
		this.startDate = startDate;
		this.startDateTime = startDateTime;
		this.endDate = endDate;
		this.lastUpdated = lastUpdated;
		this.lastUpdatedTime = lastUpdatedTime;
		this.updates = updates;
		this.ticketLocation = ticketLocation;
		this.completionDetails = completionDetails;

	}

	// Constructor without updates list and technicians list
	public Ticket(Long id, String username, String userFirstName, String userLastName, String phone, String email,
			String department, int priority, Unit unit, String subject, String details, Date startDate,
			Date lastUpdated, String ticketLocation) {
		this.id = id;
		this.username = username;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
		this.phone = phone;
		this.email = email;
		this.department = department;
		this.unit = unit;
		this.subject = subject;
		this.details = details;
		this.startDate = startDate;
		this.lastUpdated = lastUpdated;
		this.ticketLocation = ticketLocation;

		switch (priority) {
		case 0:
		default:
			this.currentPriority = Priority.NA;
			break;
		case 1:
			this.currentPriority = Priority.LOW;
			break;
		case 2:
			this.currentPriority = Priority.MEDIUM;
			break;
		case 3:
			this.currentPriority = Priority.HIGH;
			break;
		}
	}

	// Constructor without updates list
	public Ticket(Long id, String username, String userFirstName, String userLastName, String phone, String email,
			String department, int priority, Unit unit, String subject, String details, Date startDate,
			Date lastUpdated, String ticketLocation, List<User> technicianList) {
		this.id = id;
		this.username = username;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
		this.phone = phone;
		this.email = email;
		this.unit = unit;
		this.subject = subject;
		this.details = details;
		this.startDate = startDate;
		this.lastUpdated = lastUpdated;
		this.ticketLocation = ticketLocation;
		this.technicians = technicianList;

		switch (priority) {
		case 0:
		default:
			this.currentPriority = Priority.NA;
			break;
		case 1:
			this.currentPriority = Priority.LOW;
			break;
		case 2:
			this.currentPriority = Priority.MEDIUM;
			break;
		case 3:
			this.currentPriority = Priority.HIGH;
			break;
		}
		this.department = department;
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

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public List<User> getTechnicians() {
		return technicians;
	}

	public void setTechnicians(List<User> technicians) {
		this.technicians = technicians;
	}

	public Progress getCurrentProgress() {
		return currentProgress;
	}

	public void setCurrentProgress(int progress) {
		switch (progress) {
		case 0:
		default:
			this.currentProgress = Progress.OPEN;
			break;
		case 1:
			this.currentProgress = Progress.INPROGRESS;
			break;
		case 2:
			this.currentProgress = Progress.ONHOLD;
			break;
		case 3:
			this.currentProgress = Progress.COMPLETED;
			break;
		case 4:
			this.currentProgress = Progress.CLOSED;
			break;
		}
	}

	public Priority getCurrentPriority() {
		return currentPriority;
	}

	public void setCurrentPriority(int priority) {
		switch (priority) {
		case 0:
		default:
			this.currentPriority = Priority.NA;
			break;
		case 1:
			this.currentPriority = Priority.LOW;
			break;
		case 2:
			this.currentPriority = Priority.MEDIUM;
			break;
		case 3:
			this.currentPriority = Priority.HIGH;
			break;
		}
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(String lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public String getTicketLocation() {
		return ticketLocation;
	}

	public void setTicketLocation(String ticketLocation) {
		this.ticketLocation = ticketLocation;
	}

	public List<Update> getUpdates() {
		return updates;
	}

	public void setUpdates(List<Update> updates) {
		this.updates = updates;
	}

	public String getCompletionDetails() {
		return completionDetails;
	}

	public void setCompletionDetails(String completionDetails) {
		this.completionDetails = completionDetails;
	}

	public void setCurrentProgress(Progress currentProgress) {
		this.currentProgress = currentProgress;
	}

	public void setCurrentPriority(Priority currentPriority) {
		this.currentPriority = currentPriority;
	}
	
}
