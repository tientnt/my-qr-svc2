package com.am.qr.v3.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * A class which represents the card table in the door_access database.
 */
@javax.persistence.Entity
@Table(name = "card")
public class Card {

	public Card() {
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

    @Column(name="name", length = 255, columnDefinition = "varchar")
	private String name;
    public static final String NameColumn = "name";
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Column(name="description", length = 1024, columnDefinition = "varchar")
	private String description;
    public static final String DescriptionColumn = "description";
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name="is_activated", nullable = false)
	private Boolean isActivated;
    public static final String IsActivatedColumn = "is_activated";
    public Boolean getIsActivated() {
        return isActivated;
    }
    public void setIsActivated(Boolean isActivated) {
        this.isActivated = isActivated;
    }

    @Column(name="hash", length = 128, columnDefinition = "varchar")
	private String hash;
    public static final String HashColumn = "hash";
    public String getHash() {
        return hash;
    }
    public void setHash(String hash) {
        this.hash = hash;
    }

    @Column(name="user_id")
	private Long userId;
    public static final String UserIdColumn = "user_id";
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

	//ManyToOne
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private User user;	
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    @Column(name="purchased_date")
    private java.sql.Timestamp purchasedDate;
    public static final String PurchasedDateColumn = "purchased_date";
    public java.sql.Timestamp getPurchasedDate() {
        return purchasedDate;
    }
    public void setPurchasedDate(java.sql.Timestamp purchasedDate) {
        this.purchasedDate = purchasedDate;
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

	//OneToMany relationship
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "card")
	private Set<CardDeviceActivation> cardDeviceActivations = new HashSet<>();	
	public Set<CardDeviceActivation> getCardDeviceActivations() {
		return cardDeviceActivations;
	}
	public void setCardDeviceActivations(Set<CardDeviceActivation> cardDeviceActivations) {
		this.cardDeviceActivations = cardDeviceActivations;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "card")
	private Set<CardGateMap> cardGateMaps = new HashSet<>();	
	public Set<CardGateMap> getCardGateMaps() {
		return cardGateMaps;
	}
	public void setCardGateMaps(Set<CardGateMap> cardGateMaps) {
		this.cardGateMaps = cardGateMaps;
	}

	//ManyToMany relationship
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "card_gate_map",
		joinColumns = @JoinColumn(name = "card_id", nullable = false, updatable = false),
		inverseJoinColumns = @JoinColumn(name = "gate_id", nullable = false, updatable = false))
	private Set<Gate> gates = new HashSet<>(); 
	public Set<Gate> getGates() {
		return gates;
	}
	public void setGates(Set<Gate> gates) {
		this.gates = gates;
	}

}
