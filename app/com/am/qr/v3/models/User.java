package com.am.qr.v3.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * A class which represents the user table in the door_access database.
 */
@javax.persistence.Entity
@Table(name = "user")
public class User {

	public User() {
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

    @Column(name="username", length = 255, columnDefinition = "varchar")
	private String username;
    public static final String UsernameColumn = "username";
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name="name", length = 255, columnDefinition = "varchar")
    private String name;
    public static final String NameColumn = "name";
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Column(name="email", length = 255, columnDefinition = "varchar")
	private String email;
    public static final String EmailColumn = "email";
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name="phone_no", length = 20, columnDefinition = "varchar")
	private String phoneNo;
    public static final String PhoneNoColumn = "phone_no";
    public String getPhoneNo() {
        return phoneNo;
    }
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Column(name="parent_id")
	private Long parentId;
    public static final String ParentIdColumn = "parent_id";
    public Long getParentId() {
        return parentId;
    }
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

	//ManyToOne
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", insertable = false, updatable = false)
	private User user;	
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    @Column(name="role", length = 20, columnDefinition = "varchar")
	private String role;
    public static final String RoleColumn = "role";
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
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
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<Card> cards = new HashSet<>();	
	public Set<Card> getCards() {
		return cards;
	}
	public void setCards(Set<Card> cards) {
		this.cards = cards;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<DeviceHistory> deviceHistories = new HashSet<>();	
	public Set<DeviceHistory> getDeviceHistories() {
		return deviceHistories;
	}
	public void setDeviceHistories(Set<DeviceHistory> deviceHistories) {
		this.deviceHistories = deviceHistories;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<User> users = new HashSet<>();	
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<UserGateMap> userGateMaps = new HashSet<>();	
	public Set<UserGateMap> getUserGateMaps() {
		return userGateMaps;
	}
	public void setUserGateMaps(Set<UserGateMap> userGateMaps) {
		this.userGateMaps = userGateMaps;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<UserPlaceMap> userPlaceMaps = new HashSet<>();	
	public Set<UserPlaceMap> getUserPlaceMaps() {
		return userPlaceMaps;
	}
	public void setUserPlaceMaps(Set<UserPlaceMap> userPlaceMaps) {
		this.userPlaceMaps = userPlaceMaps;
	}

	//ManyToMany relationship
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_gate_map",
		joinColumns = @JoinColumn(name = "user_id", nullable = false, updatable = false),
		inverseJoinColumns = @JoinColumn(name = "gate_id", nullable = false, updatable = false))
	private Set<Gate> gates = new HashSet<>(); 
	public Set<Gate> getGates() {
		return gates;
	}
	public void setGates(Set<Gate> gates) {
		this.gates = gates;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_place_map",
		joinColumns = @JoinColumn(name = "user_id", nullable = false, updatable = false),
		inverseJoinColumns = @JoinColumn(name = "place_id", nullable = false, updatable = false))
	private Set<Place> places = new HashSet<>(); 
	public Set<Place> getPlaces() {
		return places;
	}
	public void setPlaces(Set<Place> places) {
		this.places = places;
	}

}
