package com.qdm.cg.clients.dto;

import java.util.Date;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionTrasactionDto {
	
	private int subscriptionId;;
	private Date fromTime;
	private Date endTime;
	private int serviceId;
	private int openingBalance;
	private int currentBalance;
	private int clientId;
	private boolean isActive;
}
