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
	private ConnectionFactory cf;
	private Destination d;

	public Listener(ConnectionFactory cf, Destination d) {
		super();
		this.cf = cf;
		this.d = d;
	}

	public void read() {
		Connection c = null;
		Session s = null;
		MessageConsumer mc = null;
		try {
			c = cf.createConnection();
			s = c.createSession();
			mc = s.createConsumer(d);
			

			c.start();
			
			mc.setMessageListener(new Pagos());
			
		} catch (JMSException e) {

			e.printStackTrace();
		} finally {
			try {
				if (mc != null)
					mc.close();

				if (s != null)
					s.close();

				if (c != null)
					c.close();

			} catch (JMSException e) {
				e.printStackTrace();
			}

		}
	}

}
