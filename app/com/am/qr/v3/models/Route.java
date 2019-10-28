package com.am.qr.v3.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * A class which represents the route table in the qrsvc database.
 */
@javax.persistence.Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "route")
public class Route {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "svc", length = 32, columnDefinition = "varchar")
    private String svc;

    @Column(name = "hash_value", length = 32)
    @Type(type = "com.am.common.hibernate.MySQLBinaryType")
    private byte[] hashValue;

    @Column(name = "status", length = 50, columnDefinition = "varchar")
    private String status;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private java.sql.Timestamp updatedAt;

}
