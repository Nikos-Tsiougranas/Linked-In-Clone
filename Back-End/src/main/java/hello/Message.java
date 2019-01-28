package hello;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Message {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String text;
	private long time;
	@ManyToOne
	private Chat chat;
	@ManyToOne
	private User user;
	
	public Message() {}
	
	public Message(User user,String text,long time,Chat chat) {
		this.user = user;
		this.text = text;
		this.time = time;
		this.chat = chat;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Chat getChat() {
		return chat;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}
	
	
}
