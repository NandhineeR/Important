package com.qdm.cg.clients.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qdm.cg.clients.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	public Page<Product> findByClientId(long clientId, Pageable paging);
	public List<Product> findByClientId(long clientId);

}
