package com.qdm.cg.clients.serviceimpl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.qdm.cg.clients.dto.NotificationDto;
import com.qdm.cg.clients.entity.ClientDetails;
import com.qdm.cg.clients.entity.Subscriptions;
import com.qdm.cg.clients.service.NotificationIntgService;


@Service
@Transactional
public class NotificationIntgServcieImpl implements NotificationIntgService {

	@PersistenceContext
	EntityManager em;
	
	@Override
	public void createNotification(Subscriptions subscriptions, String notificationType, Object userIdObj) {
		NotificationDto notificationDto = new NotificationDto();
		notificationDto.setRead(false);
		notificationDto.setRecipientId(Integer.parseInt(subscriptions.getCareCoordiantorId() + ""));
		notificationDto.setSenderId(Integer.parseInt(userIdObj.toString()));
		notificationDto.setPageComponent(notificationType);
		notificationDto.setCreatedAt(new Timestamp(new Date().getTime()));
		notificationDto.setUpdatedAt(new Timestamp(new Date().getTime()));
		notificationDto.setComponentUrl("");
		// find by type
		Object[] notificationCategoryObj = getNotificationCategoryByType("new_client");
		notificationDto.setNotificationCategoryId(Integer.parseInt(notificationCategoryObj[0] + ""));
		notificationDto.setTypeId(0);
		String template = notificationCategoryObj[1] + "";
		notificationDto.setNotificationContent(template);
		notificationDto.setClientId(subscriptions.getClientId());
		createNotificationByQuery(notificationDto);
	}

	private Object[] getNotificationCategoryByType(String activityType) {
		try {
			Query q = em.createNativeQuery("select * from tb_notification_category where notification_type = ?1");
			q.setParameter(1, activityType);
			List results = q.getResultList();
			if (results.size() > 0) {
				for (int i = 0; i < results.size(); i++) {
					Object[] obj = (Object[]) results.get(i);
					return obj;
				}
			}
		} catch (Exception e) {
			System.out.println("e : "+e);
		}
		return null;
	}

	private void createNotificationByQuery(NotificationDto notificationDto) {

		Query q = em.createNativeQuery("INSERT INTO tb_notification(\n"
				+ " component_url, created_at, device_token_id, is_deleted, is_read, notification_category_id, notification_content, page_component, recipient_id, sender_id, updated_at,type_id,client_id)\n"
				+ " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?);");
		q.setParameter(1, notificationDto.getComponentUrl());
		q.setParameter(5, false);
		q.setParameter(4, false);
		q.setParameter(2, new Date());
		q.setParameter(3, 0);
		q.setParameter(6, notificationDto.getNotificationCategoryId());
		q.setParameter(7, notificationDto.getNotificationContent());
		q.setParameter(8, notificationDto.getPageComponent());
		q.setParameter(9, notificationDto.getRecipientId());
		q.setParameter(10, notificationDto.getSenderId());
		q.setParameter(11, new Date());
		q.setParameter(12, notificationDto.getTypeId());
		q.setParameter(13, notificationDto.getClientId());
		q.executeUpdate();
	
	}

}
