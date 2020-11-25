package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import model.Mensaje;

public class ReadJMS {
	private ConnectionFactory cf;
	private Destination d;

	public ReadJMS(ConnectionFactory cf, Destination d) {
		super();
		this.cf = cf;
		this.d = d;
	}

	public List<Mensaje> read() {
		List<Mensaje> contenidos = null;
		Connection c = null;
		Session s = null;
		QueueBrowser qb = null;
		try {
			c = cf.createConnection();
			s = c.createSession();
			qb = s.createBrowser((Queue) d);

			c.start();
			contenidos = new ArrayList<Mensaje>();

			List<Message> men = Collections.list(qb.getEnumeration());

			for (Message m : men) {
				if(m!=null) {
					if (m instanceof ObjectMessage) {
						ObjectMessage aux = (ObjectMessage) m;
						contenidos.add((Mensaje) aux.getObject());
					}
				}
			}
		} catch (JMSException e) {

			e.printStackTrace();
		} finally {
			try {
				if (qb != null)
					qb.close();

				if (s != null)
					s.close();

				if (c != null)
					c.close();

			} catch (JMSException e) {
				e.printStackTrace();
			}

		}

		return contenidos;
	}

}
