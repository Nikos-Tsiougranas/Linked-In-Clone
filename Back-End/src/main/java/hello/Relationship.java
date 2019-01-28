package hello;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity // This tells Hibernate to make a table out of this class
public class Relationship {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@ManyToOne
    private User userOne;
	@ManyToOne
    private User userTwo;
    private String status;
    private int actionUserid;
    
    public int getId() {
    	return id;
    }
    
    public User getUserOne() {
    	return this.userOne;
    }
    
    public void setUserOne(User user) {
    	this.userOne = user;
    }
    
    public User getUserTwo() {
    	return this.userTwo;
    }
    
    public void setUserTwo(User user) {
    	this.userTwo = user;
    }
    
    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getActionUserId() {
		return this.actionUserid;
	}

	public void setActionUserId(int actionUserid) {
		this.actionUserid = actionUserid;
	}
}
