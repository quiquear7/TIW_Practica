package servlet;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;

import servlet.Usuario;

/**
 * Servlet implementation class ControladorServlet
 */
@WebServlet("/ControladorServlet")
public class ControladorServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ServletContext miServletContex = null;

	String strAutor;

	public void init() {

		ServletContext miServletContex = getServletContext();

		// Cogemos el par�metro de inicializaci�n
		miServletContex.setAttribute("autor", miServletContex.getInitParameter("autor"));

	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {

	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		
		
		Usuario _usuario= new Usuario();
		
		
		
		

		_usuario.setNombre(req.getParameter("nombre"));
		_usuario.setApellido(req.getParameter("apellido"));
		_usuario.setDireccion(req.getParameter("direccion"));
		_usuario.setEmail(req.getParameter("email"));
		_usuario.setContrasenia(req.getParameter("contrasenia"));
		_usuario.setRol(req.getParameter("rol"));
		
		HttpSession session = req.getSession(true);
		session.setAttribute("user", _usuario);
	}

}
