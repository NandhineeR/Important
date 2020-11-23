package com.qdm.cg.clients.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qdm.cg.clients.entity.Subscriptions;
import com.qdm.cg.clients.response.ResponseInfo;


@Repository
public interface SubscriptionsRepository extends JpaRepository<Subscriptions, Integer>{

	Subscriptions findBySubscriptionId(Integer subscriptionListId);
	List<Subscriptions> findByClientId(Integer clientId);
	List<Subscriptions> findByCareGiverId(Long careGiverId);
	Optional<Subscriptions> findById(Integer subscriptionListId);
	@Query(value = "SELECT distinct clientid FROM tb_subscriptions WHERE careProviderId=:careProviderId", nativeQuery = true)
	List<Long> findByCareProviderIdClient(Long careProviderId);
	@Query(value = "SELECT distinct clientid FROM tb_subscriptions WHERE careCoordiantorId=:careCoordiantorId", nativeQuery = true)
	List<Integer> findByCareCoordiantorId(Integer careCoordiantorId);
	List<Subscriptions> findByCareProviderId(Long careProviderId);
	List<Subscriptions> findBySubscriptionType(String subscriptionType);
	List<Subscriptions> findBySubscriptionIdAndSubscriptionType(int i, String subscriptionType);
	
}
