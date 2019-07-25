package zima.springboot.payload;

import java.time.Instant;

public class UserProfile {

	private Long id;
    private String username;
    private Instant joinedAt;
    private String email;
    private Long campgroundCount;
	
    public UserProfile() {
    	
    }

    
    
	public UserProfile(Long id, String username, Instant joinedAt, String email, Long campgroundCount) {
		this.id = id;
		this.username = username;
		this.joinedAt = joinedAt;
		this.email = email;
		this.campgroundCount = campgroundCount;
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

	public Instant getJoinedAt() {
		return joinedAt;
	}

	public void setJoinedAt(Instant joinedAt) {
		this.joinedAt = joinedAt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getCampgroundCount() {
		return campgroundCount;
	}

	public void setCampgroundCount(Long campgroundCount) {
		this.campgroundCount = campgroundCount;
	}
    
}
