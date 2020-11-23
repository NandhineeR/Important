package com.qdm.cg.clients.serviceimpl;

import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qdm.cg.clients.dto.SubscriptionsDTO;
import com.qdm.cg.clients.entity.Subscriptions;
import com.qdm.cg.clients.repository.SubscriptionsRepository;
import com.qdm.cg.clients.service.NotificationIntgService;
import com.qdm.cg.clients.service.SubscriptionTransactionService;
import com.qdm.cg.clients.service.SubscriptionsService;

@Service
@Transactional
public class SubscriptionsServiceImpl implements SubscriptionsService {

	@Autowired
	SubscriptionsRepository subscriptionsRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	NotificationIntgService notificationIntgService;
	
	@Autowired
	SubscriptionTransactionService subscriptionTransactionService;
	
	@Override
	public Subscriptions addSubscriptions(SubscriptionsDTO subscriptionsDTO, Object userIdObj) {
		Subscriptions subscription = modelMapper.map(subscriptionsDTO, Subscriptions.class);
		subscription.setActive(Boolean.parseBoolean(subscriptionsDTO.getActive()));
		Subscriptions subscriptions = subscriptionsRepository.save(subscription);
		subscriptionTransactionService.addSubscriptionTrans(subscriptions);
		notificationIntgService.createNotification(subscriptions,"client_info",userIdObj);
		return subscriptions;
	}

	@Override
	public List<Subscriptions> getSubscriptions() {
		return subscriptionsRepository.findAll();
	}
	@Override
	public Subscriptions getSubscription(int id) {
		System.out.println("subscriptions values "+id);
		return subscriptionsRepository.findById(id).get();
	}

}
