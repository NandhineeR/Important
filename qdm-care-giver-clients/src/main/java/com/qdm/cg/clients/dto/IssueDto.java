package com.qdm.cg.clients.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueDto {
	 private String issue_id;
	 private String issue_tag;
	 private String issue_title;
	 private String subscription_name;
	 private String issue_status;
}
