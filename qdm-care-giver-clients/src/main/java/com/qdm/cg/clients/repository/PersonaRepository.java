package com.qdm.cg.clients.repository;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.qdm.cg.clients.dto.Category;
import com.qdm.cg.clients.dto.ClientActivityInfo;
import com.qdm.cg.clients.dto.ClientActivitySummaryDto;
import com.qdm.cg.clients.dto.OrderDto;
import com.qdm.cg.clients.dto.ProviderBean;
import com.qdm.cg.clients.dto.UpcomingActivity;
import com.qdm.cg.clients.entity.ClientDetails;
import com.qdm.cg.clients.service.ClientService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PersonaRepository {

	@PersistenceContext
	private EntityManager em;
	@Autowired
	private ClientService clientService;

	public ProviderBean getGiver(long l, int categoryId) {
		Object[] bean = null;
		ProviderBean providerBean = new ProviderBean();
		try {
			Query q = em.createNativeQuery(
					"SELECT caregiver_id,caregiver_name,upload_photo_id from tb_care_giver WHERE caregiver_id = ?1");
			q.setParameter(1, l);
			bean = (Object[]) q.getSingleResult();
			System.out.println("get caregiver_id Id" + l);
			providerBean.setId(l);
			providerBean.setName(bean[1].toString());
			if (bean[2] != null) {
				String fileDownloadUri = "http://52.172.157.13:8443/images"
						+ "/" + bean[2];
				providerBean.setProfile_pic(fileDownloadUri);
			}
			try {
				Category cat = getCategory(categoryId);
				providerBean.setCategory(
						OrderDto.builder().label(cat.getName()).value(String.valueOf(cat.getId())).build());
			} catch (Exception e) {
				log.error("category List Mapping Query Error");
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("caregiver_id List Mapping Query Error");
		}
		System.out.println("check valkues" + providerBean.toString());

		return providerBean;
	}

	public ProviderBean getCoordinator(int coordiantorId, int categoryId) {
		Object[] bean = null;
		ProviderBean providerBean = new ProviderBean();
		try {
			Query q = em.createNativeQuery(
					"SELECT carecoordinator_id,carecoordinator_name,upload_photo_id from tb_care_coordinator WHERE carecoordinator_id = ?1");
			q.setParameter(1, coordiantorId);
			bean = (Object[]) q.getSingleResult();
			System.out.println("get carecoordinator_id Id" + coordiantorId);
			providerBean.setId(coordiantorId);
			providerBean.setName(bean[1].toString());
			if (bean[2] != null) {
				String fileDownloadUri = "http://52.172.157.13:8443/images"
						+ "/" + bean[2];
				providerBean.setProfile_pic(fileDownloadUri);
			}
			try {
				Category cat = getCategory(categoryId);
				providerBean.setCategory(
						OrderDto.builder().label(cat.getName()).value(String.valueOf(cat.getId())).build());
			} catch (Exception e) {
				log.error("category List Mapping Query Error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("carecoordinator_id List Mapping Query Error");
		}
		System.out.println("check valkues" + providerBean.toString());

		return providerBean;
	}

	public Category getCategory(int categoryId) {
		System.out.println("get category_id Id" + categoryId);
		Object[] bean = null;
		Category categoryBean = new Category();
		try {
			Query q = em.createNativeQuery(
					"SELECT category_id, category_name FROM tb_category_list WHERE category_id = ?1");
			q.setParameter(1, categoryId);
			bean = (Object[]) q.getSingleResult();
			System.out.println("get category_id Id" + categoryId);
			categoryBean.setId(categoryId);
			categoryBean.setName(bean[1].toString());

		} catch (Exception e) {
			e.printStackTrace();
			log.error("careprovider_id List Mapping Query Error");
		}
		System.out.println("check valkues" + categoryBean.toString());

		return categoryBean;
	}

	public ProviderBean getProvider(long careProviderId, int categoryId) {
		Object[] bean = null;
		ProviderBean providerBean = new ProviderBean();
		try {
			Query q = em.createNativeQuery(
					"SELECT careprovider_id,careprovider_name,upload_photo_id from TB_CARE_PROVIDER WHERE careprovider_id = ?1");
			q.setParameter(1, careProviderId);
			bean = (Object[]) q.getSingleResult();
			System.out.println("get careprovider_id Id" + careProviderId);
			providerBean.setId(careProviderId);
			providerBean.setName(bean[1].toString());
			if (bean[2] != null) {
				String fileDownloadUri = "http://52.172.157.13:8443/images"
						+ "/" + bean[2];
				providerBean.setProfile_pic(fileDownloadUri);
			}
			try {
				Category cat = getCategory(categoryId);
				providerBean.setCategory(
						OrderDto.builder().label(cat.getName()).value(String.valueOf(cat.getId())).build());
			} catch (Exception e) {
				log.error("category List Mapping Query Error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("careprovider_id List Mapping Query Error");
		}
		System.out.println("check valkues" + providerBean.toString());

		return providerBean;
	}

	public ClientActivitySummaryDto getActivity(long activityId) {
		Object[] bean = null;
		ClientActivitySummaryDto activityBean = new ClientActivitySummaryDto();
		try {
			Query q = em.createNativeQuery(
					"select id,activity_id,title,coalesce(activity_status, 'PENDING') as activity_status,to_char(from_time_stamp,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') as from_time_stamp,to_char(to_time_stamp,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') as to_time_stamp,"
					+ "activity_type_id,description,mode,occurence,service_professional_id, to_char(check_in,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') as check_in,to_char(check_out,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') as check_out "
					+ " from  tb_activity where activity_id = ?1");
			q.setParameter(1, activityId);
			bean = (Object[]) q.getSingleResult();
			System.out.println("get activityId Id" + activityId);
		activityBean.setClient_id(Integer.valueOf(bean[0].toString()));
			activityBean.setActivity_id(activityId);
			activityBean.setActivity_name(String.valueOf(bean[2]));
			activityBean.setActivity_status(String.valueOf(bean[3]));
			activityBean
					.setClient_name(clientService.getClientByClientId(Integer.valueOf(bean[0].toString())).getName());
			activityBean.setFrom_time_stamp(String.valueOf(bean[4]));
			activityBean.setTo_time_stamp(String.valueOf(bean[5]));
			log.info("Get Activty Type ID"+bean[6].toString());
			if (bean[6] != null) {
				activityBean.setActivity_type(getActivityType(Integer.valueOf(bean[6].toString())));
			}
			activityBean.setActivity_description(String.valueOf(bean[7]));
			activityBean.setMode(String.valueOf(bean[8]));
			activityBean.setOccurence(String.valueOf(bean[9]));
			log.info("Get Activty Type ID"+bean[10].toString());
			if (bean[10] != null) {
				activityBean.setService_professional_info(getServiceProfessional(Long.valueOf(bean[10].toString())));
			}
			activityBean.setCheck_in(bean[11].toString());
			activityBean.setCheck_out(bean[12].toString());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("getActivity List Mapping Query Error");
		}
		System.out.println("check valkues" + activityBean.toString());

		return activityBean;
	}

	public int todatefromdateActivityDetails(Integer clientId, String fromTimeStamp, String toTimeStamp) {
		List<Object[]> result = new ArrayList<>();
		int activity_count=0;
		try {
//			to_char(from_time_stamp,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"')
			String queryString="Select id,activity_id,title,activity_status,to_char(from_time_stamp,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') as from_time_stamp"
					+ " FROM tb_activity "
					+ " WHERE (from_time_stamp,to_time_stamp) overlaps ('"+ fromTimeStamp +"','" + toTimeStamp +" ')  "
					+ " AND id ="+clientId;
			log.error("Query message    "+queryString);
		Query query = em.createNativeQuery(queryString);
		result = query.getResultList();
		result.stream().forEach(elem -> System.out.println("element " + elem));
		activity_count=result.size();
//		em.getTransaction().commit();
		}catch (Exception e) {
			e.printStackTrace();
			log.error("todatefromdateActivityDetails List Mapping Query Error");
		}
		
		return activity_count;
	}

	public List<Object[]> getUpcomingActivity(Integer clientId, String fromTimeStamp, String toTimeStamp) {
		List<Object[]> result = new ArrayList<>();
		try {
		Query query = em
				.createNativeQuery("Select id,activity_id,title,coalesce(activity_status, 'N'),to_char(from_time_stamp,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') as from_time_stamp "
						+ "FROM tb_activity " +  " WHERE from_time_stamp>'"+ fromTimeStamp + "' AND id = :clientId");
		query.setParameter("clientId", clientId);
		result = query.getResultList();
		result.stream().forEach(elem -> System.out.println("element " + elem));
//		em.getTransaction().commit();
		}catch (Exception e) {
			e.printStackTrace();
			log.error("getUpcomingActivity List Mapping Query Error");
		}
		return result;
	}

	public List<Object[]> getPastActivity(Integer clientId, String fromTimeStamp, String toTimeStamp) {
		List<Object[]> result = new ArrayList<>();
		try {
			Query query = em.createNativeQuery(
					"Select id,activity_id,title,coalesce(activity_status, 'N') as activity_status,to_char(from_time_stamp,'YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') as from_time_stamp "
							+ "FROM tb_activity " +  " WHERE from_time_stamp<'"+fromTimeStamp + "' AND id= :clientId");
			query.setParameter("clientId", clientId);
			result = query.getResultList();
			result.stream().forEach(elem -> System.out.println("element " + elem));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("getPastActivity List Mapping Query Error");
		}
//		em.getTransaction().commit();
		return result;
	}

	public OrderDto getActivityType(int activityTypeId) {
		log.error("activityType Id"+activityTypeId);
		OrderDto activityType = new OrderDto();
		try {
			Query query = em.createNativeQuery("Select activity_type_id,activity_type_name,activity_identifier FROM tb_activity_type WHERE activity_type_id = ?1");
			query.setParameter(1, activityTypeId);
			Object[] result = (Object[]) query.getSingleResult();
			if (result != null) {
				activityType.setLabel(result[1].toString());
				activityType.setValue(result[0].toString());
				activityType.setIdentifier(result[2].toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("activityType List Mapping Query Error");
		}

		return activityType;

	}

	public OrderDto getServiceProfessional(long careProviderId) {
		OrderDto serviceProfessional = new OrderDto();
		try {
			Query q = em.createNativeQuery(
					"SELECT caregiver_id,caregiver_name from tb_care_giver WHERE caregiver_id = ?1");
			q.setParameter(1, careProviderId);
			Object[] bean = (Object[]) q.getSingleResult();
			if (bean != null) {
				serviceProfessional.setLabel(bean[1].toString());
				serviceProfessional.setValue(bean[0].toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("caregiver_id List Mapping Query Error");
		}
		return serviceProfessional;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getManageClientsModule(String fromTimeStamp,String toTimeStamp ) {
		log.info("entering");
		List<Object> listresponse=new ArrayList<>();
		try {
			Query q = em.createNativeQuery("select count(1)  as total_clients from tb_client_details"
			+" union all"
			+" select count(distinct id)  as in_appointments from tb_activity ta "+" WHERE (from_time_stamp,to_time_stamp) overlaps ('"+ fromTimeStamp +"','" + toTimeStamp +" ')"
			+" union all"
			+" select count(distinct clientid)  as non_scubscriptions from TB_SUBSCRIPTIONS ta2");
		List<Object> list=q.getResultList();
		log.info("Total client values"+list.size());
		JSONObject jsonObject1=new JSONObject();
		JSONObject jsonObject2=new JSONObject();
		JSONObject jsonObject3=new JSONObject();
		JSONObject jsonObject4=new JSONObject();
		jsonObject1.put("count", list.get(0));
		jsonObject1.put("label", "Total Clients");
		jsonObject2.put("count",list.get(1));
		jsonObject2.put("label", "In Appointments");
		jsonObject3.put("count", Integer.valueOf(list.get(0).toString())-Integer.valueOf(list.get(2).toString()));
		jsonObject3.put("label", "Non Subscribed Clients");
		jsonObject4.put("count", 0);
		jsonObject4.put("label", "Medical Attention Required");
		listresponse.add(jsonObject1);
		listresponse.add(jsonObject2);
		listresponse.add(jsonObject3);
		listresponse.add(jsonObject4);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Appoinments getting error");
		}
		return listresponse;
	}
	
	public List<UpcomingActivity> getClientUpcomingActivty(Integer clientId, String fromTimeStamp, String toTimeStamp) {
		List<UpcomingActivity> upcoming_activity=new ArrayList<>();
		List<Object[]> upcoming=getUpcomingActivity(clientId, fromTimeStamp, toTimeStamp);
		try {
		for(Object[] obj:upcoming) {
			UpcomingActivity up=new UpcomingActivity();
			up.setActivity_id(obj[1].toString());
			up.setTitle(obj[2].toString());
			ClientActivityInfo info=new ClientActivityInfo();
			ClientDetails clientDetails=clientService.getClientByClientId(Integer.valueOf(obj[0].toString()));
			if(clientDetails!=null) {
				info.setId(clientDetails.getId());
				info.setValue(clientDetails.getId());
				info.setLabel(clientDetails.getName());
				info.setName(clientDetails.getName());
				info.setGender(clientDetails.getGender());
				info.setAge(clientDetails.getAge());
				
				if (clientDetails.getUploadPhoto() != null) {
					URL url1 = null ;
					try {
						url1 = new URL("http://52.172.157.13:8443/");
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
					String fileDownloadUri = ServletUriComponentsBuilder.fromHttpUrl(url1.toString())
							.path("/images/" + clientDetails.getUploadPhoto().getId()).toUriString();
					info.setProfie_pic(fileDownloadUri);
				} else {
					info.setProfie_pic("");
				}	
			}
			up.setClient(info);
			upcoming_activity.add(up);
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return upcoming_activity;
	}
	
//	 end 
}
