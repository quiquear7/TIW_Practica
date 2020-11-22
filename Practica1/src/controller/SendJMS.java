package controller;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import model.Mensaje;

public class SendJMS {
	public SendJMS() {
		super();
		try {
			InitialContext ic = new InitialContext();
			cf = (ConnectionFactory) ic.lookup("jms/practica");
			d = (Destination) ic.lookup("jms/queuepractica");
		} catch (NamingException e) {

			e.printStackTrace();
		}

	}

	private ConnectionFactory cf;
	private Destination d;

	public void Send(Mensaje mensajeobj) {
		Connection c = null;
		Session s = null;
		MessageProducer mp = null;
		try {
			c = cf.createConnection();
			s = c.createSession();
			mp = s.createProducer(d);

			ObjectMessage om = s.createObjectMessage(mensajeobj);

			c.start();

			mp.send(om);

		} catch (JMSException e) {

			e.printStackTrace();
		} finally {
			try {
				if (mp != null)
					mp.close();

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
