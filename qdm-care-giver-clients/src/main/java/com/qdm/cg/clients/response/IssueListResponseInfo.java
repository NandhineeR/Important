package com.qdm.cg.clients.response;

import java.util.List;

import com.qdm.cg.clients.dto.IssueDto;

public class IssueListResponseInfo {

	private String status;
	private String message;
	private String openStatusCount;
	private String pendingStatusCount;
	private String resolvedStatusCount;
	private List<IssueDto> issueList;
	
}
