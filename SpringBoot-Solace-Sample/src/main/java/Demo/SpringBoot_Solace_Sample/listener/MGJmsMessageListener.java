package Demo.SpringBoot_Solace_Sample.listener;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.databind.ObjectMapper;

import Demo.SpringBoot_Solace_Sample.model.Customer;
import Demo.SpringBoot_Solace_Sample.service.SolaceMessageService;

@Validated
@Component
public class MGJmsMessageListener implements MessageListener {
	
	private static final Logger logger = LoggerFactory.getLogger(MGJmsMessageListener.class);

    @Autowired
    private SolaceMessageService messageService;
    
    public void onMessage(Message message) {
        String messageData;
        Customer customer;
        Boolean valid = false;
        if(message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage)message;
            try {
                messageData = textMessage.getText();
                logger.info("Destination of the msg :: "+textMessage.getJMSDestination());
                
                logger.info(messageData);	
                                                
                // process data accordingly 
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    customer = objectMapper.readValue(messageData, Customer.class);
                    if(customer == null) {
                        logger.error("Invalid message from the solace queue");
                    }else {
                        logger.info("Successfully parsed solace message to object.");
                        valid = checkIfValidRequest(customer);
                        logger.info("valid:: "+valid);
                        messageService.processSolaceMessage(customer);
                    }
                } catch (IOException e) {
                    logger.error("Error while parsing JSON from Solace.");
                    e.printStackTrace();
                }              
                
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }else {
            logger.info(message.toString());
            logger.info("Invalid message. Skipping ....");
        }

    }

	private Boolean checkIfValidRequest(@Valid Customer customer) {	
		logger.info("Bean passed is valid");
		return true;
	}
    
    

    
}
