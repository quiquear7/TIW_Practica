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

import javax.jms.Session;

import model.Comp;


public class ReadConfJMS {
	private ConnectionFactory cf;
	private Destination d;

	public ReadConfJMS(ConnectionFactory cf, Destination d) {
		super();
		this.cf = cf;
		this.d = d;
	}

	public List<Comp> read() {
		List<Comp> contenidos = null;
		Connection c = null;
		Session s = null;
		MessageConsumer mc = null;
		try {
			c = cf.createConnection();
			s = c.createSession();
			mc = s.createConsumer(d);

			c.start();
			contenidos = new ArrayList<Comp>();

			Message m = null;
			do {
				m = mc.receive(100);
				if(mc!= null && m instanceof ObjectMessage) {
					ObjectMessage aux = (ObjectMessage) m;
					contenidos.add((Comp) aux.getObject());
				}
			}while(m!=null);

	
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

		return contenidos;
	}

}
