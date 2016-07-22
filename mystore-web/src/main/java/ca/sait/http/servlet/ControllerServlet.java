package ca.sait.http.servlet;

import java.io.IOException;
import java.util.UUID;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.sait.business.Customer_operations;
import ca.sait.model.Customer;

/**
 * Servlet implementation class ControllerServlet
 */
@WebServlet("/customer")
public class ControllerServlet extends HttpServlet {

	private static final long serialVersionUID = -6766881957454442252L;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private InitialContext context;
	
	
	@Inject
	private JMSContext jmsContext;
	
	@Resource(mappedName="java:/jms/queue/CustomerQueue")
	private Queue queue;
	private TemporaryQueue tmpQueue;
	
	private ThreadLocal<JMSProducer> jmsProducer;
	private ThreadLocal<Consumer> consumer;
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init() throws ServletException {
		logger.trace("ENTER init()");
		//the INIT method is being used to create an instance of InitialContext
		//and ThreadLocals for JMSProducer and JMSConsumer
		 try {
	        	context = new InitialContext();
			} catch (NamingException e) {
				throw new ServletException(e);
			} finally {
				logger.trace("EXIT init()");
			}
		
		jmsProducer = new ThreadLocal<JMSProducer>() {
			
			@Override
			protected JMSProducer initialValue() {
				logger.trace("ENTER initialValue()");
				
				try {
					return jmsContext.createProducer();
				} finally {
					logger.trace("EXIT initialValue()");
				}
			}
		};

		consumer = new ThreadLocal<Consumer>() {
			
			@Override
			protected Consumer initialValue() {
				logger.trace("ENTER initialValue()");
				try {
					final String cid = UUID.randomUUID().toString();
					final JMSConsumer jmsConsumer = jmsContext.createConsumer(tmpQueue);
					return new Consumer(cid, jmsConsumer);
				} finally {
					logger.trace("EXIT initialValue()");
				}
			}
		};

		logger.trace("EXIT init()");
	}


	/**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    	Customer_operations co;
    	
    	logger.info("enter doGet()");

    	try {
    		//creating an instance of CustomCacheBean class
			co = (Customer_operations)context.lookup("java:app/mystore-ejb-0.0.1-SNAPSHOT/CustomCacheBean!ca.sait.business.Customer_operations");
			
			//getting the parameters from the user and defining the logic to process them
			//processing the return message and setting the request attributes to pass the
			//data to the front end depending if the data has been found or not
			
			final String action = request.getParameter("action");
			switch (action) {
			case "add":
				logger.trace("starting to update or create a customer");
				 //we need to create a string with customer param and send to the message bean
				StringBuilder ssb = new StringBuilder();
				String data = (ssb.append(request.getParameter("firstName")).append("%").append(request.getParameter("lastName")).
						append("%").append(request.getParameter("userName")).append("%").
						append(request.getParameter("password")).append("%").append(request.getParameter("email"))).toString();
				
				//sending the message to the MDB and getting the return message 
				final TextMessage cust = get_Customer (data);
				
				//processing the return message 
			try {
				logger.debug("our final message: {}", cust.getText());
				final String [] cust_info = (cust.getText()).split("%");
				if (cust_info[0].equals("update")) {
				
					request.setAttribute("success", "customer updated");
					request.setAttribute("uuid", cust_info[1]);
				} else {
					if (cust_info[0].equals("create")) {
						request.setAttribute("success", "customer created");
						request.setAttribute("uuid", cust_info[1]);
					
				} else {
					request.setAttribute("success", "no data found");
				}
				}
			} catch (JMSException e1) {
				// TODO Auto-generated catch block
				request.setAttribute("success", "we failed");
				logger.error(e1.getMessage(), e1);
			}
				break;
				
			
			case "get_all":
				// receiving the data related to all the customers which is stored in the singleton bean map
				//and setting the array of CUSTOMER objects as an attribute
				logger.trace("getting a customers array");
				try {
					
				final Customer[] customers = co.getAllCustomers();
				if (customers.length > 0) 
				{
					request.setAttribute("success", "we are done");
				} else 
				{
					request.setAttribute("success", "sorry, there are no customers");
				}
				request.setAttribute("customers", customers);
				
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					request.setAttribute("success", "we failed");
				}
				break;
				
			case "get_by_email":
				logger.trace("searching a customer by email");
				
				//searching for a certain user in the map using the Email as a key. Setting 
				//the CUSTOMER object as a request attribute if found
				try {
					final String email = request.getParameter("cust_email");
					final Customer customer = co.getCustomer (email);
					if (customer!= null) {
						logger.debug("customer name: {}", customer.getFirstName());
						request.setAttribute("success", "we are done");
						request.setAttribute("cust", customer);
					} else {
						logger.debug("customer name not found");
						request.setAttribute("success", "no customer with this email has been found");
						
					}
					
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					request.setAttribute("success", "we failed");	
				}
				
		}
		
    	} catch (NamingException e2) {
			// TODO Auto-generated catch block
			logger.error(e2.getMessage(), e2);
			request.setAttribute("success", "we failed");
		}
		
    	//creating a RequestDispatcher object to incluse the given web page in the response
		request.getRequestDispatcher("/WEB-INF/jsp/listAllCustomers.jsp").include(request, response);
		logger.info("exit doGet()");

    }
    
    
    //this method is to create a message from the formatted string containing the data from the request,
    //to set the destination and parameters of the message ("JMSReplyTo" and "CorrelationID") and
    //to send and to receive the messages using the available threads of JMSProducer and JMSConsumer
    protected TextMessage get_Customer (String data) {
    	
    	logger.trace("enter get_Customer (String data)");
    	
    	final Destination destination = queue;
    	tmpQueue = jmsContext.createTemporaryQueue();
    	final Consumer jmsConsumer = consumer.get();
    	final TextMessage txtMessage = jmsContext.createTextMessage(data);
    	try {
			txtMessage.setJMSReplyTo(tmpQueue);
			txtMessage.setJMSCorrelationID(jmsConsumer.getCorrelationId());
		} catch (final JMSException ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		jmsProducer.get().send(destination, txtMessage);
		
        final TextMessage returnMessage = (TextMessage)jmsConsumer.getJmsConsumer().receive();
		logger.trace("exit get_Customer (String data)");
		return returnMessage;
    	
    }
   

}
