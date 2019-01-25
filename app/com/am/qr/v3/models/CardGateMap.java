package com.am.qr.v3.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * A class which represents the card_gate_map table in the door_access database.
 */
@javax.persistence.Entity
@Table(name = "card_gate_map")
public class CardGateMap {

	public CardGateMap() {
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

    @Column(name="status", nullable = false)
	private Integer status;
    public static final String StatusColumn = "status";
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
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

    @Column(name="hash", length = 128, columnDefinition = "varchar")
	private String hash;
    public static final String HashColumn = "hash";
    public String getHash() {
        return hash;
    }
    public void setHash(String hash) {
        this.hash = hash;
    }

    @Column(name="remark", length = 255, columnDefinition = "varchar")
	private String remark;
    public static final String RemarkColumn = "remark";
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
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
