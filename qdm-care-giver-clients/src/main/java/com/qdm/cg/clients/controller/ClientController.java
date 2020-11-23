package com.qdm.cg.clients.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.keycloak.RSATokenVerifier;
import org.keycloak.representations.AccessToken;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.qdm.cg.clients.dto.Category;
import com.qdm.cg.clients.dto.ClientRegisterationDTO;
import com.qdm.cg.clients.dto.OrderDto;
import com.qdm.cg.clients.dto.ProductServicePackageBean;
import com.qdm.cg.clients.dto.ProviderBean;
import com.qdm.cg.clients.dto.SubscriptionsDTO;
import com.qdm.cg.clients.dto.SubscriptionsViewBean;
import com.qdm.cg.clients.entity.ClientDetails;
import com.qdm.cg.clients.entity.Subscriptions;
import com.qdm.cg.clients.entity.UploadProfile;
import com.qdm.cg.clients.repository.ClientDetailsRepository;
import com.qdm.cg.clients.repository.PersonaRepository;
import com.qdm.cg.clients.repository.SubscriptionsRepository;
import com.qdm.cg.clients.response.ResponseInfo;
import com.qdm.cg.clients.response.ResponseType;
import com.qdm.cg.clients.service.ClientService;
import com.qdm.cg.clients.service.SubscriptionsService;
import com.qdm.cg.clients.util.ClientConstants;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = { "clients" })
@Slf4j
@SuppressWarnings({ "unused", "unchecked", "rawtypes" })

public class ClientController {
	@Autowired
	ClientService clientsService;

	@Autowired
	SubscriptionsService subscriptionsService;

	@Autowired
	SubscriptionsRepository subscriptionsRepository;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	ModelMapper modelMapper;
	@Autowired
	PersonaRepository personaRepository;

	@Autowired
	ClientDetailsRepository clientsDetailsRepository;

