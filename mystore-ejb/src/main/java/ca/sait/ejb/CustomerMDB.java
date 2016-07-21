package ca.sait.ejb;

import java.util.Date;
import java.util.UUID;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ca.sait.business.Customer_operations;
import ca.sait.entity.CustomerEntity;
import ca.sait.utils.UUIDUtils;

/**
 * Message-Driven Bean implementation class for: CustomerMDB
 */
@MessageDriven(
		//configuring the queue, tie the queue with the configuration of the web-server
		activationConfig = { @ActivationConfigProperty(
				propertyName = "destination", propertyValue = "CustomerQueue"), @ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Queue")
		}, 
		mappedName = "CustomerQueue")
public class CustomerMDB implements MessageListener {
	@Inject
	private JMSContext jmsContext;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@EJB
	private Customer_operations co;
	
	@PersistenceContext
	private EntityManager em;
	
	/**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message message) {
    	
    	
    	logger.trace("enter onMessage(Message message)");
    	//parsing the incoming message using "dataExchange" method,
    	//creating JMSProducer, configuring the returnMessage and sending it back to the Servlet
    	final TextMessage txtMessage = (TextMessage)message;

    	try {
			final TemporaryQueue replyTo = (TemporaryQueue) message.getJMSReplyTo();
			final TextMessage returnMessage = jmsContext.createTextMessage();
			final String to_servlet = dataExchange (txtMessage);
			if (to_servlet == null) 
			{
				logger.trace("we have not got any UUID");
				returnMessage.setText("fail% ");
			}
			returnMessage.setText(to_servlet);
			returnMessage.setJMSCorrelationID(txtMessage.getJMSCorrelationID());
		
			try {
				jmsContext.createProducer().send(replyTo, returnMessage);
				logger.trace("UUID is being sent back");
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				logger.trace("exit onMessage(Message message)");
			}
			
		} catch (JMSException e1) {
			// TODO Auto-generated catch block
			logger.error(e1.getMessage(), e1);
		}
    	
    }
    
	private String dataExchange (TextMessage txtMessage) {
		 //	the data from the front end is parsed here
		logger.trace("enter dataExchange (Message message)");
		
    	String backMessage = "";
    	StringBuilder sb = new StringBuilder();
		String data;
		//variable to indicate if we are updating the existing CUSTOMER object or
		//or creating a new one
		Boolean isNewCustomer = true;
			try {
				data = txtMessage.getText();
				String [] values = data.split("%");
				CustomerEntity cust = null;
				
				//try to find a user by email using the EJB method, if done,
				//avoid creating a new CUSTOMER object and update the existing one
				if ((CustomerEntity) co.getCustomer(values[4])!= null) {
					cust = (CustomerEntity) co.getCustomer(values[4]);
					isNewCustomer = false;
					logger.trace("updating the existing Customer");
				} else {
					//if Customer with indicate Email does not exist
					cust = new CustomerEntity();
				}
		
			//setting data from the front for any Customer object
				cust.setEmail(values[4]);
				cust.setFirstName(values[0]);
				cust.setLastModified(new Date());
				cust.setLastName(values[1]);
				cust.setModifiedBy("");
				cust.setPassword(values[3]);
				cust.setRoleName("guest");
				cust.setUsername(values[2]);
				
				if (isNewCustomer == true) {
					//setting the rest of the parameters for a new Customer object, inserting into the DB
					//putting it to the map being stored in the singleton bean and creating a message to the Servlet
					logger.trace("inserting a new Customer");
					cust.setUuid(UUIDUtils.getIdAsByte(UUID.randomUUID()));
					cust.setCreateDate(new Date());
					cust.setCreatedBy("guest");
					em.persist(cust);
					backMessage= (sb.append("create").append("%").append(UUIDUtils.fromByte(cust.getUuid()))).toString();
					co.updateCustomerCache(cust, "put");
				
				} else {
					//creating a message to the Servlet wich indicates the successful update
					backMessage= (sb.append("update").append("%").append(UUIDUtils.fromByte(cust.getUuid()))).toString();
					
					// here we are replacing the Customer object in the map with the updated one
					co.updateCustomerCache(cust, "replace");
				}
				em.flush();
				
				logger.debug("UUID {}: ", UUIDUtils.fromByte(cust.getUuid()).toString());
				return backMessage;
				
			} catch (JMSException e1) {
				// TODO Auto-generated catch block
				logger.error(e1.getMessage(), e1);
				return sb.append("fail").append("%").append(" ").toString();
			
		} finally {
			logger.trace("EXIT dataExchange(message)");
			
		}
    	
    }


}
