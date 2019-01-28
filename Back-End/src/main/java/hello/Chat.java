package hello;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity // This tells Hibernate to make a table out of this class
public class Chat {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@ManyToOne
    private User userOne;
	@ManyToOne
    private User userTwo;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getUserOne() {
		return userOne;
	}
	public void setUserOne(User userOne) {
		this.userOne = userOne;
	}
	public User getUserTwo() {
		return userTwo;
	}
	public void setUserTwo(User userTwo) {
		this.userTwo = userTwo;
	}
}
