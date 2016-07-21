package ca.sait.ejb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.sait.business.Customer_operations;
import ca.sait.entity.CustomerEntity;
import ca.sait.model.Customer;

/**
 * Session Bean implementation class CustomCacheBean implements Customer_operations interface.
 * The single instance of this class is being created on the application startup.
 */
@Singleton
@Local(Customer_operations.class)
@Startup
public class CustomCacheBean implements Customer_operations {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	//this map gets initialized on the startup of the app, later the content of it may be modified 
    private Map<String, Customer> customers = new HashMap<>();
    
    @PersistenceContext
	private EntityManager entityManager;
    
    @PostConstruct
    private void postConstruct() {
    	//not include it in the interface
    	
    	logger.trace("enter postConstruct()");
    	//Here we are getting a list of all the customers from the DB and put them into CUSTOMERS map. It is done only
    	//ones when the project is being deployed
    	@SuppressWarnings("unchecked")
		final List <CustomerEntity> cust_list = entityManager.createNamedQuery(CustomerEntity.findAllCust).getResultList();
    	for (CustomerEntity c: cust_list) {
    		customers.put(c.getEmail(), c);
    		logger.debug("email: {} ", c.getEmail());
    	}
    	logger.debug("the map size {}:", customers.size());
    	
    	logger.trace("exit postConstruct()");
    }
    
    @PreDestroy
    private void preDestroy() {
    	//not include it in the interface
    	logger.trace("enter preDestroy()");
    	logger.trace("exit preDestroy()");
    }
    

    @Override
	public void updateCustomerCache(Customer customer, String operation) {
    	//This method is called from the MDB to modify the "customers" map after the Customer object is inserted/updated
    	// in the DB. Depending on if the Customer object has been found in the map or not,
    	// the object is put in the map directly or after having removed the object with the same email
    	// but invalid parameters.
        logger.trace("enter updateCustomerCache(Customer customer, String operation)");
    	
    	switch (operation) {
    	case ("put") :
    	customers.put(customer.getEmail(), customer); //email will be the key
    	logger.trace("customer added to the map");
    	break;
    	
    	case ("replace") :
    	customers.remove(customer.getEmail());
    	customers.put(customer.getEmail(), customer);
    	logger.trace("customer updated in the map");
    	break;
    	}
    	logger.trace("exit updateCustomerCache(Customer customer, String operation)");
    	
    }
    
    
    /* (non-Javadoc)
	 * @see ca.sait.ejb.Customer_operations#getAllCustomers()
	 */
    @Override
	public Customer[] getAllCustomers() {
    	//return the array of the map values
    	logger.trace ("enter getAllCustomers()");
    	List <Customer> all_customers = new ArrayList <Customer> (customers.size());
    	customers.forEach((k, v) ->
    	all_customers.add(v)
    	
    	);
    	Customer[] cust_arr = new Customer[all_customers.size()];
    	cust_arr = all_customers.toArray(cust_arr);
    	logger.trace ("exit getAllCustomers()");
        return cust_arr;
        
    }
    
    /* (non-Javadoc)
	 * @see ca.sait.ejb.Customer_operations#getCustomer(java.lang.String)
	 */
    @Override
    //method is being called from the servlet by user to find a certain customer or from the MDB
    //to determine if the Customer object with this email already exists.
	public Customer getCustomer(String email) {
    	logger.trace ("enter getCustomer(String email)");
    	 //
    	try {
    		Customer c = customers.get(email);
    		return c;
    		 
    	} finally {
    		logger.trace ("exit getCustomer(String email)");
    		
    		
    	}
        
    }

}
