package hello;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity // This tells Hibernate to make a table out of this class
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private String email;
    @JsonIgnore
    private String password;
    private String firstname;
    private String lastname;
    private String phone;
    private String workExp;
    private String eduExp;
    private String skillsExp;
    private String workPrivacy;
    private String eduPrivacy;
    private String skillsPrivacy;
    private String lastChatUser;
    private String profileImage;
    @OneToMany(mappedBy="user")
    private List<Article> articles;
    
    public String getLastChatUser() {
		return lastChatUser;
	}

	public void setLastChatUser(String lastChatUser) {
		this.lastChatUser = lastChatUser;
	}

	public User() {
    	articles = new ArrayList<Article>();
    }
    
    public int getId() {
    	return id;
    }
    
    public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public void createArticle(Article article) {
		this.articles.add(article);
	}
	
	public String getWorkExp() {
		return this.workExp;
	}
	
	public void setWorkExp(String workexp) {
		this.workExp = workexp;
	}
	
	public String getEduExp() {
		return this.eduExp;
	}
	
	public void setEduExp(String eduexp) {
		this.eduExp = eduexp;
	}
	
	public String getSkillsExp() {
		return this.skillsExp;
	}
	
	public void setSkillsExp(String skillsexp) {
		this.skillsExp = skillsexp;
	}

	public String getWorkPrivacy() {
		return workPrivacy;
	}

	public void setWorkPrivacy(String workPrivacy) {
		this.workPrivacy = workPrivacy;
	}

	public String getEduPrivacy() {
		return eduPrivacy;
	}

	public void setEduPrivacy(String eduPrivacy) {
		this.eduPrivacy = eduPrivacy;
	}

	public String getSkillsPrivacy() {
		return skillsPrivacy;
	}

	public void setSkillsPrivacy(String skillsPrivacy) {
		this.skillsPrivacy = skillsPrivacy;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	
	
}
