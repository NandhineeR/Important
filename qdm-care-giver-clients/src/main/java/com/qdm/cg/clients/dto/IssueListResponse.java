package com.qdm.cg.clients.dto;

import java.util.List;

import com.qdm.cg.clients.enums.StatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueListResponse {
	private int open_count;
	private int resolved_count;
	private int pending_count;
	private StatusEnum[] issues_enum;
	private List<IssueDto> issue_list;
}
