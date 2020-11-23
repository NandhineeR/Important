package com.qdm.cg.clients.service;

import java.util.List;
import java.util.Set;

import com.qdm.cg.clients.dto.ClientRegisterationDTO;
import com.qdm.cg.clients.dto.OrderDto;
import com.qdm.cg.clients.entity.ClientDetails;
import com.qdm.cg.clients.entity.UploadProfile;

public interface ClientService {

	ClientDetails clientRegisteration(ClientRegisterationDTO clientDetails);

	List<ClientDetails> getAllClientsDetails();

	ClientDetails getClientByClientId(Integer clientid);

	ClientDetails updateClientSubscriptions(Integer clientId, Set<Integer> subscriptionList);

	UploadProfile getFile(int fileId);

	List<ClientDetails> getAllClients(Integer pageNo, Integer pageSize, String sortDirec, String sortfield);

	List<OrderDto> searchClient(Integer pageNo, Integer pageSize, String careProviderName, String sortDirec,
			String sortfield);

	ClientDetails clientModify(ClientRegisterationDTO clientDetails);

	List<ClientDetails> getAllClientsDetailsBasedOnServiceProfessional(long careGiverId);

	List<ClientDetails> getAllClientsBasedonProvider(long providerId);

	List<ClientDetails> getClientsListing(Integer pageNo, Integer pageSize, String sortDirec, String sortfield,
			Object id, Object role,String searchName);
}
