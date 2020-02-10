package Demo.SpringBoot_Solace_Sample.service;

import Demo.SpringBoot_Solace_Sample.model.Customer;

public interface SolaceMessageService {
	
	void processSolaceMessage(Customer customer);
}
