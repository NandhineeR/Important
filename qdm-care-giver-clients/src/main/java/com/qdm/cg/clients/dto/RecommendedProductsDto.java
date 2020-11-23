package com.qdm.cg.clients.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendedProductsDto {
	private String product_name;
	private int product_id;
	private String product_price;
	private String current_status;
	private String current_status_date;
	private String product_image;
}