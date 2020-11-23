package com.qdm.cg.clients.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qdm.cg.clients.dto.PackageServiceBean;
import com.qdm.cg.clients.dto.ProductServiceMappingBean;
import com.qdm.cg.clients.dto.SubscriptionTrasactionDto;
import com.qdm.cg.clients.entity.Subscriptions;
import com.qdm.cg.clients.entity.SubscriptionsTransaction;
import com.qdm.cg.clients.repository.ProductMappingRepository;
import com.qdm.cg.clients.repository.SubscriptionsTransactionRepository;
import com.qdm.cg.clients.service.SubscriptionTransactionService;

@Service
public class SubscriptionTransactionServiceImpl implements SubscriptionTransactionService{

	@Autowired
	SubscriptionsTransactionRepository subsTransRepo;
	@Autowired
	ModelMapper mapper;
	@Autowired
	ProductMappingRepository productMappingRepository;
	
	@Override
	public void addSubscriptionTrans(Subscriptions subscriptions) {
		
		if(subscriptions.getSubscriptionType().equalsIgnoreCase("Product"))
		{
			List<ProductServiceMappingBean> beanList=productMappingRepository.getProductServiceMapping(subscriptions.getSubscriptionId());
		for(ProductServiceMappingBean bean:beanList) {
			SubscriptionsTransaction subsTrans=new SubscriptionsTransaction();
			subsTrans.setSubscriptionId(subscriptions.getSubscriptionId());
			subsTrans.setClientId(subscriptions.getClientId());
			subsTrans.setCurrentBalance(bean.getQuantity());
		subsTrans.setOpeningBalance(bean.getQuantity());
			subsTrans.setActive(subscriptions.isActive());
			subsTrans.setServiceId(bean.getServiceId());
			subsTransRepo.save(subsTrans);
		}
		}else if(subscriptions.getSubscriptionType().equalsIgnoreCase("Package")) {
			List<PackageServiceBean> beanList=productMappingRepository.getPackageServiceMapping(subscriptions.getSubscriptionId());
			for(PackageServiceBean bean:beanList) {
				SubscriptionsTransaction subsTrans=new SubscriptionsTransaction();
				subsTrans.setSubscriptionId(subscriptions.getSubscriptionId());
				subsTrans.setClientId(subscriptions.getClientId());
				subsTrans.setCurrentBalance(bean.getQuantity());
				subsTrans.setOpeningBalance(bean.getQuantity());
				subsTrans.setServiceId(bean.getServiceId());
				subsTrans.setActive(subscriptions.isActive());
				subsTransRepo.save(subsTrans);
			}	
		}else if(subscriptions.getSubscriptionType().equalsIgnoreCase("Service")) {
			SubscriptionsTransaction subsTrans=new SubscriptionsTransaction();
			subsTrans.setSubscriptionId(subscriptions.getSubscriptionId());
			subsTrans.setClientId(subscriptions.getClientId());
			subsTrans.setCurrentBalance(1);
			subsTrans.setOpeningBalance(1);
			subsTrans.setServiceId(subscriptions.getSubscriptionId());
			subsTrans.setActive(subscriptions.isActive());
			subsTransRepo.save(subsTrans);
		}
		
	}

}
