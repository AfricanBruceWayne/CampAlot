package zima.springboot.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String content;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "campground_id")
	private Campground campground;
	
	public Comment() {
		
	}

	public Comment(long id, String content) {
		this.id = id;
		this.content = content;
	}
	
	public Comment(long id, String content, User user, Campground campground) {
		this.id = id;
		this.content = content;
		this.user = user;
		this.campground = campground;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Campground getCampground() {
		return campground;
	}

	public void setCampground(Campground campground) {
		this.campground = campground;
	}

}
