package com.qdm.cg.clients.dto;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.qdm.cg.clients.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ClientRegisterationDTO {

	int id;
	String name;
	Date dob;
	Gender gender;
	String ic;
	String passport;
	String emailid;
	int mobilenumberISDcode;
	long mobilenumber;
	String address;
	String occupation;
	MultipartFile uploadPhoto;
	int age;
	long emergencyContactNumber;
	String languagesKnown;
	String maritalStatus;

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
	Collection<String> precaution;
	// Vital Signs
	private String bodyTemperature;
	private String bloodPressure;
	private String respirationRate;
	private String pulseRate;

	// Subscriptions
	private Set<Integer> subscriptions;
	
	
	Collection<Integer> category;
	Collection<Long> careprovider;

}
