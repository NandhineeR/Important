package com.qdm.cg.clients.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationsTrackResponse {
	private String product_name;
	private long product_id;
	private String product_price;
	private String current_status;
	private String current_status_date;
	private List<TimeLineDto> timeline;
}