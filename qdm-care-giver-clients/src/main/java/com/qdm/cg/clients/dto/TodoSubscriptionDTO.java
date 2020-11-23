package com.qdm.cg.clients.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoSubscriptionDTO {
	String Name;
	String ProviderName;
	String Category;
	int SubscriptionCount;
}
