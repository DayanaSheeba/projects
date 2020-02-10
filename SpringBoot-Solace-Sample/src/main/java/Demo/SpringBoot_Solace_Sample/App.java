package Demo.SpringBoot_Solace_Sample;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import Demo.SpringBoot_Solace_Sample.publisher.SolJMSMessageProducer;

@SpringBootApplication

public class App 
{
		
private static final Logger logger = LoggerFactory.getLogger(App.class);

	
    public static void main( String[] args )
    {
    	ApplicationContext applicationContext = SpringApplication.run(App.class, args);
    	
        SolJMSMessageProducer solMsgPro = applicationContext.getBean(SolJMSMessageProducer.class);
        try {
        	logger.info("Before Sending Msg");
			solMsgPro.sendRequestToQueue("Testing");
			logger.info("After Sending Message");
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

        
    }
}
