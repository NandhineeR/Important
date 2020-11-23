package com.qdm.cg.clients.service;

import org.springframework.stereotype.Service;
import com.qdm.cg.clients.entity.Subscriptions;


@Service
public interface NotificationIntgService {

	public void createNotification(Subscriptions subscriptions, String notificationType, Object userIdObj);

}
