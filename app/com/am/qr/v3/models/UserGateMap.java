package com.am.qr.v3.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * A class which represents the user_gate_map table in the door_access database.
 */
@javax.persistence.Entity
@Table(name = "user_gate_map")
public class UserGateMap {

	public UserGateMap() {
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

    @Column(name="is_activated", nullable = false)
	private Boolean isActivated;
    public static final String IsActivatedColumn = "is_activated";
    public Boolean getIsActivated() {
        return isActivated;
    }
    public void setIsActivated(Boolean isActivated) {
        this.isActivated = isActivated;
    }

    @Column(name="from_date")
    private java.sql.Timestamp fromDate;
    public static final String FromDateColumn = "from_date";
    public java.sql.Timestamp getFromDate() {
        return fromDate;
    }
    public void setFromDate(java.sql.Timestamp fromDate) {
        this.fromDate = fromDate;
    }

    @Column(name="to_date")
    private java.sql.Timestamp toDate;
    public static final String ToDateColumn = "to_date";
    public java.sql.Timestamp getToDate() {
        return toDate;
    }
    public void setToDate(java.sql.Timestamp toDate) {
        this.toDate = toDate;
    }

    @Column(name="user_id", nullable = false)
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

    @Column(name="gate_id", nullable = false)
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
	//ManyToMany relationship
}
