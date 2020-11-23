package com.qdm.cg.clients.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class SubscriptionsDTO {
	private int id;
	private int subscriptionId;
	private int categoryId;
	private long careProviderId;
	private long careGiverId;
	private int clientId;
	private int careCoordiantorId;
	private String subscriptionType;
	private String active;
}
