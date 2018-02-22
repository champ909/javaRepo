package techit.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "updates")
public class Update implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private int id;
	
	@Column(nullable=false)
	private int ticketId;

	@Column(nullable=false)
	private String modifier; // modifier's username

	@Column
	private String updateDetails;
	
	@Column(nullable=false)
	private String modifiedDate;

	public Update() {
		
	}
	
	public Update(int id, int ticketId, String modifier, String updateDetails, String modifiedDate) {
		super();
		this.id = id;
		this.ticketId = ticketId;
		this.modifier = modifier;
		this.updateDetails = updateDetails;
		this.modifiedDate = modifiedDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTicketId() {
		return ticketId;
	}

	public void setTicketId(int ticketId) {
		this.ticketId = ticketId;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getUpdateDetails() {
		return updateDetails;
	}

	public void setUpdateDetails(String updateDetails) {
		this.updateDetails = updateDetails;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}
