package com.am.qr.v3.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * A class which represents the place table in the door_access database.
 */
@javax.persistence.Entity
@Table(name = "place")
public class Place {

	public Place() {
	}

    @Id
	@GeneratedValue(strategy = IDENTITY)
    @Column(name="id")
	private Long id;
    public static final String IdColumn = "id";
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name="name", nullable = false, length = 255, columnDefinition = "varchar")
	private String name;
    public static final String NameColumn = "name";
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Column(name="description", length = 2048, columnDefinition = "varchar")
	private String description;
    public static final String DescriptionColumn = "description";
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name="external_id", length = 20, columnDefinition = "varchar")
	private String externalId;
    public static final String ExternalIdColumn = "external_id";
    public String getExternalId() {
        return externalId;
    }
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @Column(name="created_at")
    @CreationTimestamp
	private java.sql.Timestamp createdAt;
    public static final String CreatedAtColumn = "created_at";
    public java.sql.Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(java.sql.Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Column(name="created_by")
	private Long createdBy;
    public static final String CreatedByColumn = "created_by";
    public Long getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name="updated_at")
    @UpdateTimestamp
	private java.sql.Timestamp updatedAt;
    public static final String UpdatedAtColumn = "updated_at";
    public java.sql.Timestamp getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(java.sql.Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Column(name="updated_by")
	private Long updatedBy;
    public static final String UpdatedByColumn = "updated_by";
    public Long getUpdatedBy() {
        return updatedBy;
    }
    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

	//OneToMany relationship
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "place")
	private Set<Gate> gates = new HashSet<>();	
	public Set<Gate> getGates() {
		return gates;
	}
	public void setGates(Set<Gate> gates) {
		this.gates = gates;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "place")
	private Set<UserPlaceMap> userPlaceMaps = new HashSet<>();	
	public Set<UserPlaceMap> getUserPlaceMaps() {
		return userPlaceMaps;
	}
	public void setUserPlaceMaps(Set<UserPlaceMap> userPlaceMaps) {
		this.userPlaceMaps = userPlaceMaps;
	}

	//ManyToMany relationship
	@ManyToMany(mappedBy = "places")
	private Set<User> users = new HashSet<>(); 
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}

}
