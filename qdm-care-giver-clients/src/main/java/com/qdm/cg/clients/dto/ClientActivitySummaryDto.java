package com.qdm.cg.clients.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientActivitySummaryDto {
	private long activity_id;
	private String activity_name;
	private String client_name;
	private String from_time_stamp;
	private String to_time_stamp;
	private OrderDto activity_type;
	private String activity_status;
	private String activity_description;
	private int client_id;
	private String occurence;
	private String Mode;
	private String check_in;
	private String check_out;
	private OrderDto service_professional_info;
	private ClientInfoDto client_info;

}