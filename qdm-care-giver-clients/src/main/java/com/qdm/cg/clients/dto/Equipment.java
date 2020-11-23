package com.qdm.cg.clients.dto;

import java.sql.Blob;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Equipment {
	private String equipment_name;
	private String  equipment_code;
	private Blob  equipment_image;
}
