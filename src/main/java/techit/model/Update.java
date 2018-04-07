package techit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import techit.model.Ticket.Priority;
import techit.model.Ticket.Status;

@Entity
@Table(name = "updates")
public class Update implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Ticket ticket;

    @ManyToOne
    private User technician;

    private String details;

    private Date date;

    public Update()
    {
    }
    
	@JsonCreator
	public Update(@JsonProperty("ticketId") long ticketId) {
		if(ticketId<1)
			ticket=null;
		else {
			ticket = new Ticket();
			ticket.setId(ticketId);
		}
	}

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public Ticket getTicket()
    {
        return ticket;
    }

    public void setTicket( Ticket ticket )
    {
        this.ticket = ticket;
    }

    public User getTechnician()
    {
        return technician;
    }

    public void setTechnician( User technician )
    {
        this.technician = technician;
    }

    public String getDetails()
    {
        return details;
    }

    public void setDetails( String details )
    {
        this.details = details;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate( Date date )
    {
        this.date = date;
    }

}
