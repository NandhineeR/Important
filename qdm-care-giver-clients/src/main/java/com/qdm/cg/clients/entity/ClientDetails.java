package com.qdm.cg.clients.entity;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qdm.cg.clients.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_CLIENT_DETAILS")
@JsonIgnoreProperties({ "subscriptions" })
public class ClientDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	String name;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	Date dob;
	@Enumerated(EnumType.STRING)
	Gender gender;
	String ic;
	String passport;
	String emailid;
	int mobilenumberISDcode;
	long mobilenumber;
	String address;
	String occupation;
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "uploadPhotoid")
	UploadProfile uploadPhoto;
	int age;
	long emergencyContactNumber;
	String languagesKnown;
	String maritalStatus;

	/*
	 * @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	 * 
	 * @JoinColumn(name = "Relative_Id") RelativesDetails relativesDetails;
	 * 
	 * @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	 * 
	 * @JoinColumn(name = "HealthInformation_Id") HealthInformation
	 * healthInformation;
	 */
	
	// Relatives Details
	String relativeName;
	String relationship;
	String relativeEmailid;
	int relativeMobileISDcode;
	long relativeMobilenumber;

	// HealthInformation
	private String height;
	private String weight;
	private String BMI;
	private String medicalDiagnosis;
	@ElementCollection
	@Basic(fetch = FetchType.EAGER)
	@JoinTable(name = "TB_PRECAUTION")
	@Column(name = "precaution")
	Collection<String> precaution;
	// Vital Signs
	private String bodyTemperature;
	private String bloodPressure;
	private String respirationRate;
	private String pulseRate;

	@ElementCollection
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "subscriptionId")
	@JoinTable(name = "TB_SUBSCRIPTION")
	private Set<Integer> subscriptions;

	private String isActive = "Active";
	
	@ElementCollection
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "Category_Id")
	@JoinTable(name = "TB_CLIENT_CATEGORY")
	Collection<Integer> category;
	
	@ElementCollection
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "Provider_Id")
	@JoinTable(name = "TB_CLIENT_PROVIDER")
	Collection<Long> careprovider;
	
//	private int user_id;

}
