package servlet;

import java.io.IOException;
import java.io.PrintWriter;

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
	boolean login = false;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ServletContext miServletContex = null;

	String strAutor;

	public void init() {

		/*ServletContext miServletContex = getServletContext();

		// Cogemos el par�metro de inicializaci�n
		miServletContex.setAttribute("autor", miServletContex.getInitParameter("autor"));*/

	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		
		HttpSession sesion = req.getSession(true);
		sesion.setAttribute("sesion_iniciada", login);
		
		String path= req.getServletPath();
		
		
		if(path.compareTo("/login.html")==0) {
			req.getRequestDispatcher("login.jsp").forward(req, resp);
		}
		else if(path.compareTo("/registro.html")==0) {
			req.getRequestDispatcher("registro.jsp").forward(req, resp);
		}
		else if(path.compareTo("/index.html")==0) {
			req.getRequestDispatcher("index.jsp").forward(req, resp);
		}
		else if(path.compareTo("/modificar_usuario.html")==0) {
			req.getRequestDispatcher("modificar_usuario.jsp").forward(req, resp);
		}
		
		
		
		
		
		
		
		
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		
		HttpSession sesion = req.getSession();
		String path=req.getServletPath();
		
		Usuario _usuario= new Usuario();
		
		
		System.out.println("POST: "+path);
		if(path.compareTo("/analizar-login.html")==0) {
			login = true;
			sesion.setAttribute("sesion_iniciada", login);
			_usuario.setEmail(req.getParameter("email"));
			sesion.setAttribute("usuario", _usuario);
			RequestDispatcher miR = req.getRequestDispatcher("index.jsp");
			miR.forward(req, resp);
		}
		else if(path.compareTo("/analizar-registro.html")==0) {
			System.out.println("Registro Completo");
			_usuario.setNombre(req.getParameter("nombre"));
			_usuario.setApellido(req.getParameter("apellido"));
			_usuario.setDireccion(req.getParameter("direccion"));
			_usuario.setEmail(req.getParameter("email"));
			_usuario.setContrasenia(req.getParameter("contrasenia"));
			_usuario.setRol(req.getParameter("rol"));
			req.getRequestDispatcher("registro-correcto.jsp").forward(req, resp);
			
		}
		else if(path.compareTo("/modificar_usuario-correcto.html")==0) {
			req.getRequestDispatcher("modificar_usuario-correcto.jsp").forward(req, resp);
		}
		
		

		
		
		
		
	}

}
