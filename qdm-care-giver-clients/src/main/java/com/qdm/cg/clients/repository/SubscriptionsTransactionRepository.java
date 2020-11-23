package com.qdm.cg.clients.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qdm.cg.clients.entity.SubscriptionsTransaction;

@Repository
public interface SubscriptionsTransactionRepository extends JpaRepository<SubscriptionsTransaction, Integer> {

}
