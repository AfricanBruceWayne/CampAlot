package zima.springboot.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.*;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private long id;
	private String username;
	private String password;
	
	@OneToMany(mappedBy = "user")
	private List<Campground> campgrounds;
	
	@OneToMany(mappedBy = "user")
	private List<Comment> comments;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(
					name = "user_id", referencedColumnName = "user_id"),
			inverseJoinColumns = @JoinColumn(
					name = "role_id", referencedColumnName = "id"))
	private Collection <Role> roles;
	
	public User() {
		
	}

	public User(long id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
	}

	public User(long id, String username, String password, List<Campground> campgrounds, List<Comment> comments) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.campgrounds = campgrounds;
		this.comments = comments;
	}
	
	public User(long id, String username, String password, List<Campground> campgrounds, List<Comment> comments,
			Collection<Role> roles) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.campgrounds = campgrounds;
		this.comments = comments;
		this.roles = roles;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Campground> getCampgrounds() {
		return campgrounds;
	}

	public void setCampgrounds(List<Campground> campgrounds) {
		this.campgrounds = campgrounds;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
	
		
}
