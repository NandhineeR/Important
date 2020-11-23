package com.qdm.cg.clients.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qdm.cg.clients.entity.ClientDetails;

@Repository
public interface ClientDetailsRepository extends JpaRepository<ClientDetails, Integer>{
	@Query("Select c from ClientDetails c where lower(c.name) like %:name%")
	Page<ClientDetails> findByName(@Param("name")String name, Pageable paging);

}
