package com.qdm.cg.clients.serviceimpl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.qdm.cg.clients.dto.ClientRegisterationDTO;
import com.qdm.cg.clients.dto.OrderDto;
import com.qdm.cg.clients.entity.ClientDetails;
import com.qdm.cg.clients.entity.Subscriptions;
import com.qdm.cg.clients.entity.UploadProfile;
import com.qdm.cg.clients.repository.ClientDetailsRepository;
import com.qdm.cg.clients.repository.SubscriptionsRepository;
import com.qdm.cg.clients.repository.UploadProfileRepository;
import com.qdm.cg.clients.service.ClientService;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

	@Autowired
	ClientDetailsRepository clientDetailsRepository;

	@Autowired
	SubscriptionsRepository subscriptionsRepository;

	@Autowired
	ModelMapper modelMapper;

	@PersistenceContext
	private EntityManager em;

	@Override
	public ClientDetails clientRegisteration(ClientRegisterationDTO clientDetails) {
		if (clientDetails.getDob() != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String dobDate = formatter.format(clientDetails.getDob());
			try {
				Date d = formatter.parse(dobDate);
				Calendar c = Calendar.getInstance();
				c.setTime(d);
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH) + 1;
				int date = c.get(Calendar.DATE);
				LocalDate l1 = LocalDate.of(year, month, date);
				LocalDate now1 = LocalDate.now();
				Period diff1 = Period.between(l1, now1);
				clientDetails.setAge(diff1.getYears());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		ClientDetails clientsReg = modelMapper.map(clientDetails, ClientDetails.class);
		if (clientsReg.getUploadPhoto() != null) {
			String fileName = StringUtils.cleanPath(clientDetails.getUploadPhoto().getOriginalFilename());
			UploadProfile uploadProfile = null;
			try {
				uploadProfile = UploadProfile.builder().fileName(fileName)
						.fileType(clientDetails.getUploadPhoto().getContentType())
						.data(clientDetails.getUploadPhoto().getBytes()).size(clientDetails.getUploadPhoto().getSize())
						.build();
			} catch (IOException e) {
				System.out.println(e);
			}
			clientsReg.setUploadPhoto(uploadProfile);
		}
		return clientDetailsRepository.save(clientsReg);
	}

	@Override
	public List<ClientDetails> getAllClientsDetails() {
		return clientDetailsRepository.findAll();
	}

	@Override
	public ClientDetails getClientByClientId(Integer clientid) {
		Optional<ClientDetails> client = clientDetailsRepository.findById(clientid);
		if (client.isPresent()) {
			return client.get();
		} else {
			return null;
		}
	}

	@Override
	public ClientDetails updateClientSubscriptions(Integer clientId, Set<Integer> subscriptionList) {
		Optional<ClientDetails> client = clientDetailsRepository.findById(clientId);
		Set<Integer> subscriptionsList = new HashSet<>();
		if (client.isPresent()) {
			for (Integer subscriptions : client.get().getSubscriptions()) {
				subscriptionsList.add(subscriptions);
			}
		} else {
			return null;
		}
		for (Integer subscriptionListId : subscriptionList) {
			Subscriptions sub = subscriptionsRepository.findBySubscriptionId(subscriptionListId);
			subscriptionsList.add(sub.getSubscriptionId());
		}
		client.get().setSubscriptions(subscriptionsList);
		return clientDetailsRepository.save(client.get());
	}

	@Autowired
	private UploadProfileRepository dbFileRepository;

	public UploadProfile getFile(int fileId) {
		return dbFileRepository.findById(fileId).get();
	}

	@Override
	public List<ClientDetails> getAllClients(Integer pageNo, Integer pageSize, String sortDirec, String sortFiled) {
		Pageable paging = PageRequest.of(pageNo, pageSize,
				sortDirec.toLowerCase().startsWith("desc") ? Direction.DESC : Direction.ASC, sortFiled);
		Page<ClientDetails> pagedResult = clientDetailsRepository.findAll(paging);
		return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<ClientDetails>();
	}

	@Override
	public List<OrderDto> searchClient(Integer pageNo, Integer pageSize, String name, String sortDirec,
			String sortfield) {
		List<OrderDto> orderList = new ArrayList<>();
		Pageable paging = PageRequest.of(pageNo, pageSize,
				sortDirec.toLowerCase().startsWith("desc") ? Direction.DESC : Direction.ASC, sortfield);
		Page<ClientDetails> pagedResult = null;
		if (null != name) {
			pagedResult = clientDetailsRepository.findByName(name.toLowerCase(), paging);
		} else {
			pagedResult = clientDetailsRepository.findAll(paging);
		}
		try {

			if (pagedResult.hasContent()) {
				List<ClientDetails> detailsList = pagedResult.getContent();
				for (ClientDetails details : detailsList) {
					orderList.add(
							OrderDto.builder().label(details.getName()).value(String.valueOf(details.getId())).build());

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orderList;
	}

	@Override
	public ClientDetails clientModify(ClientRegisterationDTO clientDetails) {
		if (clientDetails.getDob() != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String dobDate = formatter.format(clientDetails.getDob());
			try {
				Date d = formatter.parse(dobDate);
				Calendar c = Calendar.getInstance();
				c.setTime(d);
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH) + 1;
				int date = c.get(Calendar.DATE);
				LocalDate l1 = LocalDate.of(year, month, date);
				LocalDate now1 = LocalDate.now();
				Period diff1 = Period.between(l1, now1);
				clientDetails.setAge(diff1.getYears());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		ClientDetails clientsReg = modelMapper.map(clientDetails, ClientDetails.class);
		Optional<ClientDetails> clientsRegDetail = clientDetailsRepository.findById(clientDetails.getId());
		if (clientsReg.getUploadPhoto() != null) {
			String fileName = StringUtils.cleanPath(clientDetails.getUploadPhoto().getOriginalFilename());
			UploadProfile uploadProfile = null;
			try {
				uploadProfile = UploadProfile.builder().fileName(fileName)
						.fileType(clientDetails.getUploadPhoto().getContentType())
						.data(clientDetails.getUploadPhoto().getBytes()).size(clientDetails.getUploadPhoto().getSize())
						.build();
			} catch (IOException e) {
				System.out.println(e);
			}
			clientsReg.setUploadPhoto(uploadProfile);
		} else {
			clientsReg.setUploadPhoto(clientsRegDetail.get().getUploadPhoto());
		}

		return clientDetailsRepository.save(clientsReg);
	}

	@Override
	public List<ClientDetails> getAllClientsDetailsBasedOnServiceProfessional(long careGiverId) {
		List<Subscriptions> subslist = subscriptionsRepository.findByCareGiverId(careGiverId);

		List<ClientDetails> clientDetails = new ArrayList<>();

		if (subslist.size() > 0) {
			for (Subscriptions subs : subslist) {
				clientDetails.add(getClientByClientId(subs.getClientId()));
			}
		}

		return clientDetails;

	}

	@Override
	public List<ClientDetails> getAllClientsBasedonProvider(long providerId) {
		List<Subscriptions> subslist = subscriptionsRepository.findByCareProviderId(providerId);

		List<ClientDetails> clientDetails = new ArrayList<>();

		if (subslist.size() > 0) {
			for (Subscriptions subs : subslist) {
				clientDetails.add(getClientByClientId(subs.getClientId()));
			}
		}

		return clientDetails;
	}

	@Override
	public List<ClientDetails> getClientsListing(Integer pageNo, Integer pageSize, String sortDirec, String sortfield,
			Object id, Object role,String searchName) {
		String user_role = String.valueOf(role);
		long user_id = Long.parseLong(id.toString());
		System.out.println("Role and Id - " + user_role + "-" + user_id);
		List<ClientDetails> newArray = new ArrayList<ClientDetails>();
		if (user_id != 0 && user_role != null) {
			 if (user_role.equals("super-admin")) {
		             newArray=clientDetailsRepository.findAll();
			 }
			else if (user_role.equals("service_professional")) {
				try {
					Query q = em
							.createNativeQuery("SELECT DISTINCT id from tb_activity WHERE service_professional_id = ?1");
					q.setParameter(1, user_id);
					for (Object clientListId : q.getResultList()) {
						String stringToConvert = String.valueOf(clientListId);
						int convertedLong = Integer.parseInt(stringToConvert);
						Optional<ClientDetails> clientsRepository = clientDetailsRepository.findById(convertedLong);
						newArray.add(clientsRepository.get());
					}
				} catch (Exception e) {
					System.out.println(e);
				}
				return searchName!=null ? newArray.stream().filter(s->s.getName().contains(searchName)).collect(Collectors.toList()):newArray;
			} else if (user_role.equals("provider_admin")) {
				try {
					Query q = em
							.createNativeQuery("SELECT DISTINCT caregiver_id from tb_provider_giver WHERE careprovider_careprovider_id = ?1");
					q.setParameter(1, user_id);
					for (Object careGiverId : q.getResultList()) {
						String stringToConvert = String.valueOf(careGiverId);
						long convertedLong = Long.parseLong(stringToConvert);
						Query qry = em
								.createNativeQuery("SELECT DISTINCT id from tb_activity WHERE service_professional_id = ?1");
						qry.setParameter(1, convertedLong);
						for (Object clientId : qry.getResultList()) {
							String clientIdConvert = String.valueOf(clientId);
							int clientIdInt = Integer.parseInt(clientIdConvert);
							Optional<ClientDetails> clientsRepository = clientDetailsRepository.findById(clientIdInt);
							newArray.add(clientsRepository.get());
						}
					}
				} catch (Exception e) {
					System.out.println(e);
				}
				return searchName!=null ? newArray.stream().filter(s->s.getName().contains(searchName)).collect(Collectors.toList()):newArray;	
			} else if (user_role.equals("service_coordinator")) {
				try {
					Query q = em
							.createNativeQuery("SELECT DISTINCT careprovider_careprovider_id from tb_provider_coordinator WHERE carecoordinator_id = ?1");
					q.setParameter(1, user_id);
					for (Object careProviderId : q.getResultList()) {
						String stringToConvert = String.valueOf(careProviderId);
						long convertedLong = Long.parseLong(stringToConvert);
						Query qry = em
								.createNativeQuery("SELECT DISTINCT caregiver_id from tb_provider_giver WHERE careprovider_careprovider_id = ?1");
						q.setParameter(1, convertedLong);
						for (Object careGiverId : qry.getResultList()) {
							String careGiverIdStr = String.valueOf(careGiverId);
							long careGiverLong = Long.parseLong(careGiverIdStr);
							Query qy = em
									.createNativeQuery("SELECT DISTINCT id from tb_activity WHERE service_professional_id = ?1");
							qy.setParameter(1, careGiverLong);
							for (Object clientId : qy.getResultList()) {
								String clientIdConvert = String.valueOf(clientId);
								int clientIdInt = Integer.parseInt(clientIdConvert);
								Optional<ClientDetails> clientsRepository = clientDetailsRepository.findById(clientIdInt);
								newArray.add(clientsRepository.get());
							}
						}
					}
				} catch (Exception e) {
					System.out.println(e);
				}
				return searchName!=null ? newArray.stream().filter(s->s.getName().contains(searchName)).collect(Collectors.toList()):newArray;	
			} else if (user_role.equals("provider_supervisor")) {
				try {
					System.out.println("provider_supervisor");
					Query q = em
							.createNativeQuery("SELECT DISTINCT careprovider_id from tb_supervisor_provider WHERE caresupervisor_caresupervisor_id = ?1");
					q.setParameter(1, user_id);
					for (Object careProviderId : q.getResultList()) {
						String stringToConvert = String.valueOf(careProviderId);
						long convertedLong = Long.parseLong(stringToConvert);
						Query qry = em
								.createNativeQuery("SELECT DISTINCT caregiver_id from tb_provider_giver WHERE careprovider_careprovider_id = ?1");
						q.setParameter(1, convertedLong);
						for (Object careGiverId : qry.getResultList()) {
							String careGiverIdStr = String.valueOf(careGiverId);
							long careGiverLong = Long.parseLong(careGiverIdStr);
							Query qy = em
									.createNativeQuery("SELECT DISTINCT id from tb_activity WHERE service_professional_id = ?1");
							qy.setParameter(1, careGiverLong);
							for (Object clientId : qy.getResultList()) {
								String clientIdConvert = String.valueOf(clientId);
								int clientIdInt = Integer.parseInt(clientIdConvert);
								Optional<ClientDetails> clientsRepository = clientDetailsRepository.findById(clientIdInt);
								newArray.add(clientsRepository.get());
							}
						}
					}
				} catch (Exception e) {
					System.out.println(e);
				}
				return searchName!=null ? newArray.stream().filter(s->s.getName().contains(searchName)).collect(Collectors.toList()):newArray;
			}
		}
		return searchName!=null ? newArray.stream().filter(s->s.getName().contains(searchName)).collect(Collectors.toList()):newArray;	
	}
}
