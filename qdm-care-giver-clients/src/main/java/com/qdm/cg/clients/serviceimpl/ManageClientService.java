package com.qdm.cg.clients.serviceimpl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.qdm.cg.clients.dto.ClientActivityDto;
import com.qdm.cg.clients.dto.ClientActivityResponse;
import com.qdm.cg.clients.dto.ClientActivitySummaryDto;
import com.qdm.cg.clients.dto.ClientInfoDto;
import com.qdm.cg.clients.dto.ClientReportResponse;
import com.qdm.cg.clients.dto.EquipmentDto;
import com.qdm.cg.clients.dto.IssueDetailDto;
import com.qdm.cg.clients.dto.IssueDto;
import com.qdm.cg.clients.dto.IssueListResponse;
import com.qdm.cg.clients.dto.IssueStatus;
import com.qdm.cg.clients.dto.ProductRatingDto;
import com.qdm.cg.clients.dto.ProductRatingResponse;
import com.qdm.cg.clients.dto.ProductsDto;
import com.qdm.cg.clients.dto.RecommendationsDto;
import com.qdm.cg.clients.dto.RecommendationsTrackResponse;
import com.qdm.cg.clients.dto.RecommendedProductsDto;
import com.qdm.cg.clients.dto.RecommendedProductsResponse;
import com.qdm.cg.clients.dto.ReportsDto;
import com.qdm.cg.clients.dto.TimeLineDto;
import com.qdm.cg.clients.dto.TodoSubscriptionDTO;
import com.qdm.cg.clients.entity.Activity;
import com.qdm.cg.clients.entity.ClientDetails;
import com.qdm.cg.clients.entity.Equipment;
import com.qdm.cg.clients.entity.Issues;
import com.qdm.cg.clients.entity.Product;
import com.qdm.cg.clients.entity.Reports;
import com.qdm.cg.clients.entity.Subscriptions;
import com.qdm.cg.clients.entity.TimeLine;
import com.qdm.cg.clients.entity.UploadProfile;
import com.qdm.cg.clients.enums.ManageClientsConstants;
import com.qdm.cg.clients.enums.StatusEnum;
import com.qdm.cg.clients.exceptionhandler.NoIssueFoundException;
import com.qdm.cg.clients.repository.ActivityRepository;
import com.qdm.cg.clients.repository.ClientDetailsRepository;
import com.qdm.cg.clients.repository.EquipmentRepository;
import com.qdm.cg.clients.repository.IssuesRepository;
import com.qdm.cg.clients.repository.PersonaRepository;
import com.qdm.cg.clients.repository.ProductRepository;
import com.qdm.cg.clients.repository.ReportsRepository;
import com.qdm.cg.clients.repository.SubscriptionsRepository;
import com.qdm.cg.clients.repository.TimeLineRepository;
import com.qdm.cg.clients.response.ResponseInfo;
import com.qdm.cg.clients.service.ClientService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ManageClientService {

	@Autowired
	ModelMapper modelMapper;
	@Autowired
	IssuesRepository issuesRepository;
	@Autowired
	ReportsRepository reportsRepository;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	EquipmentRepository equipmentRepository;
	@Autowired
	ActivityRepository activityRepository;
	@Autowired
	TimeLineRepository timeLineRepository;
	@Autowired
	PersonaRepository personaRepository;
	@Autowired
	ClientService clientService;
	@Autowired
	SubscriptionsRepository subscriptionRepository;

	@Autowired
	ClientDetailsRepository clientDetailsRepository;

	@PersistenceContext
	private EntityManager em;

	public ResponseInfo getClientReport(long clientId, Integer pageNo, Integer pageSize) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		List<ReportsDto> reportsList = new ArrayList<ReportsDto>();
		Page<Reports> pagedResult = reportsRepository.findByClientId(clientId, paging);
		List<Reports> entityList = pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<Reports>();
		for (Reports entity : entityList) {
			ReportsDto dto = modelMapper.map(entity, ReportsDto.class);
			dto.setReported_at(getDateTimewithZone(entity.getReported_at()));
			reportsList.add(dto);
		}
		return ResponseInfo.builder().status("Success").status_code(200).message("")
				.data(ClientReportResponse.builder().reports(reportsList).total_reports(reportsList.size()).build())
				.build();
	}

	public ResponseInfo getIssueDetail(long issueId) {
		Issues isssue = issuesRepository.findById(issueId)
				.orElseThrow(() -> new NoIssueFoundException(issueId + " Issue ID not found."));
		Long id = isssue.getClientId();

		ClientDetails clientDetails = clientDetailsRepository.findById(id.intValue())
				.orElseThrow(() -> new NoIssueFoundException("No ID found"));

		// System.out.println("check"+clientDetails.toString());
		URL url1 = null;
		try {
			url1 = new URL("http://52.172.157.13:8443/");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		String fileDownloadUri = ServletUriComponentsBuilder.fromHttpUrl(url1.toString())
				.path("/images/" + clientDetails.getUploadPhoto().getId()).toUriString();

		IssueDetailDto issueDetails = modelMapper.map(isssue, IssueDetailDto.class);
		issueDetails.setProfile_pic(fileDownloadUri);
		return ResponseInfo.builder().status("Success").status_code(200).message("").data(issueDetails).build();
	}

	public ResponseInfo getIssueList(long clientId, Integer pageNo, Integer pageSize) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		int openCount = 0, resolvedCount = 0, pendingCount = 0;
		Page<Issues> pagedResult = issuesRepository.findByClientId(clientId, paging);
		List<Issues> issueList = pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<Issues>();
		List<IssueDto> issuedto = new ArrayList<>();
		for (Issues entity : issueList) {
			issuedto.add(modelMapper.map(entity, IssueDto.class));
		}
		for (IssueDto dto : issuedto) {

			if (null != dto.getIssue_status()
					&& dto.getIssue_status().equalsIgnoreCase(ManageClientsConstants.open_status)) {
				openCount++;
			} else if (null != dto.getIssue_status()
					&& dto.getIssue_status().equalsIgnoreCase(ManageClientsConstants.resolved_status)) {
				resolvedCount++;
			} else if (null != dto.getIssue_status()
					&& dto.getIssue_status().equalsIgnoreCase(ManageClientsConstants.pending_status)) {
				pendingCount++;
			}
		}
		return ResponseInfo.builder().status("Success").status_code(200).message("")
				.data(IssueListResponse.builder().open_count(openCount).pending_count(pendingCount)
						.resolved_count(resolvedCount).issues_enum(StatusEnum.values()).issue_list(issuedto).build())
				.build();

	}

	public ResponseInfo modifyIssueStatus(IssueStatus issueStatus) {

		Issues issues = issuesRepository.findById(issueStatus.getIssue_id())
				.orElseThrow(() -> new NoIssueFoundException(issueStatus.getIssue_id() + "  Issue ID was Not found."));

		issues.setIssue_status(issueStatus.getIssue_status());
		issuesRepository.save(issues);
		return ResponseInfo.builder().status("Success").status_code(200).message("").build();

	}

	public ResponseInfo getRecommendations(long clientId, Integer pageNo, Integer pageSize) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<Equipment> pagedResult = equipmentRepository.findByClientId(clientId, paging);
		List<Equipment> equipments = pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<Equipment>();
		List<EquipmentDto> equipmentList = new ArrayList<EquipmentDto>();
		for (Equipment equipment : equipments) {
			equipmentList.add(modelMapper.map(equipment, EquipmentDto.class));
		}
		Page<Product> pagedProdResult = productRepository.findByClientId(clientId, paging);
		List<Product> products = pagedProdResult.hasContent() ? pagedProdResult.getContent() : new ArrayList<Product>();
		List<ProductsDto> productList = new ArrayList<ProductsDto>();
		for (Product product : products) {
			productList.add(modelMapper.map(product, ProductsDto.class));
		}
		return ResponseInfo.builder().status("Success").status_code(200).message("")
				.data(RecommendationsDto.builder().equipments(equipmentList).products(productList).build()).build();

	}

	public ResponseInfo getProductRatings(long clientId) {
		List<Product> productsList = productRepository.findByClientId(clientId);
		List<ProductRatingDto> productRatings = new ArrayList<ProductRatingDto>();
		for (Product product : productsList) {
			productRatings.add(modelMapper.map(product, ProductRatingDto.class));
		}
		return ResponseInfo.builder().status("Success").status_code(200).message("")
				.data(ProductRatingResponse.builder().ratings_list(productRatings).build()).build();

	}

	public ResponseInfo getRecommendedProductList(long clientId, Integer pageNo, Integer pageSize) {
		Pageable paging = PageRequest.of(pageNo, pageSize);

		Page<Product> pagedProdResult = productRepository.findByClientId(clientId, paging);
		List<Product> products = pagedProdResult.hasContent() ? pagedProdResult.getContent() : new ArrayList<Product>();
		List<RecommendedProductsDto> recommendedProductList = new ArrayList<RecommendedProductsDto>();
		for (Product product : products) {
			recommendedProductList.add(modelMapper.map(product, RecommendedProductsDto.class));
		}
		return ResponseInfo.builder().status("Success").status_code(200).message("")
				.data(RecommendedProductsResponse.builder().recommended_products_list(recommendedProductList).build())
				.build();
	}

	public ResponseInfo getClientActivity(String event, int clientId, String fromTimeStamp, String toTimeStamp) {
		List<ClientActivityDto> pastActivityList = new ArrayList<ClientActivityDto>();
		List<ClientActivityDto> upcomingActivityList = new ArrayList<ClientActivityDto>();

		if (event == null) {

			return ResponseInfo.builder().status("Success").status_code(200).message("")
					.data(ClientActivityResponse.builder().activities(pastActivityList).build()).build();
		}

		if ("past".equals(event)) {
			List<Object[]> pastActivities = personaRepository.getPastActivity(clientId, fromTimeStamp, toTimeStamp);
//			client_id,activity_id,title,activity_status,from_time_stamp
			for (Object[] bean : pastActivities) {
				pastActivityList.add(ClientActivityDto.builder().activity_id(bean[1].toString())
						.activity_name(bean[2].toString())
						.client_name(clientService.getClientByClientId(Integer.valueOf(bean[0].toString())).getName())
						.is_attended(bean[3].toString()).date_time(bean[4].toString()).build());
			}
			return ResponseInfo.builder().status("Success").status_code(200).message("")
					.data(ClientActivityResponse.builder().activities(pastActivityList).build()).build();

		} else if ("upcoming".equals(event)) {
			List<Object[]> upcomingActivities = personaRepository.getUpcomingActivity(clientId, fromTimeStamp,
					toTimeStamp);
//			client_id,activity_id,title,activity_status,from_time_stamp
			for (Object[] bean : upcomingActivities) {
				upcomingActivityList.add(ClientActivityDto.builder().activity_id(bean[1].toString())
						.activity_name(bean[2].toString())
						.client_name(clientService.getClientByClientId(Integer.valueOf(bean[0].toString())).getName())
						.is_attended(bean[3].toString()).date_time(bean[4].toString()).build());
			}
			return ResponseInfo.builder().status("Success").status_code(200).message("")
					.data(ClientActivityResponse.builder().activities(upcomingActivityList).build()).build();

		}
		return null;

	}

	public ResponseInfo getActivitySummary(long activityId) {

//		Activity activity = activityRepository.findById(activityId)
//				.orElseThrow(() -> new NoIssueFoundException("No ID found"));
		ClientActivitySummaryDto clientSummary = new ClientActivitySummaryDto();
		clientSummary = personaRepository.getActivity(activityId);
		ClientDetails clientDetails = clientDetailsRepository.findById(clientSummary.getClient_id())
				.orElseThrow(() -> new NoIssueFoundException("No ID found"));

		ClientInfoDto clientInfo = modelMapper.map(clientDetails, ClientInfoDto.class);
		String fileDownloadUri;
		if (clientDetails.getUploadPhoto() != null) {
			URL url1 = null;
			try {
				url1 = new URL("http://52.172.157.13:8443/");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			fileDownloadUri = ServletUriComponentsBuilder.fromHttpUrl(url1.toString())
					.path("/images/" + clientDetails.getUploadPhoto().getId()).toUriString();
		} else {
			fileDownloadUri = "";
		}
		clientInfo.setClient_name(clientDetails.getName());
		clientInfo.setGender(clientDetails.getGender().name());
		clientInfo.setMobile_no(String.valueOf(clientDetails.getMobilenumber()));
		clientInfo.setProfile_pic(fileDownloadUri);

//		ClientActivitySummaryDto clientSummary = modelMapper.map(activity, ClientActivitySummaryDto.class);
//		clientSummary.setClient_name(clientDetails.getName());
		clientSummary.setClient_info(clientInfo);
		return ResponseInfo.builder().status("Success").status_code(200).message("").data(clientSummary).build();

	}

	// done

	public ResponseInfo getRecommendedProductTrack(long productId) {
		RecommendationsTrackResponse response = new RecommendationsTrackResponse();
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new NoIssueFoundException("Product ID not found"));

		response = modelMapper.map(product, RecommendationsTrackResponse.class);
		List<TimeLine> timeLine = timeLineRepository.findByProductId(productId);

		List<TimeLineDto> timeDtoList = new ArrayList<TimeLineDto>();
		for (TimeLine time : timeLine) {
			timeDtoList.add(modelMapper.map(time, TimeLineDto.class));
		}
		response.setTimeline(timeDtoList);
		return ResponseInfo.builder().status("Success").status_code(200).message("").data(response).build();

	}

	public void addProduct(Product product) {
		productRepository.save(product);
	}

	public void addEquipment(EquipmentDto equipment) {
		Equipment equip = modelMapper.map(equipment, Equipment.class);
		String fileName = StringUtils.cleanPath(equipment.getEquipment_image().getOriginalFilename());
		UploadProfile uploadProfile = null;
		try {
			uploadProfile = UploadProfile.builder().fileName(fileName)
					.fileType(equipment.getEquipment_image().getContentType())
					.data(equipment.getEquipment_image().getBytes()).size(equipment.getEquipment_image().getSize())
					.build();
		} catch (IOException e) {
			System.out.println(e);
		}
		equip.setUploadPhoto(uploadProfile);
		equipmentRepository.save(equip);
	}

	public void addIssues(Issues issue) {
		issuesRepository.save(issue);
	}

	public void addReports(Reports reports) {
		LocalDateTime date = LocalDateTime.parse(reports.getReported_at().toString(), DateTimeFormatter.ISO_DATE_TIME);
		reports.setReported_at(date);
		reportsRepository.save(reports);
	}

	public void addActivity(Activity activity) {
		activityRepository.save(activity);
	}

	public String getDateTimewithZone(LocalDateTime ldt) {
		if (null != ldt) {
			ZonedDateTime ldtZoned = ldt.atZone(ZoneId.systemDefault());

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			return ldtZoned.format(formatter);
		} else {
			return "";
		}

	}

	public ResponseInfo getRecommendedEquipment(long clientId, Integer pageNo, Integer pageSize) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<Equipment> pagedResult = equipmentRepository.findByClientId(clientId, paging);
		List<Equipment> equipments = pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<Equipment>();
		List<Object> equipmentList = new ArrayList<Object>();

		for (Equipment equipment : equipments) {
			log.info("equipments values" + equipment.getEquipment_name());
			Map<String, Object> map = new HashMap<>();
			String fileDownloadUri = null;
			map.put("equipment_code", equipment.getId());
			if (equipment.getUploadPhoto() != null) {
				URL url1 = null;
				try {
					url1 = new URL("http://52.172.157.13:8443/");
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				fileDownloadUri = ServletUriComponentsBuilder.fromHttpUrl(url1.toString())
						.path("/images/" + equipment.getUploadPhoto().getId()).toUriString();
			}
			map.put("equipment_image", fileDownloadUri);
			map.put("equipment_price", equipment.getCurrency() + " " + equipment.getPrice());
			map.put("equipment_name", equipment.getEquipment_name());
			equipmentList.add(map);
		}
		return ResponseInfo.builder().status("Success").status_code(200).message("").data(equipmentList).build();

	}

	public ResponseInfo subscriptionsDashboard(String subscriptionType) {
		TodoSubscriptionDTO todoSubscriptionDTO = null;
		List<TodoSubscriptionDTO> todo = new ArrayList<>();
		System.out.println(subscriptionType);
		try {
			Query q = em.createNativeQuery(
					"SELECT subscriptionid, COUNT(*) FROM tb_subscriptions where subscriptiontype = '"
							+ subscriptionType + "'  GROUP BY subscriptionid ORDER BY 2 desc");

			List<Object[]> subscriptions = q.getResultList();
			for (Object[] subscriptionId : subscriptions) {
				int subscriptionIdInt = Integer.valueOf(subscriptionId[0].toString());
				int sub_count = Integer.valueOf(subscriptionId[1].toString());
//				List<Subscriptions> subscription = subscriptionRepository
//						.findBySubscriptionIdAndSubscriptionType(subscriptionIdInt, subscriptionType);
//
//				for (Subscriptions subsc : subscription) {
				
				Object[] rs = null;
				if(subscriptionType.equals("Service")) {
					Query qry = em.createNativeQuery(
							"SELECT name, care_provider_id,category_id from tbl_cs_service where service_id = ?1");
					qry.setParameter(1, subscriptionIdInt);
					rs = (Object[]) qry.getSingleResult();
				}else if(subscriptionType.equals("Package")) {
					Query qry = em.createNativeQuery(
							"SELECT name, care_provider_id,category_id from tbl_cs_package where package_id = ?1");
					qry.setParameter(1, subscriptionIdInt);
					rs = (Object[]) qry.getSingleResult();
				}else if(subscriptionType.equals("Product")) {
					Query qry = em.createNativeQuery(
							"SELECT name, care_provider_id,category_id from tbl_cs_product where product_id = ?1");
					qry.setParameter(1, subscriptionIdInt);
					rs = (Object[]) qry.getSingleResult();
				}
				
				String name = String.valueOf(rs[0]);
				String care_provider_id = String.valueOf(rs[1]);
				String category_id = String.valueOf(rs[2]);
				Long care_pro_id = Long.parseLong(care_provider_id);
				int category_id_int = Integer.parseInt(category_id);

				Query qr = em
						.createNativeQuery("SELECT careprovider_name from tb_care_provider where careprovider_id = ?1");
				qr.setParameter(1, care_pro_id);
				Object care_provider_name = (Object) qr.getSingleResult();

				Query cat_qry = em
						.createNativeQuery("SELECT category_name from tb_category_list where category_id = ?1");
				cat_qry.setParameter(1, category_id_int);
				Object category_name = (Object) cat_qry.getSingleResult();

				// Optional<ClientDetails> clients =
				// clientDetailsRepository.findById(subsc.getClientId());

				todoSubscriptionDTO = new TodoSubscriptionDTO();
				todoSubscriptionDTO.setName(name);
				todoSubscriptionDTO.setProviderName(String.valueOf(care_provider_name));
				todoSubscriptionDTO.setCategory(String.valueOf(category_name));
				todoSubscriptionDTO.setSubscriptionCount(sub_count);
				todo.add(todoSubscriptionDTO);
				if (todo.size() == 10) {
					return ResponseInfo.builder().status("Success").status_code(200).message("").data(todo).build();
				}
				// }
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("subscriptionsDashboard Mapping Query Error");
		}
		return ResponseInfo.builder().status("Success").status_code(200).message("").data(todo).build();
	}

}