	@PostMapping(value = "/registeration")
	public ResponseEntity<?> clientRegisteration(ClientRegisterationDTO clientDetails) {

		ResponseEntity response = null;
		try {
			ClientDetails clientRegisteration = clientsService.clientRegisteration(clientDetails);
			log.info("Client Created Successfully With ClientId : " + clientRegisteration.getId());
			response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
					ResponseType.SUCCESS.getResponseCode(), "", null), HttpStatus.CREATED);
			return response;
		} catch (Exception e) {
			log.error("Error Occured At Adding Client : " + e.getMessage());
			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "Try Again", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

	@PostMapping(value = "/modify")
	public ResponseEntity<?> clientModify(ClientRegisterationDTO clientDetails) {
		ResponseEntity response = null;
		try {
			ClientDetails clientRegisteration = clientsService.clientModify(clientDetails);
			log.info("Client Updated Successfully With ClientId : " + clientRegisteration.getId());
			response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
					ResponseType.SUCCESS.getResponseCode(), "", null), HttpStatus.CREATED);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Occured At Updating Client : " + e.getMessage());
			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "Try Again", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

	@GetMapping(value = "/list/get", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getAllClientsDetails(@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize,@RequestParam String toTimeStamp,
			@RequestParam String fromTimeStamp,@RequestParam(defaultValue = "0") long serviceProfessionId,
			@RequestParam(value = "sortDirec", required = false) String sortDirec,
			@RequestParam(value = "sortfield", required = false) String sortfield,@RequestParam(value = "searchName", required = false) String searchName,HttpServletRequest request) { 
		ResponseEntity response = null;
		try {
			List<ClientDetails> clientstotal= new ArrayList<>();
			List<ClientDetails> clients=new ArrayList<>();
			
			String strtoken = request.getHeader("Authorization");
			String tokenString = strtoken.substring(7);
			AccessToken token = RSATokenVerifier.create(tokenString).getToken();
			Map<String, Object> otherClaims = token.getOtherClaims();
			System.out.println(otherClaims);
			clients = clientsService.getClientsListing(pageNo, pageSize, sortDirec,
					sortfield, otherClaims.get("user_id"),
					token.getRealmAccess().getRoles().toArray()[0],searchName);
	
			List<Object> list = new ArrayList<Object>();

			Map<String, Object> getClients = new HashMap<String, Object>();
			getClients.put("offset", pageNo);
			getClients.put("total_count", clients.size());
			for (ClientDetails clientsData : clients) {

				Map<String, Object> map = new HashMap();
				map.put("id", clientsData.getId());
				map.put("name", clientsData.getName());
				map.put("is_active", clientsData.getIsActive());
				
				//added for new client api request
				map.put("upcoming_activity",personaRepository.getClientUpcomingActivty(clientsData.getId(), fromTimeStamp, toTimeStamp));
				map.put("gender", clientsData.getGender());
				map.put("age", clientsData.getAge());
				map.put("gender", clientsData.getGender());
				map.put("contact", clientsData.getMobilenumber());
				map.put("status_type", "Needs medical attention");
				if (clientsData.getUploadPhoto() != null) {
					URL url1 = null ;
					try {
						url1 = new URL("http://52.172.157.13:8443/");
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
					String fileDownloadUri = ServletUriComponentsBuilder.fromHttpUrl(url1.toString())
							.path("/images/" + clientsData.getUploadPhoto().getId()).toUriString();
					map.put("profile_pic", fileDownloadUri);
				} else {
					map.put("profile_pic", "");
				}
				map.put("isactive", clientsData.getIsActive());
				map.put("email", clientsData.getEmailid());
				list.add(map);
				List<OrderDto> orderList = new ArrayList<>();

				orderList.add(OrderDto.builder()
						.value(String.valueOf(personaRepository.todatefromdateActivityDetails(clientsData.getId(), fromTimeStamp, toTimeStamp)))
						.label(ClientConstants.ACTIVITIES_TODAY)
						.build());

				List<Subscriptions> listSubs = subscriptionsRepository.findByClientId(clientsData.getId());
				if (listSubs != null) {
					orderList.add(OrderDto.builder().value(String.valueOf(listSubs.size()))
							.label(ClientConstants.SUBSCRIPTIONS).build());
					List<Object> caregiverList = new ArrayList<>();
					for (Subscriptions subs : listSubs) {
						Map<String, Object> care_giver = new HashMap();
					ProviderBean providerBean=	personaRepository.getGiver(subs.getCareGiverId(), subs.getCategoryId());
						care_giver.put("id", subs.getCareGiverId());
						
						care_giver.put("value",subs.getCareGiverId() );
						care_giver.put("name", providerBean.getName());
						care_giver.put("label", providerBean.getName());
						care_giver.put("profile_pic", providerBean.getProfile_pic());

						caregiverList.add(care_giver);
					}
					map.put("care_giver", caregiverList);
				}

				map.put("orderList", orderList);

				getClients.put("list", list);
			}

			log.info("Clients List " + list);
			response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
					ResponseType.SUCCESS.getResponseCode(), "", getClients), HttpStatus.OK);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Occured At getClientsLists : " + e.getMessage());
			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

	@GetMapping(value = "/details/get/{clientid}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getClientByClientId(@PathVariable("clientid") Integer clientid) {
		ResponseEntity response = null;
		try {
			ClientDetails clientsData = clientsService.getClientByClientId(clientid);

			if (clientsData != null) {
				Set<Integer> subs = clientsData.getSubscriptions();
				ArrayList<Subscriptions> sub = new ArrayList<Subscriptions>();
				for (Integer integer : subs) {
					Subscriptions subb = subscriptionsRepository.findBySubscriptionId(integer);
					sub.add(subb);
				}

				Map<String, Object> bodyTemp = new HashMap();
				bodyTemp.put("vital_sign", "Body Temperature");
				bodyTemp.put("value", clientsData.getBodyTemperature());
				bodyTemp.put("measure_unit", "Celsius");
				bodyTemp.put("color_code", "#DC143C");

				Map<String, Object> bloodPressure = new HashMap();
				bloodPressure.put("vital_sign", "Blood Pressure");
				bloodPressure.put("value", clientsData.getBloodPressure());
				bloodPressure.put("measure_unit", "Normal");
				bloodPressure.put("color_code", "#008000");

				Map<String, Object> pulseRate = new HashMap();
				pulseRate.put("vital_sign", "Pulse Rate");
				pulseRate.put("value", clientsData.getPulseRate());
				pulseRate.put("measure_unit", "Bpm");
				pulseRate.put("color_code", "#008000");

				Map<String, Object> respiration = new HashMap();
				respiration.put("vital_sign", "Respiration Rate");
				respiration.put("value", clientsData.getRespirationRate());
				respiration.put("measure_unit", "Bpm");
				respiration.put("color_code", "#FCFC0B");

				List<Object> vitalsign = new ArrayList();
				vitalsign.add(bodyTemp);
				vitalsign.add(bloodPressure);
				vitalsign.add(pulseRate);
				vitalsign.add(respiration);

				Map<String, Object> emergency_contact = new HashMap();
				emergency_contact.put("contact_name", clientsData.getRelativeName());
				emergency_contact.put("mobile_no", clientsData.getRelativeMobilenumber());
				emergency_contact.put("mobile_no_isd_code", clientsData.getRelativeMobileISDcode());
				emergency_contact.put("email", clientsData.getRelativeEmailid());
				emergency_contact.put("relationship", clientsData.getRelationship());

				Map<String, Object> clientDetails = new HashMap();
				clientDetails.put("id", clientsData.getId());
				clientDetails.put("name", clientsData.getName());
				clientDetails.put("is_active", clientsData.getIsActive());
				clientDetails.put("upcoming_activity", "Initial asessment");
				clientDetails.put("age", clientsData.getAge());
				clientDetails.put("gender", clientsData.getGender());
				clientDetails.put("mobile_no_isd_code", clientsData.getMobilenumberISDcode());
				clientDetails.put("mobile_no", clientsData.getMobilenumber());
				clientDetails.put("status_type", "");
				if (clientsData.getUploadPhoto() != null) {
					URL url1 = null;
					try {
						url1 = new URL("http://52.172.157.13:8443/");
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
					String fileDownloadUri = ServletUriComponentsBuilder.fromHttpUrl(url1.toString())
							.path("/images/" + clientsData.getUploadPhoto().getId()).toUriString();
					clientDetails.put("profile_pic", fileDownloadUri);
				} else {
					clientDetails.put("profile_pic", "");
				}
				if (clientsData.getDob() != null) {
					SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
					clientDetails.put("dob", date.format(clientsData.getDob()));
				} else {
					clientDetails.put("dob", "");
				}
				clientDetails.put("isactive", clientsData.getIsActive());
				clientDetails.put("email", clientsData.getEmailid());
				clientDetails.put("user_type", "Pro");
				clientDetails.put("emergency_contact", emergency_contact);
				clientDetails.put("recent_vital_signs", vitalsign);
//				clientDetails.put("subscriptions", sub);

				/// Added by Shakila

				clientDetails.put("ic_number", clientsData.getIc());
				clientDetails.put("passport_number", clientsData.getPassport());
				clientDetails.put("address", clientsData.getAddress());
				clientDetails.put("occupation", clientsData.getOccupation());
				clientDetails.put("special_precaution", clientsData.getPrecaution());
				clientDetails.put("medical_diagnosis", clientsData.getMedicalDiagnosis());
				if (Integer.parseInt(clientsData.getBodyTemperature()) > 90) {
					clientDetails.put("health_status_primary", ClientConstants.health_status_primary);

					clientDetails.put("health_status_message",
							clientsData.getName() + " " + ClientConstants.health_status_message);
				} else {
					clientDetails.put("health_status_primary", "");
					clientDetails.put("health_status_message", "");
				}

				Map<String, Object> health_information = new HashMap();

				health_information.put("height", clientsData.getHeight());
				health_information.put("weight", clientsData.getWeight());
				health_information.put("bmi", clientsData.getBMI());
				health_information.put("blood_pressure", clientsData.getBloodPressure());
				clientDetails.put("health_information", health_information);

				clientDetails.put("subscriptions", getSubscriptionsList(clientsData.getId()));
				log.info("ClientById " + clientDetails);
				response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
						ResponseType.SUCCESS.getResponseCode(), "", clientDetails), HttpStatus.OK);
				return response;
			} else {
				log.info("No Clients Found with Id :  " + clientid);
				response = new ResponseEntity(new ResponseInfo(ResponseType.NOT_FOUND.getResponseMessage(),
						ResponseType.NOT_FOUND.getResponseCode(), "", null), HttpStatus.NOT_FOUND);
				return response;
			}
		} catch (Exception e) {
			log.error("Error Occured At getClientByClientId : " + e.getMessage());
			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

	@PostMapping(value = "/subscription/add", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> addSubscriptions(@RequestBody SubscriptionsDTO subscriptionsDTO,HttpServletRequest request) {
		ResponseEntity response = null;
		try {
			String strtoken = request.getHeader("Authorization");
			String tokenString = strtoken.substring(7);
			AccessToken token = RSATokenVerifier.create(tokenString).getToken();
			Map<String, Object> otherClaims = token.getOtherClaims();
			System.out.println(otherClaims);
			
			Subscriptions subscription = subscriptionsService.addSubscriptions(subscriptionsDTO, otherClaims.get("user_id"));
			log.info("Subscription Created Successfully With subscriptionId : " + subscription.getSubscriptionId());
			response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
					ResponseType.SUCCESS.getResponseCode(), "", null), HttpStatus.CREATED);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Occured At AddSubscription : " + e.getMessage());
			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "Try Again", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

	@PutMapping(value = "/subscription/modify", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> updateClientSubscriptions(@RequestParam("clientId") Integer clientId,
			@RequestParam("subscriptionList") Set<Integer> subscriptionList) {
		ResponseEntity response = null;
		if (clientId == 0) {
			log.error("Client Id is Empty");
			response = new ResponseEntity(new ResponseInfo(ResponseType.BAD_REQUEST.getResponseMessage(),
					ResponseType.BAD_REQUEST.getResponseCode(), "", null), HttpStatus.BAD_REQUEST);
			return response;
		}
		try {
			ClientDetails clients = clientsService.updateClientSubscriptions(clientId, subscriptionList);
			if (clients != null) {
				log.info("Client Updated Successfully with Id " + clients.getId());
				response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
						ResponseType.SUCCESS.getResponseCode(), "", null), HttpStatus.OK);
				return response;
			} else {
				log.info("No Clients Found with Id :  " + clientId);
				response = new ResponseEntity(new ResponseInfo(ResponseType.NOT_FOUND.getResponseMessage(),
						ResponseType.NOT_FOUND.getResponseCode(), "", null), HttpStatus.NOT_FOUND);
				return response;
			}
		} catch (Exception e) {
			log.error("Error Occured At UpdateClientSubscriptions : " + e.getMessage());
			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "Try Again", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

	@GetMapping(value = "/subscription/get", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getSubscriptions() {
		ResponseEntity response = null;
		try {
			List<Subscriptions> subscriptions = subscriptionsService.getSubscriptions();
			log.info("Care Givers List " + subscriptions);
			response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
					ResponseType.SUCCESS.getResponseCode(), "", subscriptions), HttpStatus.OK);
			return response;
		} catch (Exception e) {
			log.error("Error Occured At getSubscriptionsList : " + e.getMessage());
			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

	@GetMapping(value = "/subscription/deactivate/{id}/isActive/{active}", produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> deactivateSubscriptions(@PathVariable int id, @PathVariable boolean active) {
		ResponseEntity response = null;
		try {

			Subscriptions subscriptions = subscriptionsService.getSubscription(id);
			subscriptions.setActive(active);
			System.out.println("subscriptions id values" + id);
			subscriptionsRepository.save(subscriptions);
			response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
					ResponseType.SUCCESS.getResponseCode(), "", ""), HttpStatus.OK);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Occured At getSubscriptionsList : " + e.getMessage());
			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

	@GetMapping("/downloadFile/{fileId:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable int fileId, HttpServletRequest request) {
		UploadProfile databaseFile = clientsService.getFile(fileId);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(databaseFile.getFileType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + databaseFile.getFileName() + "\"")
				.body(new ByteArrayResource(databaseFile.getData()));
	}

	public int getCurrentActivity(int clientId) {
		List<Object[]> results = new ArrayList<>();
		int count = 0;
		try {
			Query q = em.createNativeQuery("SELECT from_time_stamp from  activity WHERE clientid = ?1");
			q.setParameter(1, Long.valueOf(clientId));
			results = q.getResultList();
			if (results.size() > 0) {
				for (int i = 0; i < results.size(); i++) {
					Object[] obj = results.get(i);
					Instant stamp = (Instant) obj[0];

				}
			}
		} catch (Exception e) {
			log.error("Activity List Mapping Query Error");
		}

		return count;
	}

	public ProductServicePackageBean getProductServicePackageBean(int id, String type) {
		Object[] bean = null;
		ProductServicePackageBean mapbean = new ProductServicePackageBean();
		try {
			Query q = null;
			if (type.equalsIgnoreCase("product")) {
				q = em.createNativeQuery(
						"SELECT product_id,name,description,upload_photoid,is_active from  tbl_CS_Product WHERE product_id = ?1");

			} else if (type.equalsIgnoreCase("service")) {
				q = em.createNativeQuery(
						"SELECT service_id,name,description,upload_photoid,is_active from  tbl_cs_Service WHERE service_id = ?1");
			} else if (type.equalsIgnoreCase("package")) {
				q = em.createNativeQuery(
						"SELECT package_id,name,description,upload_photoid,is_active from  tbl_CS_Package WHERE package_id = ?1");
			}
			q.setParameter(1, Long.valueOf(id));
			bean = (Object[]) q.getSingleResult();
			mapbean.setId(Integer.parseInt(bean[0].toString()));
			mapbean.setName(bean[1].toString());
			mapbean.setDescription(bean[2].toString());
			if (bean[3] != null) {
				mapbean.setUploadPhotoId(Integer.parseInt(bean[3].toString()));
			}
			mapbean.setIsActive(bean[4].toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapbean;

	}

	public List<SubscriptionsViewBean> getSubscriptionsList(int clientsId) {

		List<SubscriptionsViewBean> viewBean = new ArrayList<>();
		List<Subscriptions> listSubs = subscriptionsRepository.findByClientId(clientsId);
		if (listSubs != null) {
			for (Subscriptions subs1 : listSubs) {
				ProductServicePackageBean productBean = getProductServicePackageBean(subs1.getSubscriptionId(),
						subs1.getSubscriptionType());
				SubscriptionsViewBean bean = new SubscriptionsViewBean();
				bean.setId(subs1.getId());
				bean.setSubscriptionId(subs1.getSubscriptionId());
				bean.setSubscriptionDesc(productBean.getDescription());
				System.out.println(productBean.getIsActive());
				bean.setSubscriptionActive(Boolean.parseBoolean(productBean.getIsActive()));
				bean.setActive(subs1.isActive());
				bean.setSubscriptionType(subs1.getSubscriptionType());
				bean.setSubscriptionName(productBean.getName());
				if (productBean.getUploadPhotoId() > 0) {
					String fileDownloadUri = "http://52.172.157.13:8443/images" + "/" + productBean.getUploadPhotoId();
					bean.setSubscriptionProfile(fileDownloadUri);
				}
				try {
					Category cat = personaRepository.getCategory(subs1.getCategoryId());
					bean.setCategory(
							OrderDto.builder().label(cat.getName()).value(String.valueOf(cat.getId())).build());
				} catch (Exception e) {
					log.error("category List Mapping Query Error");
				}
//				bean.setCategory(OrderDto.builder().label("").value(String.valueOf(subs1.getCategoryId())).build());
				bean.setProvider(personaRepository.getProvider(subs1.getCareProviderId(), subs1.getCategoryId()));
				bean.setCareCoordiantor(
						personaRepository.getCoordinator(subs1.getCareCoordiantorId(), subs1.getCategoryId()));
				bean.setServiceProffessional(personaRepository.getGiver(subs1.getCareGiverId(), subs1.getCategoryId()));
				viewBean.add(bean);
			}

		}
		System.out.println(viewBean.toString());
		return viewBean;
	}

	@GetMapping(value = "/list/search", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getCareProvider(@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(value = "clientName", required = false) String clientName,
			@RequestParam(value = "sortDirec", defaultValue = "desc", required = false) String sortDirec,
			@RequestParam(value = "sortfield", defaultValue = "name", required = false) String sortfield) {
		ResponseEntity response = new ResponseEntity(
				new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(), ResponseType.SUCCESS.getResponseCode(), "",
						clientsService.searchClient(pageNo, pageSize, clientName, sortDirec, sortfield)),
				HttpStatus.OK);

		return response;

	}

	@GetMapping(value = "/activateDeactivateClient/{id}/{status}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> activateDeactivateClient(@PathVariable int id, @PathVariable("status") String active) {
		ResponseEntity response = null;
		try {

			Optional<ClientDetails> clientsdetails = clientsDetailsRepository.findById(id);
			if (clientsdetails.isPresent()) {
				clientsdetails.get().setIsActive(active);
			}
			clientsDetailsRepository.save(clientsdetails.get());
			response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
					ResponseType.SUCCESS.getResponseCode(), "", ""), HttpStatus.OK);
			return response;
		} catch (Exception e) {
			log.error("Error Occured At getSubscriptionsList : " + e.getMessage());
			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

	@GetMapping(value = "/monitoring/list/get", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getAllMonitoringClientInfo(@RequestParam String toTimeStamp,
			@RequestParam String fromTimeStamp) {
		ResponseEntity response = null;
		try {
			response = new ResponseEntity(
					new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(), ResponseType.SUCCESS.getResponseCode(),
							"", personaRepository.getManageClientsModule(fromTimeStamp, toTimeStamp)),
					HttpStatus.OK);
			return response;
		} catch (Exception e) {
			log.error("Error Occured At monitoring client list : " + e.getMessage());
			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

	@GetMapping(value = "/list/getClientbasedonProvider", produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> getAllClientsDetailsbasedonProvider(@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize, @RequestParam String toTimeStamp,
			@RequestParam String fromTimeStamp, @RequestParam(defaultValue = "0") long providerId,
			@RequestParam(value = "sortDirec", required = false) String sortDirec,
			@RequestParam(value = "sortfield", required = false) String sortfield) {
//		@RequestBody HashMap timemap
//		String toTimeStamp = (String) timemap.get("toTimeStamp");
//		String fromTimeStamp = (String) timemap.get("fromTimeStamp");
		ResponseEntity response = null;
		try {
			List<ClientDetails> clientstotal = new ArrayList<>();
			List<ClientDetails> clients = new ArrayList<>();

			clients = clientsService.getAllClientsBasedonProvider(providerId);
			clientstotal = clients;

			List<Object> list = new ArrayList<Object>();

			Map<String, Object> getClients = new HashMap<String, Object>();
			getClients.put("offset", pageNo);
			getClients.put("total_count", clientstotal.size());
			for (ClientDetails clientsData : clients) {

				Map<String, Object> map = new HashMap();
				map.put("id", clientsData.getId());
				map.put("name", clientsData.getName());
				map.put("is_active", clientsData.getIsActive());

				// added for new client api request
				map.put("upcoming_activity",
						personaRepository.getClientUpcomingActivty(clientsData.getId(), fromTimeStamp, toTimeStamp));
				map.put("gender", clientsData.getGender());
				map.put("age", clientsData.getAge());
				map.put("gender", clientsData.getGender());
				map.put("contact", clientsData.getMobilenumber());
				map.put("status_type", "Needs medical attention");
				if (clientsData.getUploadPhoto() != null) {
					URL url1 = null;
					try {
						url1 = new URL("http://52.172.157.13:8443/");
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
					String fileDownloadUri = ServletUriComponentsBuilder.fromHttpUrl(url1.toString())
							.path("/images/" + clientsData.getUploadPhoto().getId()).toUriString();
					map.put("profile_pic", fileDownloadUri);
				} else {
					map.put("profile_pic", "");
				}
				map.put("isactive", clientsData.getIsActive());
				map.put("email", clientsData.getEmailid());
				list.add(map);
				List<OrderDto> orderList = new ArrayList<>();

				orderList.add(OrderDto
						.builder().value(String.valueOf(personaRepository
								.todatefromdateActivityDetails(clientsData.getId(), fromTimeStamp, toTimeStamp)))
						.label(ClientConstants.ACTIVITIES_TODAY).build());

				List<Subscriptions> listSubs = subscriptionsRepository.findByClientId(clientsData.getId());
				if (listSubs != null) {
					orderList.add(OrderDto.builder().value(String.valueOf(listSubs.size()))
							.label(ClientConstants.SUBSCRIPTIONS).build());
					List<Object> caregiverList = new ArrayList<>();
					for (Subscriptions subs : listSubs) {
						Map<String, Object> care_giver = new HashMap();
						ProviderBean providerBean = personaRepository.getGiver(subs.getCareGiverId(),
								subs.getCategoryId());
						care_giver.put("id", subs.getCareGiverId());

						care_giver.put("value", subs.getCareGiverId());
						care_giver.put("name", providerBean.getName());
						care_giver.put("label", providerBean.getName());
						care_giver.put("profile_pic", providerBean.getProfile_pic());

						caregiverList.add(care_giver);
					}
					map.put("care_giver", caregiverList);
				}

				map.put("orderList", orderList);

				getClients.put("list", list);
			}

			return getClients;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Occured At getClientsLists : " + e.getMessage());
		}
		return null;
	}

}
