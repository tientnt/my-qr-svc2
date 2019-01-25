package com.am.qr.v3.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * A class which represents the device_history table in the door_access database.
 */
@javax.persistence.Entity
@Table(name = "device_history")
public class DeviceHistory {

	public DeviceHistory() {
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

    @Column(name="activation_code", length = 255, columnDefinition = "varchar")
	private String activationCode;
    public static final String ActivationCodeColumn = "activation_code";
    public String getActivationCode() {
        return activationCode;
    }
    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    @Column(name="device_id")
	private Long deviceId;
    public static final String DeviceIdColumn = "device_id";
    public Long getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

	//ManyToOne
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "device_id", insertable = false, updatable = false)
	private Device device;	
    public Device getDevice() {
        return device;
    }
    public void setDevice(Device device) {
        this.device = device;
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

    @Column(name="remark", length = 1024, columnDefinition = "varchar")
    private String remark;
    public static final String RemarkColumn = "remark";
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
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
