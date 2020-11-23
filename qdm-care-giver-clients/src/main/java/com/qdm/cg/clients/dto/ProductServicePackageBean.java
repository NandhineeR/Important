package com.qdm.cg.clients.dto;

import lombok.Data;

@Data
public class ProductServicePackageBean {
	private int id;
	private String name;
	private String description;
	private int uploadPhotoId;
	private String isActive;
}
