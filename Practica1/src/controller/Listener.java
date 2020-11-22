package controller;



import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;

import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class Listener {
	private InitialContext ic = null;
	private ConnectionFactory cf = null;
	private Destination d = null;
	private Connection c = null;
	private Session s = null;
	private MessageConsumer mc = null;
	

	public void read() {
		
		try {
			ic = new InitialContext();
			cf = (ConnectionFactory) ic.lookup("jms/practica");
			d = (Destination) ic.lookup("jms/queueAsinpractica");
			c = cf.createConnection();
			s = c.createSession(false,Session.AUTO_ACKNOWLEDGE);
			mc = s.createConsumer(d);

			mc.setMessageListener(new Pagos());

			c.start();

			System.out.println("Listener Iniciado");

		} catch (JMSException e) {
			System.out.println("Error en el listenetr");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			System.out.println("Error 2 el listenetr");
		}
	}
}
