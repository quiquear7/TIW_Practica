package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import model.Mensaje;

public class Listener {
	private ConnectionFactory cf = null;
	private Destination d = null;
	private Connection c = null;
	private Session s = null;
	private MessageConsumer mc = null;
	
	public Listener(ConnectionFactory cf, Destination d) {
		super();
		this.cf = cf;
		this.d = d;
	}

	public void read() {
		
		try {
			c = cf.createConnection();
			s = c.createSession(false,Session.AUTO_ACKNOWLEDGE);
			mc = s.createConsumer(d);

			mc.setMessageListener(new Pagos());

			c.start();

			System.out.println("Listener Iniciado");

		} catch (JMSException e) {

			//e.printStackTrace();
		}
	}
}
