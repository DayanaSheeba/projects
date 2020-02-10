package Demo.SpringBoot_Solace_Sample.publisher;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Demo.SpringBoot_Solace_Sample.config.BeanConfig;

@Service
public class SolJMSMessageProducer {

	@Autowired
	private BeanConfig beanConfig;
	
	Connection conn;
	Session session;
	
	Topic destinationTopic;
	MessageProducer mp;
	
	public volatile boolean isConnected = false;
	
	private static final Logger logger = LoggerFactory.getLogger(SolJMSMessageProducer.class);
	
	private void createConnection() throws Exception{
		ConnectionFactory connFac = beanConfig.solConnectionFactory();
		conn = connFac.createConnection();
		createSession();
		logger.info("Topic COnnection Successful");
	}
	
	private void createSession() throws JMSException,NamingException{
		if(mp != null) {
			try {
				mp.close();			
			}catch(JMSException e) {
				logger.info("Exception while closing Message Producer");
			}
		}if(session !=null) {
		try {
			session.close();
		} catch (JMSException e) {
			logger.info("Exception while closing Session");
		}	
		}
		session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		logger.info("After creating session");
		
		destinationTopic = (Topic)session.createTopic("New Topic");
		mp = session.createProducer(destinationTopic);
		
		logger.info("After creating producer");		
	}
	
	public void sendRequestToQueue(String wrapper) throws JMSException {
		sendRequestToQueue(wrapper,null);
	}
	
	public void sendRequestToQueue(String wrapper, String ID) throws JMSException {
		try {
			
			if(!isConnected) {
				createConnection();
			}
			TextMessage tm = session.createTextMessage("  {\r\n" + 
					"    \"id\": \"5e264d1f699e03d148ea27a1\",\r\n" + 
					"    \"firstName\": \"Test4\",\r\n" + 
					"    \"lastName\": \"Person4\",\r\n" + 
					"    \"query\": \"How is it \"\r\n" + 
					"  }");
			
		/*	TextMessage tm = session.createTextMessage("Testing");*/
			tm.setJMSReplyTo(destinationTopic);
			tm.setJMSCorrelationID(ID);
			mp.send(tm);
			
		} catch (Exception e) {
			logger.info("Exception occured in sendRequestToQueue");
			mp.close();
			session.close();
			conn.close();
		}
	}
	
	
}
