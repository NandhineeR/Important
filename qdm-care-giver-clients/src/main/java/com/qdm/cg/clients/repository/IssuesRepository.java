package com.qdm.cg.clients.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qdm.cg.clients.entity.Issues;

@Repository
public interface IssuesRepository extends JpaRepository<Issues, Long> {

	public Page<Issues> findByClientId(long clientId, Pageable paging);

}
