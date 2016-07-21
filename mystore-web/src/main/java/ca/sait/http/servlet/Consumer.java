/**
 * 
 */
package ca.sait.http.servlet;

import javax.jms.JMSConsumer;

/**
 * @author Olga
 *
 */
public class Consumer {

	private final JMSConsumer jmsConsumer;
	private final String correlationId;
	
	/**
	 * 
	 */
	public Consumer(String correlationId, JMSConsumer jmsConsumer) {
		this.correlationId = correlationId;
		this.jmsConsumer = jmsConsumer;
	}
	
	public String getCorrelationId() {
		return correlationId;
	}
	
	public JMSConsumer getJmsConsumer() {
		return jmsConsumer;
	}

}
