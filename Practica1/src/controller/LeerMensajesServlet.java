package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Usuario;

import java.util.ArrayList;

import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * Implementacion del Servlet de Leer Mensajes
 */
@WebServlet({ "/LeerMensajesServlet", "/leerMensajes.html" })
public class LeerMensajesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource
	ConnectionFactory connectF; // Definimos la ConnectionFactory
	@Resource(mappedName = "connectF")
	Queue cola;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LeerMensajesServlet() {
		super();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest req, HttpServletResponse res)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		try {

			doPost(req, res); // llamamos al metodo doPost
		} catch (Exception e) {
			System.out.println("Error en el doGet: " + e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest req, HttpServletResponse res)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		ArrayList<String> mensajes = new ArrayList<String>();
		ArrayList<String> envios = new ArrayList<String>();
		try {
			// Creamos la conexion
			javax.jms.Connection conexion = connectF.createConnection();
			// Creamos la sesion
			javax.jms.Session sesiones = conexion.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// E iniciamos la conexion
			conexion.start();
			// Creamos la sesion de un cliente y filtramos los mensajes por su email
			HttpSession sesion = req.getSession(true);
			Usuario aux = (Usuario) sesion.getAttribute("usuario");
			String receptor = "receptor=" + "'" + aux.getEmail() + "'";
			javax.jms.MessageConsumer mc = sesiones.createConsumer(cola, receptor);
			Message men = null;
			while (true) { // Mientras la sesion este abierta
				men = mc.receive(1000);
				if (men != null) { // Mientras haya mensajes
					if (men instanceof TextMessage) {
						TextMessage m = (TextMessage) men;
						mensajes.add(m.getText());
						envios.add(m.getStringProperty("enviados"));
					} else {
						break;
					}
				} else // no hay mensajes
				{
					break;
				}
			}
			sesion.setAttribute("mensajes", mensajes);
			sesion.setAttribute("envios", envios);
			// llamamos al jsp correspondiente con el Dispatcher
			req.getRequestDispatcher("leerMensajes.jsp").forward(req, res);

			// Cerramos la sesion
			sesiones.close();

			// Cerramos la conexion
			conexion.close();

		} catch (Exception e) {
			System.out.println("Error en el doPost: " + e);
		}
	}

}
