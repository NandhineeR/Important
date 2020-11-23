package com.qdm.cg.clients.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRatingDto {
	private String review_description;
	private String review_title;
	private String rating_out_of_five;
	private String product_name;
	private String product_id;
	private String reviewed_on;

}
