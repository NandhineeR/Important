package com.qdm.cg.clients.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import com.qdm.cg.clients.entity.Reports;

@Repository
public interface ReportsRepository extends JpaRepository<Reports, Long> {

	public Page<Reports> findByClientId(long clientId, Pageable paging);
}
