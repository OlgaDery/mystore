package ca.sait.business;

import ca.sait.model.Customer;
//template for the EJB CustomCacheBean class

public interface Customer_operations {

	void updateCustomerCache(Customer customer, String operarion);

	Customer[] getAllCustomers();

	Customer getCustomer(String email);

}