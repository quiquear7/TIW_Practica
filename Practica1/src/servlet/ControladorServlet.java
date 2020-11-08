package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

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

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


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
		else if(path.compareTo("/cerrar_sesion.html")==0) {
			Usuario _usuario= new Usuario();
			login = false;
			sesion.setAttribute("sesion_iniciada", login);
			sesion.setAttribute("usuario", _usuario);
			req.getRequestDispatcher("index.jsp").forward(req, resp);
		}
		else if(path.compareTo("/cuenta.html")==0) {
			req.getRequestDispatcher("cuenta.jsp").forward(req, resp);
		}
		else if(path.compareTo("/compras_realizadas.html")==0) {
			req.getRequestDispatcher("compras_realizadas.jsp").forward(req, resp);
		}
		
		
		
		
		
		
		
		
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		
		HttpSession sesion = req.getSession();
		String path=req.getServletPath();
		
		Usuario _usuario= new Usuario();


		if(path.compareTo("/analizar-login.html")==0) {
			try {
				Context ctx = new InitialContext();
				System.out.println("iniciamos context");
				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");
				System.out.println("ds");
				Connection con = ds.getConnection();
				if (con != null) {
					System.out.println("CONEXION CORRECTA");
					Statement st = con.createStatement();
					
					ResultSet rs = st.executeQuery("Select * from usuario");
					System.out.println("rs");
					int usuario_existe = 0;
					while(rs.next()) {
						System.out.println(rs.getString(1));
						System.out.println(rs.getString(5));
						if((rs.getString(1).compareTo(req.getParameter("email"))== 0  && rs.getString(5).compareTo(req.getParameter("contrasenia"))==0 )) {
							usuario_existe = 1;
							System.out.println("Usuario existe");
							_usuario.setEmail(rs.getString(1));
							_usuario.setNombre(rs.getString(2));
							_usuario.setApellido(rs.getString(3));
							_usuario.setDireccion(rs.getString(4));
							_usuario.setContrasenia(rs.getString(5));
							_usuario.setRol(rs.getString(6));
						}

					}
					rs.close();
					st.close();
					con.close();
					System.out.println("Connection close");
					System.out.println("User"+usuario_existe);
					if(usuario_existe==1) {
						login = true;
						sesion.setAttribute("sesion_iniciada", login);
						sesion.setAttribute("usuario", _usuario);
						req.getRequestDispatcher("index.jsp").forward(req, resp);
					}
					else {
						req.getRequestDispatcher("login-incorrecto.jsp").forward(req, resp);
					}
					
				}
				else {
					System.out.println("CONEXION inCORRECTA");
					req.getRequestDispatcher("login-incorrecto.jsp").forward(req, resp);
				}
				
			} 
			catch (SQLException e) {
				
				System.out.println("Error al Obtener "+e.getMessage());
				req.getRequestDispatcher("log-incorrecto.jsp").forward(req, resp);
			}// complete
			catch (NamingException e) {
				System.out.println("Error al 2");
				req.getRequestDispatcher("login-incorrecto.jsp").forward(req, resp);
			}
			
			
		}
		else if(path.compareTo("/analizar-registro.html")==0) {
			System.out.println("entramos al path");
			
			try {
				Context ctx = new InitialContext();
				System.out.println("iniciamos context");
				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");
				System.out.println("ds");
				Connection con = ds.getConnection();
				if (con != null) {
					System.out.println("CONEXION CORRECTA");
					Statement st = con.createStatement();
					
					String script= "insert into usuario (email,nombre,apellido,direccion,contrasenia,rol) values (?,?,?,?,?,?)";
					PreparedStatement ps = con.prepareStatement(script);
					ps.setString(1, req.getParameter("email"));
					ps.setString(2, req.getParameter("nombre"));
					ps.setString(3, req.getParameter("apellido"));
					ps.setString(4, req.getParameter("direccion"));
					ps.setString(5, req.getParameter("contrasenia"));
					ps.setString(6, req.getParameter("rol"));

					int correcto = ps.executeUpdate();
					ps.close();
					st.close();
					con.close();
					System.out.println("Connection close");
					if(correcto==1) req.getRequestDispatcher("registro-correcto.jsp").forward(req, resp);
					else {
						req.getRequestDispatcher("registro-incorrecto.jsp").forward(req, resp);
					}
					
				}
				else {
					System.out.println("CONEXION inCORRECTA");
					req.getRequestDispatcher("registro-incorrecto.jsp").forward(req, resp);
				}
				
			} 
			catch (SQLException e) {
				
				System.out.println("Error al Insertar "+e.getMessage());
				req.getRequestDispatcher("registro-incorrecto.jsp").forward(req, resp);
			}// complete
			catch (NamingException e) {
				req.getRequestDispatcher("registro-incorrecto.jsp").forward(req, resp);
			}
			
	
		}
		else if(path.compareTo("/modificar_usuario-correcto.html")==0) {
			try {
				Context ctx = new InitialContext();
				System.out.println("iniciamos context");
				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");
				System.out.println("ds");
				Connection con = ds.getConnection();
				if (con != null) {
					System.out.println("CONEXION CORRECTA");
					Statement st = con.createStatement();
					
					String script= "UPDATE usuario SET nombre = ?, apellido=?, direccion=?, contrasenia=? WHERE email = ?";
					PreparedStatement ps = con.prepareStatement(script);
					
					ps.setString(1, req.getParameter("nombre"));
					ps.setString(2, req.getParameter("apellido"));
					ps.setString(3, req.getParameter("direccion"));
					ps.setString(4, req.getParameter("contrasenia"));
					ps.setString(5, req.getParameter("email"));

					int correcto = ps.executeUpdate();
					ps.close();
					st.close();
					con.close();
					System.out.println("Connection close");
					if(correcto==1) {
						_usuario.setNombre(req.getParameter("nombre"));
						_usuario.setApellido(req.getParameter("apellido"));
						_usuario.setDireccion(req.getParameter("direccion"));
						_usuario.setContrasenia(req.getParameter("contrasenia"));
						System.out.println("usuarios almacenados");
						login = true;
						sesion.setAttribute("sesion_iniciada", login);
						sesion.setAttribute("usuario", _usuario);
						req.getRequestDispatcher("modificar_usuario-correcto.jsp").forward(req, resp);
					}
					else {
						req.getRequestDispatcher("modificar-usuario-incorrecto.jsp").forward(req, resp);
					}
					
				}
				else {
					System.out.println("CONEXION INCORRECTA");
					req.getRequestDispatcher("modificar-usuario-incorrecto.jsp").forward(req, resp);
				}
				
			} 
			catch (SQLException e) {
				
				System.out.println("Error al Insertar "+e.getMessage());
				req.getRequestDispatcher("modificar-usuario-incorrecto.jsp").forward(req, resp);
			}// complete
			catch (NamingException e) {
				req.getRequestDispatcher("modificar-usuario-incorrecto.jsp").forward(req, resp);
			}
			
		}
		else if(path.compareTo("/eliminar-usuario.html")==0) {
			try {
				Context ctx = new InitialContext();
				System.out.println("iniciamos context");
				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");
				System.out.println("ds");
				Connection con = ds.getConnection();
				if (con != null) {
					System.out.println("CONEXION CORRECTA");
					Statement st = con.createStatement();
					
					String script= "DELETE FROM usuario where email like '"+req.getParameter("email")+"'";
					
					int correcto = st.executeUpdate(script);
					st.close();
					con.close();
					System.out.println("Connection close");
					if(correcto==1) {
						_usuario=null;
						login = false;
						sesion.setAttribute("sesion_iniciada", login);
						sesion.setAttribute("usuario", _usuario);
						req.getRequestDispatcher("index.jsp").forward(req, resp);
					}
					else {
						req.getRequestDispatcher("eliminar-usuario-incorrecto.jsp").forward(req, resp);
					}
					
				}
				else {
					System.out.println("CONEXION INCORRECTA");
					req.getRequestDispatcher("eliminar-usuario-incorrecto.jsp").forward(req, resp);
				}
				
			} 
			catch (SQLException e) {
				
				System.out.println("Error al Insertar "+e.getMessage());
				req.getRequestDispatcher("eliminar-usuario-incorrecto.jsp").forward(req, resp);
			}// complete
			catch (NamingException e) {
				req.getRequestDispatcher("eliminar-usuario-incorrecto.jsp").forward(req, resp);
			}
		}
		

		
		
		
		
	}

}
