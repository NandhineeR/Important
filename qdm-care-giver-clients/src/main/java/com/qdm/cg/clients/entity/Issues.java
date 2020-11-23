package com.qdm.cg.clients.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qdm.cg.clients.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_issues_details")
public class Issues {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long issue_id;
	private long clientId;
	private String issue_tag;
	private String issue_title;
	private String subscription_name;
	private String issue_status;
	private String issue_description;
	private String issue_type;
	private String issued_product;
	private String issued_client_name;
	private String issued_time;
	private String client_phone_no;

}
