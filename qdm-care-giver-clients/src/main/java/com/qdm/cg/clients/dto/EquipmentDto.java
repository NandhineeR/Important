package com.qdm.cg.clients.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentDto {
	private long id;
	private long clientId;
	private String equipment_name;
	private MultipartFile  equipment_image;
	private String currency;
	private double price;
}
