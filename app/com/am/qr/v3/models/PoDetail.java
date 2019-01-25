package com.am.qr.v3.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * A class which represents the client table in the door_access database.
 */
@Entity
@Table(name = "po_detail")
public class PoDetail {

	public PoDetail() {
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

    @Column(name="external_id", length = 20, columnDefinition = "varchar")
	private String externalId;
    public static final String ExternalIdColumn = "external_id";
    public String getExternalId() {
        return externalId;
    }
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @Column(name="external_sub_id", length = 20, columnDefinition = "varchar")
    private String externalSubId;
    public static final String ExternalSubIdColumn = "external_sub_id";
    public String getExternalSubId() {
        return externalSubId;
    }
    public void setExternalSubId(String externalSubId) {
        this.externalSubId = externalSubId;
    }

    @Column(name="external_place_id", length = 20, columnDefinition = "varchar")
    private String externalPlaceId;
    public static final String ExternalPlaceIdColumn = "external_place_id";
    public String getExternalPlaceId() {
        return externalPlaceId;
    }
    public void setExternalPlaceId(String externalPlaceId) {
        this.externalPlaceId = externalPlaceId;
    }

    @Column(name="external_choice_ids", length = 100, columnDefinition = "varchar")
    private String externalChoiceIds;
    public static final String ChoiceIdColumn = "external_choice_ids";
    public String getExternalChoiceIds() {
        return externalChoiceIds;
    }
    public void setExternalChoiceIds(String externalChoiceIds) {
        this.externalChoiceIds = externalChoiceIds;
    }

    @Column(name="external_user_id", length = 20, columnDefinition = "varchar")
    private String externalUserId;
    public static final String ExternalUserIdColumn = "external_user_id";
    public String getExternalUserId() {
        return externalUserId;
    }
    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
    }

    @Column(name="external_account_id", length = 20, columnDefinition = "varchar")
    private String externalAccountId;
    public static final String ExternalAccountIdColumn = "external_account_id";
    public String getExternalAccountId() {
        return externalAccountId;
    }
    public void setExternalAccountId(String externalAccountId) {
        this.externalAccountId = externalAccountId;
    }

    @Column(name="purchase_date")
    private java.sql.Timestamp purchaseDate;
    public static final String PurchaseDateColumn = "purchase_date";
    public java.sql.Timestamp getPurchaseDate() {
        return purchaseDate;
    }
    public void setPurchaseDate(java.sql.Timestamp purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Column(name="card_id")
    private Long cardId;
    public static final String CardIdColumn = "card_id";
    public Long getCardId() {
        return cardId;
    }
    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    @Column(name="card_name", length = 255, columnDefinition = "varchar")
    private String cardName;
    public static final String CardNameColumn = "card_name";
    public String getCardName() {
        return cardName;
    }
    public void setCardName(String cardName) {
        this.cardName = cardName;
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
