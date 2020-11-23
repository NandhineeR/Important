package com.qdm.cg.clients.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationsDto {
	private List<EquipmentDto> equipments;
	private List<ProductsDto> products;
}
