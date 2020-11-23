package com.qdm.cg.clients.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name="TB_SUBSCRIPTION_TXN")
public class SubscriptionsTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "subscriptions_id")
	private int subscriptionId;
	@Column(name = "from_time")
	private Date fromTime;
	@Column(name = "end_time")
	private Date endTime;
	@Column(name = "service_id")
	private int serviceId;
	@Column(name = "opening_balance")
	private int openingBalance;
	@Column(name = "current_balance")
	private int currentBalance;
	@Column(name = "client_id")
	private int clientId;
	@Column(name = "is_active")
	private boolean isActive;

	@CreatedDate
	@Column(name = "Created_Date", updatable = false)
	private Date createdAt;

	@LastModifiedDate
	@Column(name = "updated_at")
	private Date updatedAt;
	
}
