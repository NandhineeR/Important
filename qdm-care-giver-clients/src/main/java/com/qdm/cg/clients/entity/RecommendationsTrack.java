package com.qdm.cg.clients.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.qdm.cg.clients.dto.TimeLineDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationsTrack {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String product_name;
	private long product_id;
	private String product_price;
	private String current_status;
	private List<TimeLineDto> timeline;
}
