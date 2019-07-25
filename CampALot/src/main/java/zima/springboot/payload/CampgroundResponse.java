package zima.springboot.payload;

import java.time.Instant;

public class CampgroundResponse {

	private Long id;
	private String name;
	private String image;
	private String description;
	private UserSummary createdBy;
	private Instant updatedDateTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public UserSummary getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(UserSummary createdBy) {
		this.createdBy = createdBy;
	}
	public Instant getUpdatedDateTime() {
		return updatedDateTime;
	}
	public void setUpdatedDateTime(Instant updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}
	
	
	
}
