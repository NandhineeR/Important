package com.qdm.cg.clients.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueDetailDto {

	private String issue_title;
	private String issue_id;
	private String issue_type;
	private String issue_tag;
	private String issued_product;
	private String issued_client_name;
	private String issued_time;
	private String issue_description;
	private String profile_pic;
	private String client_phone_no;
	private String issue_status;
	private String latitude;
	private String longitude;
	
}