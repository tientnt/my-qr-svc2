package com.am.qr.v3.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * A class which represents the device table in the door_access database.
 */
@javax.persistence.Entity
@Table(name = "device")
public class Device {

	public Device() {
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

    @Column(name="username", length = 40, columnDefinition = "varchar")
	private String username;
    public static final String UsernameColumn = "username";
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name="version", length = 20, columnDefinition = "varchar")
	private String version;
    public static final String VersionColumn = "version";
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }

    @Column(name="is_activated")
	private Boolean isActivated;
    public static final String IsActivatedColumn = "is_activated";
    public Boolean getIsActivated() {
        return isActivated;
    }
    public void setIsActivated(Boolean isActivated) {
        this.isActivated = isActivated;
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

    @Column(name="uid", length = 40, columnDefinition = "varchar")
	private String uid;
    public static final String UidColumn = "uid";
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

    @Column(name="gate_id")
	private Long gateId;
    public static final String GateIdColumn = "gate_id";
    public Long getGateId() {
        return gateId;
    }
    public void setGateId(Long gateId) {
        this.gateId = gateId;
    }

	//ManyToOne
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gate_id", insertable = false, updatable = false)
	private Gate gate;	
    public Gate getGate() {
        return gate;
    }
    public void setGate(Gate gate) {
        this.gate = gate;
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
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "device")
	private Set<CardDeviceActivation> cardDeviceActivations = new HashSet<>();	
	public Set<CardDeviceActivation> getCardDeviceActivations() {
		return cardDeviceActivations;
	}
	public void setCardDeviceActivations(Set<CardDeviceActivation> cardDeviceActivations) {
		this.cardDeviceActivations = cardDeviceActivations;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "device")
	private Set<DeviceHistory> deviceHistories = new HashSet<>();	
	public Set<DeviceHistory> getDeviceHistories() {
		return deviceHistories;
	}
	public void setDeviceHistories(Set<DeviceHistory> deviceHistories) {
		this.deviceHistories = deviceHistories;
	}

	//ManyToMany relationship
}
