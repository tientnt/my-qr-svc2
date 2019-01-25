package com.am.qr.v3.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * A class which represents the gate table in the door_access database.
 */
@javax.persistence.Entity
@Table(name = "gate")
public class Gate {

	public Gate() {
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

    @Column(name="description", length = 512, columnDefinition = "varchar")
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

    @Column(name="external_id2", length = 20, columnDefinition = "varchar")
    private String externalId2;
    public static final String ExternalId2Column = "external_id2";
    public String getExternalId2() {
        return externalId;
    }
    public void setExternalId2(String externalId2) {
        this.externalId2 = externalId2;
    }

    @Column(name="place_id", nullable = false)
	private Long placeId;
    public static final String PlaceIdColumn = "place_id";
    public Long getPlaceId() {
        return placeId;
    }
    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

	//ManyToOne
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "place_id", insertable = false, updatable = false)
	private Place place;	
    public Place getPlace() {
        return place;
    }
    public void setPlace(Place place) {
        this.place = place;
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
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "gate")
	private Set<CardGateMap> cardGateMaps = new HashSet<>();	
	public Set<CardGateMap> getCardGateMaps() {
		return cardGateMaps;
	}
	public void setCardGateMaps(Set<CardGateMap> cardGateMaps) {
		this.cardGateMaps = cardGateMaps;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "gate")
	private Set<Device> devices = new HashSet<>();	
	public Set<Device> getDevices() {
		return devices;
	}
	public void setDevices(Set<Device> devices) {
		this.devices = devices;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "gate")
	private Set<UserGateMap> userGateMaps = new HashSet<>();	
	public Set<UserGateMap> getUserGateMaps() {
		return userGateMaps;
	}
	public void setUserGateMaps(Set<UserGateMap> userGateMaps) {
		this.userGateMaps = userGateMaps;
	}

	//ManyToMany relationship
	@ManyToMany(mappedBy = "gates")
	private Set<Card> cards = new HashSet<>(); 
	public Set<Card> getCards() {
		return cards;
	}
	public void setCards(Set<Card> cards) {
		this.cards = cards;
	}

	@ManyToMany(mappedBy = "gates")
	private Set<User> users = new HashSet<>(); 
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}

}
