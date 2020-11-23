package com.qdm.cg.clients.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

	private String status;
	private String status_code;
	private String message;
//	private List<String> details;

}
