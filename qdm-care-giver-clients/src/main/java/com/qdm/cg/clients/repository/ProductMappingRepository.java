package com.qdm.cg.clients.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.qdm.cg.clients.dto.PackageServiceBean;
import com.qdm.cg.clients.dto.ProductServiceMappingBean;
import com.qdm.cg.clients.dto.UpcomingActivity;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ProductMappingRepository {

	@PersistenceContext
	private EntityManager em;
	
	public List<ProductServiceMappingBean> getProductServiceMapping(int id) {
		List<ProductServiceMappingBean> bean=new ArrayList<>();
		List<Object[]> result = new ArrayList<>();
		try {
			Query q = em.createNativeQuery(
					"SELECT service_id, product_id,quantity FROM tbl_product_service_mapping WHERE product_id = ?1");
			q.setParameter(1, id);
			result=	q.getResultList();
			for(Object[] object:result) {
				bean.add(ProductServiceMappingBean.builder()
						.productId(Integer.valueOf(object[1].toString()))
						.serviceId(Integer.valueOf(object[0].toString()))
						.quantity(Integer.valueOf(object[2].toString()))
						.build());
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Product List Mapping Query Error");
		}
		return bean;
	}
	
	public List<PackageServiceBean> getPackageServiceMapping(int id) {
		List<PackageServiceBean> bean=new ArrayList<>();
		List<Object[]> result = new ArrayList<>();
		try {
			Query q = em.createNativeQuery(
					"SELECT service_id, package_id,quantity FROM tbl_package_service_mapping WHERE package_id = ?1");
			q.setParameter(1, id);
	     result=	q.getResultList();
	     for(Object[] object:result) {
				bean.add(PackageServiceBean.builder()
						.packageId(Integer.valueOf(object[1].toString()))
						.serviceId(Integer.valueOf(object[0].toString()))
						.quantity(Integer.valueOf(object[2].toString()))
						.build());
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Product List Mapping Query Error");
		}
		return bean;
	}

}
