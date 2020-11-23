package com.qdm.cg.clients.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionsViewBean {
	private int id;
	private int subscriptionId;
	private String subscriptionName;
	private String subscriptionDesc;
	private String subscriptionProfile;
	private boolean active;
	private String subscriptionType;
	private ProviderBean provider;
	private ProviderBean serviceProffessional;
	private ProviderBean careCoordiantor;
	private boolean subscriptionActive;
	private OrderDto category;
	
}
