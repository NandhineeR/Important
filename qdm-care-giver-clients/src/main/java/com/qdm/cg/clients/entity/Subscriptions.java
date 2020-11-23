package com.qdm.cg.clients.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="TB_SUBSCRIPTIONS")
public class Subscriptions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int subscriptionId;
	private String subscriptionName;
//	private String subscriptionDesc;
	private int categoryId;
	private long careProviderId;
	private long careGiverId;
	private int clientId;
	private int careCoordiantorId;
	private String subscriptionType;
	private boolean isActive;
}
