package com.qdm.cg.clients.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qdm.cg.clients.entity.TimeLine;

@Repository
public interface TimeLineRepository  extends  JpaRepository<TimeLine, Long>{

	public List<TimeLine> findByProductId(long productId);
}
