package Demo.SpringBoot_Solace_Sample.config;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.solacesystems.jms.SolConnectionFactory;
import com.solacesystems.jms.SolJmsUtility;

import Demo.SpringBoot_Solace_Sample.listener.JmsExceptionListener;
import Demo.SpringBoot_Solace_Sample.listener.MGJmsMessageListener;
import Demo.SpringBoot_Solace_Sample.listener.VCCJmsMessageListener;

@Configuration
@PropertySource({"classpath:application.properties"})

public class BeanConfig {

    private static final Logger logger = LoggerFactory.getLogger(BeanConfig.class);

    @Autowired
    private Environment environment;

    @Autowired
    private JmsExceptionListener exceptionListener;
    


    @Bean
    public SolConnectionFactory solConnectionFactory() throws Exception {
        SolConnectionFactory connectionFactory = SolJmsUtility.createConnectionFactory();
        connectionFactory.setHost(environment.getProperty("solace.java.host"));
        connectionFactory.setVPN(environment.getProperty("solace.java.msgVpn"));
        connectionFactory.setUsername(environment.getProperty("solace.java.clientUsername"));
        connectionFactory.setPassword(environment.getProperty("solace.java.clientPassword"));
      //  connectionFactory.setClientID(environment.getProperty("solace.java.clientName"));
        return connectionFactory;
    }

    @Bean
    public MGJmsMessageListener jmsMessageListener() {
        return new MGJmsMessageListener();
    }
    
    
    @Bean
    public VCCJmsMessageListener vccJmsMessageListener() {
        return new VCCJmsMessageListener();
    }
  

    @Bean(destroyMethod = "close")
    public Connection mgConnection() {
        Connection connection = null;
        javax.jms.Session session;
        try {
            connection = solConnectionFactory().createConnection();
            session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(environment.getProperty("solace.message.consumer.queue"));
            MessageConsumer messageConsumer = session.createConsumer(queue);
            messageConsumer.setMessageListener(jmsMessageListener());
            connection.setExceptionListener(exceptionListener);
            connection.start();
            logger.info("Connected. Awaiting message...");
        } catch (Exception e) {
            logger.info("JMS connection failed with Solace." + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }
    
    @Bean(destroyMethod = "close")
    public Connection vccConnection() {
        Connection vccConnection = null;
        javax.jms.Session session;
        try {
        	vccConnection = solConnectionFactory().createConnection();
            session = vccConnection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            Queue vccQueue = session.createQueue(environment.getProperty("solace.message.consumer.queue.vcc"));
            MessageConsumer messageConsumer = session.createConsumer(vccQueue);
            messageConsumer.setMessageListener(vccJmsMessageListener());
            vccConnection.setExceptionListener(exceptionListener);
            vccConnection.start();
            logger.info("Connected. Awaiting message...");
        } catch (Exception e) {
            logger.info("JMS connection failed with Solace." + e.getMessage());
            e.printStackTrace();
        }
        return vccConnection;
    }
    

    

}
