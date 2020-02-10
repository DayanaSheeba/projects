package Demo.SpringBoot_Solace_Sample.listener;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JmsExceptionListener implements ExceptionListener {
    private static final Logger logger = LoggerFactory.getLogger(JmsExceptionListener.class);

    public void onException(JMSException e) {
        logger.error(e.getLinkedException().getMessage());
        e.printStackTrace();
    }
}
