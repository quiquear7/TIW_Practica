package controller;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.sql.DataSource;

import model.Comp;
import model.Compra;

public class Pagos implements MessageListener {
	
	 private EntityManager em; 
	 private EntityTransaction et;
	 

	public void onMessage(Message message) {
		ObjectMessage msg = null;

		try {
			if (message != null) {
				if (message instanceof ObjectMessage) {

					System.out.println("Pago Recibido");
					msg = (ObjectMessage) message;

					Comp c = (Comp) msg.getObject();
					// System.out.println("Comprador" + c.getComprador());

					procesar_compra(c);
					vaciar_carro(c.getComprador());
					cambiar_estado(c.getComprador());

				}
			}

		} catch (JMSException e) {
			System.err.println("JMSException in onMessage(): " + e.toString());
		} catch (Throwable t) {
			System.err.println("Exception in onMessage():" + t.getMessage());
		}
	}

	public void procesar_compra(Comp compra) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Practica1");

		em = emf.createEntityManager();
		et = em.getTransaction();

		try {
			et.begin();
			Compra c = new Compra();
			c.setReferencia(compra.getReferencia());
			c.setVendedor(compra.getVendedor());
			c.setComprador(compra.getComprador());
			c.setPrecio(compra.getPrecio());
			c.setDireccion(compra.getDireccion());
			c.setTarjeta(compra.getTarjeta());
			c.setFecha(compra.getFecha());
			em.persist(c);

			et.commit();
		} catch (Exception e) {
			if (et != null) {
				et.rollback();
			}
		}

	}

	public void procesar_compraJDBC(Comp compra) {
		System.out.println("Procesamos compra");
		try {
			Context ctx = new InitialContext();

			DataSource ds = (DataSource) ctx.lookup("jdbc/practica");

			Connection con = ds.getConnection();

			Statement st = con.createStatement();

			String script = "insert into compra (referencia,vendedor,comprador,precio,direccion,tarjeta,fecha) values (?,?,?,?,?,?,?)";
			PreparedStatement ps = con.prepareStatement(script);
			ps.setString(1, compra.getReferencia());
			ps.setString(2, compra.getVendedor());
			ps.setString(3, compra.getComprador());
			ps.setFloat(4, compra.getPrecio());
			ps.setString(5, compra.getDireccion());
			ps.setString(6, compra.getTarjeta());
			ps.setString(7, compra.getFecha());
			ps.executeUpdate();
			ps.close();
			st.close();
			con.close();

		} catch (

		SQLException e) {

			System.out.println("Error al Insertar " + e.getMessage());

		} // complete
		catch (NamingException e) {

		}
	}

	public void vaciar_carro(String comprador) {
		System.out.println("Borramos carro");
		try {
			Context ctx = new InitialContext();

			DataSource ds = (DataSource) ctx.lookup("jdbc/practica");

			Connection con = ds.getConnection();

			Statement st = con.createStatement();
			String script = "delete from carro where usuario like '" + comprador + "'";
			st.executeUpdate(script);
			st.close();
			con.close();

		} catch (SQLException e) {

		} // complete
		catch (NamingException e) {

		}

	}

	public void cambiar_estado(String referencia) {
		try {
			Context ctx = new InitialContext();

			DataSource ds = (DataSource) ctx.lookup("jdbc/practica");

			Connection con = ds.getConnection();

			String[] parts = referencia.split("-");
			for (int i = 0; i < parts.length; i++) {
				Statement st = con.createStatement();
				String script = "UPDATE producto SET estado='" +true+"' WHERE referencia ='" +parts[i]+"'";
				st.executeUpdate(script);
				st.close();
			}
			con.close();

		} catch (SQLException e) {

			System.out.println("Error al Insertar " + e.getMessage());

		} // complete
		catch (NamingException e) {

		}
	}

}
