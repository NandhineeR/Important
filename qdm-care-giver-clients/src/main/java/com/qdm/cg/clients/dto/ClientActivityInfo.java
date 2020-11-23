package com.qdm.cg.clients.dto;

import com.qdm.cg.clients.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientActivityInfo {
	private int value;
	private String label;
	private int id;
	private String name;
	private String profie_pic;
	private int age;
	private Gender gender;
	
}
