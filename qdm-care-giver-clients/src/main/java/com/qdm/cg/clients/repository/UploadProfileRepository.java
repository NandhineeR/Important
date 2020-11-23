package com.qdm.cg.clients.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qdm.cg.clients.entity.UploadProfile;

@Repository
public interface UploadProfileRepository extends JpaRepository<UploadProfile, Integer> {

	UploadProfile findByFileName(String fileName);

}
