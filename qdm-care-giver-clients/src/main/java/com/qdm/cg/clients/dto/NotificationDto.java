package com.qdm.cg.clients.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class NotificationDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private boolean isRead;
	private int recipientId;
	private int deviceTokenId;
	private int senderId;
	private String pageComponent;
	private int notificationCategoryId;
	private String notificationContent;
	private String componentUrl;
	private long typeId;
	private boolean isDeleted;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private int clientId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	public int getDeviceTokenId() {
		return deviceTokenId;
	}
	public void setDeviceTokenId(int deviceTokenId) {
		this.deviceTokenId = deviceTokenId;
	}
	public String getPageComponent() {
		return pageComponent;
	}
	public void setPageComponent(String pageComponent) {
		this.pageComponent = pageComponent;
	}
	public int getNotificationCategoryId() {
		return notificationCategoryId;
	}
	public void setNotificationCategoryId(int notificationCategoryId) {
		this.notificationCategoryId = notificationCategoryId;
	}
	public String getNotificationContent() {
		return notificationContent;
	}
	public void setNotificationContent(String notificationContent) {
		this.notificationContent = notificationContent;
	}
	public String getComponentUrl() {
		return componentUrl;
	}
	public void setComponentUrl(String componentUrl) {
		this.componentUrl = componentUrl;
	}
	public long getTypeId() {
		return typeId;
	}
	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	public int getRecipientId() {
		return recipientId;
	}
	public void setRecipientId(int recipientId) {
		this.recipientId = recipientId;
	}
	public int getSenderId() {
		return senderId;
	}
	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	
}
