package controller;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;


import model.Comp;


public class Pagos implements MessageListener {
	

	 

	public void onMessage(Message message) {
		ObjectMessage msg = null;

		try {
			if (message != null) {
				if (message instanceof ObjectMessage) {

					msg = (ObjectMessage) message;
					
					Comp c = (Comp) msg.getObject();
				
					SendConfJMS sc = new SendConfJMS();
					sc.Send(c);
					

				}
			}

		} catch (JMSException e) {
			
			System.err.println("JMSException in onMessage(): " + e.toString());
		} catch (Throwable t) {
			
			System.err.println("Exception in onMessage():" + t.getMessage());
		}
	}
}
