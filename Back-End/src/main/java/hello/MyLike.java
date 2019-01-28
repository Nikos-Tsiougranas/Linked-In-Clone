package hello;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class MyLike {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
	@ManyToOne
	private User user;
	@ManyToOne
	private Article article;
	private String status;
	
	public int getId() {
    	return id;
    }
	
	public User getUser() {
    	return this.user;
    }
    
    public void setUser(User user) {
    	this.user = user;
    }
    
    public Article getArticle() {
    	return this.article;
    }
    
    public void setArticle(Article article) {
    	this.article = article;
    }
    
    public String getStatus() {
    	return this.status;
    }
    
    public void setStatus(String status) {
    	this.status = status;
    }
}
