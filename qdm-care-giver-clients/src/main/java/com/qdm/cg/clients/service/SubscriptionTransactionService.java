package com.qdm.cg.clients.service;

import org.springframework.stereotype.Service;

import com.qdm.cg.clients.dto.SubscriptionTrasactionDto;
import com.qdm.cg.clients.entity.Subscriptions;
@Service
public interface SubscriptionTransactionService {

	void addSubscriptionTrans(Subscriptions subscriptions);
}
