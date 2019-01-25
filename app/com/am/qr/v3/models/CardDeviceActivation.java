package com.am.qr.v3.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * A class which represents the card_device_activation table in the door_access database.
 */
@javax.persistence.Entity
@Table(name = "card_device_activation")
public class CardDeviceActivation {

	public CardDeviceActivation() {
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

    @Column(name="otp", length = 20, columnDefinition = "varchar")
	private String otp;
    public static final String OtpColumn = "otp";
    public String getOtp() {
        return otp;
    }
    public void setOtp(String otp) {
        this.otp = otp;
    }

    @Column(name="expires_at")
	private java.sql.Timestamp expiresAt;
    public static final String ExpiresAtColumn = "expires_at";
    public java.sql.Timestamp getExpiresAt() {
        return expiresAt;
    }
    public void setExpiresAt(java.sql.Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Column(name="confirmed_at")
	private java.sql.Timestamp confirmedAt;
    public static final String ConfirmedAtColumn = "confirmed_at";
    public java.sql.Timestamp getConfirmedAt() {
        return confirmedAt;
    }
    public void setConfirmedAt(java.sql.Timestamp confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    @Column(name="confirmed_otp", length = 20, columnDefinition = "varchar")
	private String confirmedOtp;
    public static final String ConfirmedOtpColumn = "confirmed_otp";
    public String getConfirmedOtp() {
        return confirmedOtp;
    }
    public void setConfirmedOtp(String confirmedOtp) {
        this.confirmedOtp = confirmedOtp;
    }

    @Column(name="activation_type", length = 20, columnDefinition = "varchar")
	private String activationType;
    public static final String ActivationTypeColumn = "activation_type";
    public String getActivationType() {
        return activationType;
    }
    public void setActivationType(String activationType) {
        this.activationType = activationType;
    }

    @Column(name="card_id", nullable = false)
	private Long cardId;
    public static final String CardIdColumn = "card_id";
    public Long getCardId() {
        return cardId;
    }
    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

	//ManyToOne
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "card_id", insertable = false, updatable = false)
	private Card card;	
    public Card getCard() {
        return card;
    }
    public void setCard(Card card) {
        this.card = card;
    }

    @Column(name="device_id", nullable = false)
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
