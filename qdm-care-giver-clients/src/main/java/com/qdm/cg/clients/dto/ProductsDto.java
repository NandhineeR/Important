package com.qdm.cg.clients.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductsDto {
	 private String product_name;
     private String product_desc;
     private String product_code;
     private String product_price;
     private String offer;
}
