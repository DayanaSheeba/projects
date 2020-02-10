package Demo.SpringBoot_Solace_Sample.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import Demo.SpringBoot_Solace_Sample.model.Customer;
import Demo.SpringBoot_Solace_Sample.service.SolaceMessageService;

@Component
public class SolaceMessageServiceImpl implements SolaceMessageService{

	private static final Logger logger = LoggerFactory.getLogger(SolaceMessageServiceImpl.class);

    public void processSolaceMessage(Customer customer) {

        logger.info(customer.getId());
    }
}
