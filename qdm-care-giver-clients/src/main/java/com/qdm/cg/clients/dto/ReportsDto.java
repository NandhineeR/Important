package com.qdm.cg.clients.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportsDto {
	private int clientId;
	private String report_name;
	private long report_id;
	private long report_category;
	private String reported_at;

}
