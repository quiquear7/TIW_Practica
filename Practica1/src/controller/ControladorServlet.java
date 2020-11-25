package controller;

import java.util.List;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.sql.*;
import java.util.ArrayList;

import java.util.Date;
import javax.servlet.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.imageio.ImageIO;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;

import model.Carro;
import model.Comp;
import model.Compra;
import model.Mensaje;
import model.Producto;
import model.Usuario;

/**
 * Servlet implementation class ControladorServlet
 */
@WebServlet("/ControladorServlet")
@MultipartConfig
public class ControladorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/*
	 * @Resource(name="jms/practica") private ConnectionFactory cf;
	 * 
	 * @Resource(name="jms/queuepractica") private Destination d;
	 */

	ServletContext miServletContex = null;

	String strAutor;
	HttpSession sesion;
	ArrayList<Comp> conf = new ArrayList<Comp>();
	public void init(ServletConfig config) throws ServletException {
		// login = false;
		System.out.println("-------------Levantando listener-----------------");
		Listener listener = new Listener();
		listener.read();

		/*
		 * ServletContext miServletContex = getServletContext();
		 * 
		 * //Cogemos el par�metro de inicializaci�n
		 * miServletContex.setAttribute("autor",
		 * miServletContex.getInitParameter("autor"));
		 */

	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		sesion = req.getSession(true);
		// sesion.setAttribute("sesion_iniciada",false);
		// sesion.setAttribute("usuario",null );
		String path = req.getServletPath();

		if (path.compareTo("/login.html") == 0) {
			req.getRequestDispatcher("login.jsp").forward(req, resp);
		} else if (path.compareTo("/registro.html") == 0) {
			req.getRequestDispatcher("registro.jsp").forward(req, resp);
		} else if (path.compareTo("/index.html") == 0) {

			try {
				Context ctx = new InitialContext();

				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");

				Connection con = ds.getConnection();
				ArrayList<Producto> producto_total = new ArrayList<Producto>();
				if (con != null) {

					Statement st = con.createStatement();
					String script = "SELECT * FROM producto";

					ResultSet rs = st.executeQuery(script);
					while (rs.next()) {
						if (rs.getBoolean(8) == false) {
							Producto _producto = new Producto();
							_producto.setReferencia(rs.getInt(1));
							_producto.setTitulo(rs.getString(2));
							_producto.setDescripcion(rs.getString(3));
							_producto.setCategoria(rs.getString(4));
							Blob bytesImagen = rs.getBlob(5);
							byte[] imgData = bytesImagen.getBytes(1, (int) bytesImagen.length());
							_producto.setImagen(imgData);
							_producto.setPrecio(rs.getFloat(6));
							_producto.setUser(rs.getString(7));
							_producto.setEstado(rs.getBoolean(8));
							producto_total.add(_producto);
						}

					}
					Boolean ini = (Boolean) sesion.getAttribute("sesion_iniciada");

					if (ini == null) {
						sesion.setAttribute("sesion_iniciada", false);
					} else if (ini == true) {
						Usuario user = (Usuario) sesion.getAttribute("usuario");
						/*
						 * ResultSet rs2 = st .executeQuery("SELECT * FROM carro where usuario like  '"
						 * + user.getEmail() + "'");
						 * 
						 * ArrayList<Carro> carrito = new ArrayList<Carro>();
						 * 
						 * while (rs2.next()) { Carro _carro = new Carro();
						 * _carro.setReferencia(rs2.getInt(1)); _carro.setUser(rs2.getString(2));
						 * _carro.setPrecio(rs2.getFloat(3)); _carro.setTitulo(rs2.getString(4)); Blob
						 * bytesImagen = rs2.getBlob(5); byte[] imgData = bytesImagen.getBytes(1, (int)
						 * bytesImagen.length()); _carro.setImagen(imgData); carrito.add(_carro); }
						 */
						sesion.setAttribute("carro", obtener_carro(user.getEmail()));
					}

					sesion.setAttribute("productos", producto_total);
					rs.close();
					st.close();
					con.close();

					req.getRequestDispatcher("index.jsp").forward(req, resp);
				} else {

					req.getRequestDispatcher("error.jsp").forward(req, resp);
				}

			} catch (SQLException e) {

				System.out.println("Error al Insertar " + e.getMessage());
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			} // complete
			catch (NamingException e) {
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/modificar_usuario.html") == 0) {
			req.getRequestDispatcher("modificar_usuario.jsp").forward(req, resp);
		} else if (path.compareTo("/cerrar_sesion.html") == 0) {
			Usuario _usuario = new Usuario();

			sesion.setAttribute("sesion_iniciada", false);
			sesion.setAttribute("usuario", _usuario);
			try {
				Context ctx = new InitialContext();

				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");

				Connection con = ds.getConnection();
				ArrayList<Producto> producto_total = new ArrayList<Producto>();

				if (con != null) {

					Statement st = con.createStatement();
					String script = "SELECT * FROM producto";
					ResultSet rs = st.executeQuery(script);
					while (rs.next()) {
						if (rs.getBoolean(8) == false) {
							Producto _producto = new Producto();
							_producto.setReferencia(rs.getInt(1));
							_producto.setTitulo(rs.getString(2));
							_producto.setDescripcion(rs.getString(3));
							_producto.setCategoria(rs.getString(4));
							Blob bytesImagen = rs.getBlob(5);
							byte[] imgData = bytesImagen.getBytes(1, (int) bytesImagen.length());
							_producto.setImagen(imgData);
							_producto.setPrecio(rs.getFloat(6));
							_producto.setUser(rs.getString(7));
							_producto.setEstado(rs.getBoolean(8));
							producto_total.add(_producto);
						}
					}

					sesion.setAttribute("productos", producto_total);
					rs.close();
					st.close();
					con.close();

					req.getRequestDispatcher("index.jsp").forward(req, resp);
				} else {

					req.getRequestDispatcher("error.jsp").forward(req, resp);
				}

			} catch (SQLException e) {

				System.out.println("Error al Insertar " + e.getMessage());
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			} // complete
			catch (NamingException e) {
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/cuenta.html") == 0) {
			req.getRequestDispatcher("cuenta.jsp").forward(req, resp);
		} else if (path.compareTo("/compras_realizadas.html") == 0) {

			try {

				Usuario user = (Usuario) sesion.getAttribute("usuario");
				EntityManagerFactory emf = Persistence.createEntityManagerFactory("Practica1");

				EntityManager em = emf.createEntityManager();
				em.getTransaction().begin();

				Query q = em.createQuery("SELECT c FROM Compra c WHERE c.comprador like ?1");
				q.setParameter(1, user.getEmail());
				List<Compra> comp = q.getResultList();

				Context ctx = new InitialContext();

				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");
				Connection con = ds.getConnection();

				ArrayList<Producto> compras = new ArrayList<Producto>();
				for (int i = 0; i < comp.size(); i++) {
					Compra c = comp.get(i);
					String[] parts = c.getReferencia().split("-");

					for (int x = 0; x < parts.length; x++) {

						Statement st2 = con.createStatement();

						String script2 = "SELECT * FROM producto where referencia like '" + parts[x] + "'";
						ResultSet rs2 = st2.executeQuery(script2);
						while (rs2.next()) {

							if (rs2.getBoolean(8) == true) {
								Producto _producto = new Producto();
								_producto.setReferencia(Integer.parseInt(parts[x]));
								_producto.setTitulo(rs2.getString(2));
								_producto.setDescripcion(rs2.getString(3));
								_producto.setCategoria(rs2.getString(4));
								Blob bytesImagen = rs2.getBlob(5);
								byte[] imgData = bytesImagen.getBytes(1, (int) bytesImagen.length());
								_producto.setImagen(imgData);
								_producto.setPrecio(rs2.getFloat(6));
								_producto.setUser(rs2.getString(7));
								_producto.setEstado(rs2.getBoolean(8));
								compras.add(_producto);
							}

						}

						rs2.close();
						st2.close();

					}
				}
				con.close();
				sesion.setAttribute("carro", obtener_carro(user.getEmail()));
				req.setAttribute("compras", compras);

				req.getRequestDispatcher("compras_realizadas.jsp").forward(req, resp);

			} catch (SQLException e) {

				System.out.println("Error al Insertar " + e.getMessage());
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			} // complete catch
			catch (NamingException e) {
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/add_producto.html") == 0) {
			req.getRequestDispatcher("registrar_producto.jsp").forward(req, resp);
		} else if (path.compareTo("/cuenta-productos.html") == 0) {
			try {
				Context ctx = new InitialContext();

				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");

				Connection con = ds.getConnection();
				if (con != null) {

					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery(
							"Select * from producto where vendedor like'" + req.getParameter("referenciaM") + "'");

					ArrayList<Producto> productoList = new ArrayList<Producto>();

					while (rs.next()) {

						Producto _producto = new Producto();
						_producto.setReferencia(rs.getInt(1));
						_producto.setTitulo(rs.getString(2));
						_producto.setDescripcion(rs.getString(3));
						_producto.setCategoria(rs.getString(4));
						Blob bytesImagen = rs.getBlob(5);
						byte[] imgData = bytesImagen.getBytes(1, (int) bytesImagen.length());
						_producto.setImagen(imgData);
						_producto.setPrecio(rs.getFloat(6));
						_producto.setUser(rs.getString(7));
						_producto.setEstado(rs.getBoolean(8));
						productoList.add(_producto);
					}

					req.setAttribute("producto", productoList);
					rs.close();
					st.close();
					con.close();

					req.getRequestDispatcher("cuenta-productos.jsp").forward(req, resp);
				} else {

					req.getRequestDispatcher("registrar_producto-incorrecto.jsp").forward(req, resp);
				}

			} catch (SQLException e) {

				System.out.println("Error al Insertar " + e.getMessage());
				req.getRequestDispatcher("registrar_producto-incorrecto.jsp").forward(req, resp);
			} // complete
			catch (NamingException e) {
				req.getRequestDispatcher("registrar_producto-incorrecto.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/busqueda-avanzada.html") == 0) {
			req.getRequestDispatcher("busqueda-avanzada.jsp").forward(req, resp);
		} else if (path.compareTo("/mensajes.html") == 0) {

			try {
				InitialContext ic = new InitialContext();
				ConnectionFactory cf = (ConnectionFactory) ic.lookup("jms/practica");
				Destination d = (Destination) ic.lookup("jms/queuepractica");
				ReadJMS readJMS = new ReadJMS(cf, d);
				List<Mensaje> contenidos = readJMS.read();

				req.setAttribute("mensaje", contenidos);

			} catch (NamingException e) {
				e.printStackTrace();
			}

			req.getRequestDispatcher("mensajes.jsp").forward(req, resp);
		} else if (path.compareTo("/conf_compras.html") == 0) {
			try {
				InitialContext ic = new InitialContext();
				ConnectionFactory cf = (ConnectionFactory) ic.lookup("jms/practica");
				Destination d = (Destination) ic.lookup("jms/queueConfpractica");
				ReadConfJMS rc = new ReadConfJMS(cf, d);
				List<Comp> contenidos = rc.read();
				for (int i = 0; i < contenidos.size(); i++) {
					Comp c = contenidos.get(i);
					conf.add(c);
				}
				req.setAttribute("confirmaciones", conf);

				req.setAttribute("receptor", req.getParameter("referenciaE"));

			} catch (NamingException e) {
				e.printStackTrace();
			}
			req.getRequestDispatcher("confirmaciones.jsp").forward(req, resp);
		}

	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		sesion = req.getSession();
		String path = req.getServletPath();

		if (path.compareTo("/analizar-login.html") == 0) {
			try {
				Context ctx = new InitialContext();

				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");

				Connection con = ds.getConnection();
				if (con != null) {

					Statement st = con.createStatement();

					ResultSet rs = st.executeQuery("Select * from usuario");

					int usuario_existe = 0;
					String user = "";
					Usuario _usuario = new Usuario();
					while (rs.next()) {

						if ((rs.getString(1).compareTo(req.getParameter("email")) == 0
								&& rs.getString(5).compareTo(req.getParameter("contrasenia")) == 0)) {
							usuario_existe = 1;
							_usuario.setEmail(rs.getString(1));
							user = rs.getString(1);
							_usuario.setNombre(rs.getString(2));
							_usuario.setApellido(rs.getString(3));
							_usuario.setDireccion(rs.getString(4));
							_usuario.setContrasenia(rs.getString(5));
							_usuario.setRol(rs.getString(6));
						}

					}
					rs.close();
					st.close();

					if (usuario_existe == 1) {

						sesion.setAttribute("sesion_iniciada", true);
						sesion.setAttribute("usuario", _usuario);
						st = con.createStatement();

						ResultSet rs2 = st.executeQuery("SELECT * FROM carro where usuario like  '" + user + "'");

						ArrayList<Carro> carrito = new ArrayList<Carro>();

						while (rs2.next()) {
							Carro _carro = new Carro();
							_carro.setReferencia(rs2.getInt(1));
							_carro.setUser(rs2.getString(2));
							_carro.setPrecio(rs2.getFloat(3));
							_carro.setTitulo(rs2.getString(4));
							Blob bytesImagen = rs2.getBlob(5);
							byte[] imgData = bytesImagen.getBytes(1, (int) bytesImagen.length());
							_carro.setImagen(imgData);
							carrito.add(_carro);
						}
						sesion.setAttribute("carro", carrito);

						rs2.close();
						st.close();
						con.close();

						req.getRequestDispatcher("index.jsp").forward(req, resp);
					} else {
						req.getRequestDispatcher("login-incorrecto.jsp").forward(req, resp);
					}

				} else {

					req.getRequestDispatcher("login-incorrecto.jsp").forward(req, resp);
				}

			} catch (SQLException e) {

				System.out.println("Error al Obtener " + e.getMessage());
				req.getRequestDispatcher("log-incorrecto.jsp").forward(req, resp);
			} // complete
			catch (NamingException e) {

				req.getRequestDispatcher("login-incorrecto.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/analizar-registro.html") == 0) {

			try {
				Context ctx = new InitialContext();

				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");

				Connection con = ds.getConnection();
				if (con != null) {

					Statement st = con.createStatement();
					if ((req.getParameter("rol").compareTo("Admin") == 0
							&& req.getParameter("contraAdmin").compareTo("2020") == 0)
							|| (req.getParameter("rol").compareTo("Admin") != 0)) {
						String script = "insert into usuario (email,nombre,apellido,direccion,contrasenia,rol) values (?,?,?,?,?,?)";
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

						if (correcto == 1)
							req.getRequestDispatcher("registro-correcto.jsp").forward(req, resp);
						else {
							req.getRequestDispatcher("registro-incorrecto.jsp").forward(req, resp);
						}
					} else {
						req.getRequestDispatcher("registro-incorrecto.jsp").forward(req, resp);
					}

				} else {

					req.getRequestDispatcher("registro-incorrecto.jsp").forward(req, resp);
				}

			} catch (SQLException e) {

				System.out.println("Error al Insertar " + e.getMessage());
				req.getRequestDispatcher("registro-incorrecto.jsp").forward(req, resp);
			} // complete
			catch (NamingException e) {
				req.getRequestDispatcher("registro-incorrecto.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/modificar_usuario-correcto.html") == 0) {
			try {
				Context ctx = new InitialContext();

				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");

				Connection con = ds.getConnection();
				int update = 0;
				if (con != null) {

					Statement st = con.createStatement();
					Usuario user = (Usuario) sesion.getAttribute("usuario");
					if (user.getRol().compareTo("Admin") == 0
							|| user.getEmail().compareTo(req.getParameter("email")) == 0)
						update = 1;
					if (update == 1) {
						String script = "UPDATE usuario SET nombre = ?, apellido=?, direccion=?, contrasenia=? WHERE email = ?";
						PreparedStatement ps = con.prepareStatement(script);

						ps.setString(1, req.getParameter("nombre"));
						ps.setString(2, req.getParameter("apellido"));
						ps.setString(3, req.getParameter("direccion"));
						ps.setString(4, req.getParameter("contrasenia"));
						ps.setString(5, req.getParameter("email"));

						ps.executeUpdate();
						ps.close();
						st.close();

						if (user.getEmail().compareTo(req.getParameter("email")) == 0) {

							st = con.createStatement();

							ResultSet rs = st.executeQuery(
									"SELECT * FROM usuario where email like  '" + req.getParameter("email") + "'");

							Usuario _usuario = new Usuario();

							while (rs.next()) {
								_usuario.setEmail(rs.getString(1));
								_usuario.setNombre(rs.getString(2));
								_usuario.setApellido(rs.getString(3));
								_usuario.setDireccion(rs.getString(4));
								_usuario.setContrasenia(rs.getString(5));
								_usuario.setRol(rs.getString(6));
							}
							sesion.setAttribute("usuario", _usuario);
						}
						con.close();
						req.getRequestDispatcher("modificar_usuario-correcto.jsp").forward(req, resp);
					} else {
						req.getRequestDispatcher("modificar-usuario-incorrecto.jsp").forward(req, resp);
					}

				} else {

					req.getRequestDispatcher("modificar-usuario-incorrecto.jsp").forward(req, resp);
				}

			} catch (SQLException e) {

				System.out.println("Error al Insertar " + e.getMessage());
				req.getRequestDispatcher("modificar-usuario-incorrecto.jsp").forward(req, resp);
			} // complete
			catch (NamingException e) {
				req.getRequestDispatcher("modificar-usuario-incorrecto.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/eliminar-usuario.html") == 0) {
			try {
				Context ctx = new InitialContext();

				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");

				Connection con = ds.getConnection();
				if (con != null) {

					Statement st = con.createStatement();
					Usuario user = (Usuario) sesion.getAttribute("usuario");

					if (user.getEmail().compareTo(req.getParameter("email")) == 0) {
						String script = "DELETE FROM usuario where email like '" + req.getParameter("email") + "'";

						st.executeUpdate(script);
						st.close();

						if (user.getRol().compareTo("Vendedor") == 0) {
							st = con.createStatement();
							script = "DELETE FROM producto where email like '" + req.getParameter("email") + "'";

							st.executeUpdate(script);
							st.close();
						}

						Usuario _usuario = new Usuario();

						_usuario.setRol("");
						_usuario.setEmail("");
						sesion.setAttribute("sesion_iniciada", false);
						sesion.setAttribute("usuario", _usuario);
						con.close();
						req.getRequestDispatcher("eliminar-usuario-correcto.jsp").forward(req, resp);
					} else {
						req.getRequestDispatcher("eliminar-usuario-incorrecto.jsp").forward(req, resp);
					}

				} else {

					req.getRequestDispatcher("eliminar-usuario-incorrecto.jsp").forward(req, resp);
				}

			} catch (SQLException e) {

				System.out.println("Error al Insertar " + e.getMessage());
				req.getRequestDispatcher("eliminar-usuario-incorrecto.jsp").forward(req, resp);
			} // complete
			catch (NamingException e) {
				req.getRequestDispatcher("eliminar-usuario-incorrecto.jsp").forward(req, resp);
			}
		} else if (path.compareTo("/agregar_producto.html") == 0) {

			int referencia = 0;
			try {
				Context ctx = new InitialContext();

				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");

				Connection con = ds.getConnection();
				if (con != null) {

					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery("Select referencia from producto order by referencia");

					while (rs.next()) {
						referencia = rs.getInt(1);
					}

					rs.close();
					st.close();

					st = con.createStatement();
					String script = "insert into producto (referencia,nombre,descripcion,categoria,imagen,precio,vendedor, estado) values (?,?,?,?,?,?,?,?)";
					PreparedStatement ps = con.prepareStatement(script);
					
					ps.setInt(1, referencia + 1);
					ps.setString(2, req.getParameter("nombreProd"));
					ps.setString(3, req.getParameter("descripcionProd"));
					ps.setString(4, req.getParameter("categoriaProd"));
					Part imagen = req.getPart("fotoproducto");
					ps.setBlob(5, imagen.getInputStream());
					ps.setString(6, req.getParameter("precioProd"));
					Usuario usu = (Usuario) sesion.getAttribute("usuario");
					ps.setString(7, usu.getEmail());
					ps.setBoolean(8, false);

					int correcto = ps.executeUpdate();
					ps.close();
					st.close();
					con.close();

					if (correcto == 1)
						req.getRequestDispatcher("registrar_producto-correctamente.jsp").forward(req, resp);
					else {
						req.getRequestDispatcher("registrar_producto-incorrecto.jsp").forward(req, resp);
					}

				} else {

					req.getRequestDispatcher("registrar_producto-incorrecto.jsp").forward(req, resp);
				}

			} catch (SQLException e) {

				System.out.println("Error al Insertar " + e.getMessage());
				req.getRequestDispatcher("registrar_producto-incorrecto.jsp").forward(req, resp);
			} // complete
			catch (NamingException e) {
				req.getRequestDispatcher("registrar_producto-incorrecto.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/eliminar-producto.html") == 0) {
			try {
				Context ctx = new InitialContext();

				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");

				Connection con = ds.getConnection();
				if (con != null) {

					Statement st = con.createStatement();

					String script = "DELETE FROM producto where referencia like '" + req.getParameter("referenciaE")
							+ "'";

					int correcto = st.executeUpdate(script);
					st.close();
					con.close();

					if (correcto == 1) {

						req.getRequestDispatcher("producto-eliminar-correctamente.jsp").forward(req, resp);
					} else {
						req.getRequestDispatcher("producto-eliminar-incorrectamente.jsp").forward(req, resp);
					}

				} else {

					req.getRequestDispatcher("producto-eliminar-incorrectamente.jsp").forward(req, resp);
				}

			} catch (SQLException e) {

				System.out.println("Error al Insertar " + e.getMessage());
				req.getRequestDispatcher("producto-eliminar-incorrectamente.jsp").forward(req, resp);
			} // complete
			catch (NamingException e) {
				req.getRequestDispatcher("producto-eliminar-incorrectamente.jsp").forward(req, resp);
			}
		} else if (path.compareTo("/modificar-producto.html") == 0) {
			try {
				Context ctx = new InitialContext();

				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");

				Connection con = ds.getConnection();

				if (con != null) {

					Statement st = con.createStatement();

					String script = "UPDATE producto SET nombre = ?, descripcion=?, categoria=?, imagen=?, precio=? WHERE referencia = ? and vendedor=?";
					PreparedStatement ps = con.prepareStatement(script);

					ps.setString(1, req.getParameter("nombreProd"));
					ps.setString(2, req.getParameter("descripcionProd"));
					ps.setString(3, req.getParameter("categoriaProd"));
					Part imagen = req.getPart("fotoproducto");
					ps.setBlob(4, imagen.getInputStream());
					ps.setString(5, req.getParameter("precioProd"));

					ps.setInt(6, Integer.parseInt(req.getParameter("referenciaProd")));
					ps.setString(7, req.getParameter("referenciaM"));

					ps.executeUpdate();
					ps.close();
					st.close();
					con.close();

					req.getRequestDispatcher("modificar_producto-correcto.jsp").forward(req, resp);

				} else {

					req.getRequestDispatcher("modificar_producto-incorrecto.jsp").forward(req, resp);
				}

			} catch (SQLException e) {

				System.out.println("Error al Insertar " + e.getMessage());
				req.getRequestDispatcher("modificar_producto-incorrecto.jsp").forward(req, resp);
			} // complete
			catch (NamingException e) {
				req.getRequestDispatcher("modificar_producto-incorrecto.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/producto.html") == 0) {
			try {
				Context ctx = new InitialContext();

				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");

				Connection con = ds.getConnection();
				if (con != null) {

					Statement st = con.createStatement();
					String script = "SELECT * FROM producto where referencia like '" + req.getParameter("referenciaM")
							+ "' ";
					ResultSet rs = st.executeQuery(script);

					ArrayList<Producto> productoList = new ArrayList<Producto>();

					while (rs.next()) {

						Producto _producto = new Producto();
						_producto.setReferencia(rs.getInt(1));
						_producto.setTitulo(rs.getString(2));
						_producto.setDescripcion(rs.getString(3));
						_producto.setCategoria(rs.getString(4));
						Blob bytesImagen = rs.getBlob(5);
						byte[] imgData = bytesImagen.getBytes(1, (int) bytesImagen.length());
						_producto.setImagen(imgData);
						_producto.setPrecio(rs.getFloat(6));
						_producto.setUser(rs.getString(7));
						_producto.setEstado(rs.getBoolean(8));
						productoList.add(_producto);

					}
					sesion.setAttribute("producto_info", productoList);
					rs.close();
					st.close();
					con.close();

					req.getRequestDispatcher("producto.jsp").forward(req, resp);
				} else {

					req.getRequestDispatcher("error.jsp").forward(req, resp);
				}

			} catch (SQLException e) {

				System.out.println("Error al Insertar " + e.getMessage());
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			} // complete
			catch (NamingException e) {
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/producto_index.html") == 0) {
			try {
				Context ctx = new InitialContext();

				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");

				Connection con = ds.getConnection();
				if (con != null) {

					Statement st = con.createStatement();
					String script = "SELECT * FROM producto where referencia like '" + req.getParameter("referenciaM")
							+ "' ";
					ResultSet rs = st.executeQuery(script);

					ArrayList<Producto> productoList = new ArrayList<Producto>();

					while (rs.next()) {
						if (rs.getBoolean(8) == false) {
							Producto _producto = new Producto();
							_producto.setReferencia(rs.getInt(1));
							_producto.setTitulo(rs.getString(2));
							_producto.setDescripcion(rs.getString(3));
							_producto.setCategoria(rs.getString(4));
							Blob bytesImagen = rs.getBlob(5);
							byte[] imgData = bytesImagen.getBytes(1, (int) bytesImagen.length());
							_producto.setImagen(imgData);
							_producto.setPrecio(rs.getFloat(6));
							_producto.setUser(rs.getString(7));
							_producto.setEstado(rs.getBoolean(8));
							productoList.add(_producto);
						}

					}
					sesion.setAttribute("producto_part", productoList);
					rs.close();
					st.close();
					con.close();

					req.getRequestDispatcher("producto-index.jsp").forward(req, resp);
				} else {

					req.getRequestDispatcher("error.jsp").forward(req, resp);
				}

			} catch (SQLException e) {

				System.out.println("Error al Insertar " + e.getMessage());
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			} // complete
			catch (NamingException e) {
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/agregar_carro.html") == 0) {

			try {
				Context ctx = new InitialContext();

				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");

				Connection con = ds.getConnection();
				Producto _producto = new Producto();
				Usuario us = (Usuario) sesion.getAttribute("usuario");
				if (con != null) {

					Statement st1 = con.createStatement();
					ResultSet rs1 = st1.executeQuery(
							"SELECT * FROM carro where referencia like '" + req.getParameter("referenciaE") + "' ");
					int cont = 0;

					if (rs1.next()) {
						cont = 1;
					}
					rs1.close();
					st1.close();
					Statement st = con.createStatement();
					if (cont == 0) {
						ResultSet rs = st.executeQuery("SELECT * FROM producto where referencia like '"
								+ req.getParameter("referenciaE") + "' ");

						while (rs.next()) {
							if (rs.getBoolean(8) == false) {
								_producto.setReferencia(rs.getInt(1));
								_producto.setTitulo(rs.getString(2));
								_producto.setDescripcion(rs.getString(3));
								_producto.setCategoria(rs.getString(4));
								Blob bytesImagen = rs.getBlob(5);
								byte[] imgData = bytesImagen.getBytes(1, (int) bytesImagen.length());
								_producto.setImagen(imgData);
								_producto.setPrecio(rs.getFloat(6));
								_producto.setUser(rs.getString(7));
								_producto.setEstado(rs.getBoolean(8));
							}

						}
						rs.close();

						st.close();

						st = con.createStatement();

						String script = "insert into carro (referencia,usuario,precio,titulo,imagen) values (?,?,?,?,?)";
						PreparedStatement ps = con.prepareStatement(script);

						ps.setInt(1, Integer.parseInt(req.getParameter("referenciaE")));
						ps.setString(2, us.getEmail());
						ps.setFloat(3, _producto.getPrecio());
						ps.setString(4, _producto.getTitulo());
						Blob blob = new SerialBlob(_producto.getImagen());
						ps.setBlob(5, blob);

						ps.executeUpdate();
						ps.close();
						st.close();
					}
					st = con.createStatement();

					ResultSet rs2 = st.executeQuery("SELECT * FROM carro where usuario like  '" + us.getEmail() + "'");

					ArrayList<Carro> carrito = new ArrayList<Carro>();

					while (rs2.next()) {

						Carro _carro = new Carro();
						_carro.setReferencia(rs2.getInt(1));
						_carro.setUser(rs2.getString(2));
						_carro.setPrecio(rs2.getFloat(3));
						_carro.setTitulo(rs2.getString(4));
						Blob bytesImagen = rs2.getBlob(5);
						byte[] imgData = bytesImagen.getBytes(1, (int) bytesImagen.length());
						_carro.setImagen(imgData);
						carrito.add(_carro);
					}

					sesion.setAttribute("carro", carrito);

					rs2.close();
					st.close();
					con.close();

					req.getRequestDispatcher("index.jsp").forward(req, resp);

				} else {

					req.getRequestDispatcher("index.jsp").forward(req, resp);
				}

			} catch (SQLException e) {

				System.out.println("Error al Insertar " + e.getMessage());
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			} // complete
			catch (NamingException e) {
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			}
		} else if (path.compareTo("/eliminar_carro.html") == 0) {

			try {
				Context ctx = new InitialContext();

				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");

				Connection con = ds.getConnection();
				if (con != null) {

					Statement st = con.createStatement();
					Usuario us = (Usuario) sesion.getAttribute("usuario");
					String script = "DELETE FROM carro where referencia like '" + req.getParameter("referenciaC")
							+ "' and usuario like '" + us.getEmail() + "'";

					st.executeUpdate(script);

					st.close();

					st = con.createStatement();

					ResultSet rs2 = st.executeQuery("SELECT * FROM carro where usuario like  '" + us.getEmail() + "'");

					ArrayList<Carro> carrito = new ArrayList<Carro>();

					while (rs2.next()) {
						Carro _carro = new Carro();
						_carro.setReferencia(rs2.getInt(1));
						_carro.setUser(rs2.getString(2));
						_carro.setPrecio(rs2.getFloat(3));
						_carro.setTitulo(rs2.getString(4));
						Blob bytesImagen = rs2.getBlob(5);
						byte[] imgData = bytesImagen.getBytes(1, (int) bytesImagen.length());
						_carro.setImagen(imgData);
						carrito.add(_carro);
					}
					sesion.setAttribute("carro", carrito);

					rs2.close();
					st.close();
					con.close();

					req.getRequestDispatcher("index.jsp").forward(req, resp);

				} else {

					req.getRequestDispatcher("index.jsp").forward(req, resp);
				}

			} catch (SQLException e) {

				System.out.println("Error al Insertar " + e.getMessage());
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			} // complete
			catch (NamingException e) {
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			}
		} else if (path.compareTo("/busqueda.html") == 0) {

			try {
				Context ctx = new InitialContext();

				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");

				Connection con = ds.getConnection();
				ArrayList<Producto> producto_total = new ArrayList<Producto>();
				if (con != null) {

					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery("SELECT * FROM producto where nombre like '%"
							+ req.getParameter("name") + "%' or descripcion like '%" + req.getParameter("name") + "%'");

					while (rs.next()) {
						if (rs.getBoolean(8) == false) {
							Producto _producto = new Producto();
							_producto.setReferencia(rs.getInt(1));
							_producto.setTitulo(rs.getString(2));
							_producto.setDescripcion(rs.getString(3));
							_producto.setCategoria(rs.getString(4));
							Blob bytesImagen = rs.getBlob(5);
							byte[] imgData = bytesImagen.getBytes(1, (int) bytesImagen.length());
							_producto.setImagen(imgData);
							_producto.setPrecio(rs.getFloat(6));
							_producto.setUser(rs.getString(7));
							_producto.setEstado(rs.getBoolean(8));
							producto_total.add(_producto);
						}

					}

					sesion.setAttribute("productos-busqueda", producto_total);

					rs.close();
					st.close();
					con.close();

					req.getRequestDispatcher("res-busqueda.jsp").forward(req, resp);

				} else {

					req.getRequestDispatcher("index.jsp").forward(req, resp);
				}

			} catch (SQLException e) {

				System.out.println("Error al Insertar " + e.getMessage());
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			} // complete
			catch (NamingException e) {
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			}
		} else if (path.compareTo("/buscar_producto.html") == 0) {

			try {
				Context ctx = new InitialContext();

				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");

				Connection con = ds.getConnection();
				ArrayList<Producto> producto_total = new ArrayList<Producto>();
				if (con != null) {

					Statement st = con.createStatement();
					String name = "";
					String descripcion = "";
					String categoria = "";
					String vendedor = "";
					String precio = "";
					int cont = 0;
					if (req.getParameter("nombreProd").compareTo("") != 0) {
						name = "nombre like '%" + req.getParameter("nombreProd") + "%'";
						cont++;
					}
					if (req.getParameter("descripcionProd").compareTo("") != 0) {

						if (cont > 0)
							descripcion = " and descripcion like '%" + req.getParameter("descripcionProd") + "%'";
						else
							descripcion = " descripcion like '%" + req.getParameter("descripcionProd") + "%'";
						cont++;

					}
					if (req.getParameter("precioProd").compareTo("") != 0) {
						if (cont > 0)
							precio = " and precio like '%" + req.getParameter("precioProd") + "%'";
						else
							precio = " precio like '%" + req.getParameter("precioProd") + "%'";
						cont++;
					}
					if (req.getParameter("vendedorProd").compareTo("") != 0) {
						if (cont > 0)
							vendedor = " and vendedor like '%" + req.getParameter("vendedorProd") + "%'";
						else
							vendedor = " vendedor like '%" + req.getParameter("vendedorProd") + "%'";
						cont++;
					}
					if (req.getParameter("categoriaProd").compareTo("") != 0) {
						if (cont > 0)
							categoria = " and categoria like '%" + req.getParameter("categoriaProd") + "%'";
						else
							categoria = " categoria like '%" + req.getParameter("categoriaProd") + "%'";
						cont++;
					}

					ResultSet rs = st.executeQuery("SELECT * FROM producto where " + name + "" + descripcion + ""
							+ precio + "" + vendedor + "" + categoria);

					while (rs.next()) {
						if (rs.getBoolean(8) == false) {
							Producto _producto = new Producto();
							_producto.setReferencia(rs.getInt(1));
							_producto.setTitulo(rs.getString(2));
							_producto.setDescripcion(rs.getString(3));
							_producto.setCategoria(rs.getString(4));
							Blob bytesImagen = rs.getBlob(5);
							byte[] imgData = bytesImagen.getBytes(1, (int) bytesImagen.length());
							_producto.setImagen(imgData);
							_producto.setPrecio(rs.getFloat(6));
							_producto.setUser(rs.getString(7));
							_producto.setEstado(rs.getBoolean(8));
							producto_total.add(_producto);
						}

					}

					sesion.setAttribute("productos-busqueda", producto_total);

					rs.close();
					st.close();
					con.close();

					req.getRequestDispatcher("res-busqueda.jsp").forward(req, resp);

				} else {

					req.getRequestDispatcher("index.jsp").forward(req, resp);
				}

			} catch (SQLException e) {

				System.out.println("Error al Insertar " + e.getMessage());
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			} // complete
			catch (NamingException e) {
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			}
		} else if (path.compareTo("/usuarios_admin.html") == 0) {
			try {
				Context ctx = new InitialContext();
				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");
				Connection con = ds.getConnection();
				if (con != null) {

					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery("Select * from usuario");
					ArrayList<Usuario> us = new ArrayList<Usuario>();
					Usuario user = (Usuario) sesion.getAttribute("usuario");
					while (rs.next()) {
						if (rs.getString(1).compareTo(user.getEmail()) != 0) {
							Usuario _usuario = new Usuario();
							_usuario.setEmail(rs.getString(1));
							_usuario.setNombre(rs.getString(2));
							_usuario.setApellido(rs.getString(3));
							_usuario.setDireccion(rs.getString(4));
							_usuario.setContrasenia(rs.getString(5));
							_usuario.setRol(rs.getString(6));
							us.add(_usuario);
						}
					}
					sesion.setAttribute("usuario-admin", us);
					rs.close();
					st.close();
					con.close();

					req.getRequestDispatcher("usuarios_admin.jsp").forward(req, resp);

				} else {

					req.getRequestDispatcher("login-incorrecto.jsp").forward(req, resp);
				}

			} catch (SQLException e) {

				System.out.println("Error al Obtener " + e.getMessage());
				req.getRequestDispatcher("log-incorrecto.jsp").forward(req, resp);
			} // complete
			catch (NamingException e) {

				req.getRequestDispatcher("login-incorrecto.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/productos_admin.html") == 0) {

			try {
				Context ctx = new InitialContext();

				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");

				Connection con = ds.getConnection();
				if (con != null) {

					Statement st = con.createStatement();
					String script = "SELECT * FROM producto";

					ResultSet rs = st.executeQuery(script);

					ArrayList<Producto> productoList = new ArrayList<Producto>();

					while (rs.next()) {

						Producto _producto = new Producto();
						_producto.setReferencia(rs.getInt(1));
						_producto.setTitulo(rs.getString(2));
						_producto.setDescripcion(rs.getString(3));
						_producto.setCategoria(rs.getString(4));
						Blob bytesImagen = rs.getBlob(5);
						byte[] imgData = bytesImagen.getBytes(1, (int) bytesImagen.length());
						_producto.setImagen(imgData);
						_producto.setPrecio(rs.getFloat(6));
						_producto.setUser(rs.getString(7));
						_producto.setEstado(rs.getBoolean(8));
						productoList.add(_producto);

					}
					req.setAttribute("producto", productoList);
					rs.close();
					st.close();
					con.close();
					
					req.getRequestDispatcher("cuenta-productos.jsp").forward(req, resp);
				} else {

					req.getRequestDispatcher("error.jsp").forward(req, resp);
				}

			} catch (SQLException e) {

				System.out.println("Error al Insertar " + e.getMessage());
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			} // complete
			catch (NamingException e) {
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/eliminar-usuario-admin.html") == 0)

		{
			try {
				Context ctx = new InitialContext();
				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");

				Connection con = ds.getConnection();
				if (con != null) {
					Statement st = con.createStatement();

					String script = "DELETE FROM usuario where email like '" + req.getParameter("referenciaM") + "'";

					st.executeUpdate(script);
					st.close();

					if (req.getParameter("rol").compareTo("Vendedor") == 0) {
						st = con.createStatement();
						script = "DELETE FROM producto where vendedor like '" + req.getParameter("email") + "'";
						st.executeUpdate(script);
						st.close();
					}
					con.close();
					req.getRequestDispatcher("eliminar-usuario-correcto.jsp").forward(req, resp);
				} else {
					req.getRequestDispatcher("error.jsp").forward(req, resp);
				}

			} catch (SQLException e) {

				System.out.println("Error al Insertar " + e.getMessage());
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			} // complete
			catch (NamingException e) {
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}
		} else if (path.compareTo("/chat.html") == 0) {
			req.setAttribute("destino", req.getParameter("referenciaE"));

			req.getRequestDispatcher("chat.jsp").forward(req, resp);

		} else if (path.compareTo("/enviar_mensaje.html") == 0)

		{

			String destino = req.getParameter("referenciaE");

			String mensaje = req.getParameter("mensaje");
			Usuario user = (Usuario) sesion.getAttribute("usuario");
			Mensaje mensajeobj = new Mensaje(user.getEmail(), destino, mensaje);
			SendJMS sendJMS = new SendJMS();

			sendJMS.Send(mensajeobj);

			try {
				InitialContext ic = new InitialContext();
				ConnectionFactory cf = (ConnectionFactory) ic.lookup("jms/practica");
				Destination d = (Destination) ic.lookup("jms/queuepractica");
				ReadJMS readJMS = new ReadJMS(cf, d);
				List<Mensaje> contenidos = readJMS.read();

				req.setAttribute("mensaje", contenidos);
				req.setAttribute("receptor", destino);

			} catch (NamingException e) {
				e.printStackTrace();
			}

			req.getRequestDispatcher("chat_unico.jsp").forward(req, resp);

			// req.getRequestDispatcher("mensaje_enviado.jsp").forward(req, resp);

		} else if (path.compareTo("/pagar.html") == 0) {
			try {
				Context ctx = new InitialContext();

				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");

				Connection con = ds.getConnection();
				if (con != null) {
					Usuario user = (Usuario) sesion.getAttribute("usuario");
					Statement st = con.createStatement();
					String script = "SELECT * FROM carro where usuario like  '" + user.getEmail() + "'";

					ResultSet rs = st.executeQuery(script);

					ArrayList<Producto> productoList = new ArrayList<Producto>();

					while (rs.next()) {
						Statement st2 = con.createStatement();
						String script2 = "SELECT * FROM producto where referencia like  '" + rs.getInt(1) + "'";
						ResultSet rs2 = st2.executeQuery(script2);
						while (rs2.next()) {
							if (rs2.getBoolean(8) == false) {
								Producto _producto = new Producto();
								_producto.setReferencia(rs2.getInt(1));
								_producto.setTitulo(rs2.getString(2));
								_producto.setDescripcion(rs2.getString(3));
								_producto.setCategoria(rs2.getString(4));
								Blob bytesImagen = rs2.getBlob(5);
								byte[] imgData = bytesImagen.getBytes(1, (int) bytesImagen.length());
								_producto.setImagen(imgData);
								_producto.setPrecio(rs2.getFloat(6));
								_producto.setUser(rs2.getString(7));
								_producto.setEstado(rs2.getBoolean(8));
								productoList.add(_producto);
							}

						}
						rs2.close();
						st2.close();
					}
					req.setAttribute("producto", productoList);
					rs.close();
					st.close();
					con.close();

					req.getRequestDispatcher("compra.jsp").forward(req, resp);
				} else {

					req.getRequestDispatcher("error.jsp").forward(req, resp);
				}

			} catch (SQLException e) {

				System.out.println("Error al Insertar " + e.getMessage());
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			} // complete
			catch (NamingException e) {
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/pago.html") == 0) {
			ArrayList<String> usuarios = new ArrayList<String>();
			ArrayList<Producto> productoList = new ArrayList<Producto>();
			Usuario user = (Usuario) sesion.getAttribute("usuario");
			try {
				Context ctx = new InitialContext();

				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");

				Connection con = ds.getConnection();
				if (con != null) {

					Statement st = con.createStatement();
					String script = "SELECT * FROM carro where usuario like  '" + user.getEmail() + "'";

					ResultSet rs = st.executeQuery(script);

					while (rs.next()) {
						Statement st2 = con.createStatement();
						String script2 = "SELECT * FROM producto where referencia like  '" + rs.getInt(1) + "'";
						ResultSet rs2 = st2.executeQuery(script2);
						while (rs2.next()) {
							if (rs2.getBoolean(8) == false) {
								Producto _producto = new Producto();
								_producto.setReferencia(rs2.getInt(1));
								_producto.setTitulo(rs2.getString(2));
								_producto.setDescripcion(rs2.getString(3));
								_producto.setCategoria(rs2.getString(4));
								Blob bytesImagen = rs2.getBlob(5);
								byte[] imgData = bytesImagen.getBytes(1, (int) bytesImagen.length());
								_producto.setImagen(imgData);
								_producto.setPrecio(rs2.getFloat(6));
								_producto.setUser(rs2.getString(7));
								if (usuarios.contains(rs2.getString(7)) == false) {
									usuarios.add(rs2.getString(7));
								}
								_producto.setEstado(rs2.getBoolean(8));
								productoList.add(_producto);
							}

						}
						rs2.close();
						st2.close();
					}
					req.setAttribute("producto", productoList);
					rs.close();

					for (int i = 0; i < usuarios.size(); i++) {
						String referencia = "";
						String vendedor = "";
						String comprador = "";
						Float total = (float) 0;
						String direccion = "";
						String tarjeta = "";
						String fecha = "";
						String vend = usuarios.get(i);
						for (int x = 0; x < productoList.size(); x++) {
							Producto p = productoList.get(x);
							if (vend.compareTo(p.getUser()) == 0) {
								referencia += p.getReferencia() + "-";
								vendedor = p.getUser();
								comprador = user.getEmail();
								total += p.getPrecio();
								tarjeta = req.getParameter("tarjeta");
								direccion = req.getParameter("direccion");
								Date objDate = new Date();
								fecha = objDate.toString();
							}
						}

						Comp comp = new Comp(referencia, vendedor, comprador, total, direccion, tarjeta, fecha);
						PagoJMS sendJMS = new PagoJMS();
						sendJMS.Send(comp);
						con = ds.getConnection();
						st = con.createStatement();

						ResultSet rs2 = st
								.executeQuery("SELECT * FROM carro where usuario like  '" + user.getEmail() + "'");

						ArrayList<Carro> carrito = new ArrayList<Carro>();

						while (rs2.next()) {
							Carro _carro = new Carro();
							_carro.setReferencia(rs2.getInt(1));
							_carro.setUser(rs2.getString(2));
							_carro.setPrecio(rs2.getFloat(3));
							_carro.setTitulo(rs2.getString(4));
							Blob bytesImagen = rs2.getBlob(5);
							byte[] imgData = bytesImagen.getBytes(1, (int) bytesImagen.length());
							_carro.setImagen(imgData);
							carrito.add(_carro);
						}
						ArrayList<Producto> producto_total = new ArrayList<Producto>();

						String script3 = "SELECT * FROM producto";
						ResultSet rs3 = st.executeQuery(script3);
						while (rs3.next()) {
							if (rs3.getBoolean(8) == false) {
								Producto _producto = new Producto();
								_producto.setReferencia(rs3.getInt(1));
								_producto.setTitulo(rs3.getString(2));

								_producto.setDescripcion(rs3.getString(3));
								_producto.setCategoria(rs3.getString(4));
								Blob bytesImagen = rs3.getBlob(5);
								byte[] imgData = bytesImagen.getBytes(1, (int) bytesImagen.length());
								_producto.setImagen(imgData);
								_producto.setPrecio(rs3.getFloat(6));
								_producto.setUser(rs3.getString(7));
								_producto.setEstado(rs3.getBoolean(8));
								producto_total.add(_producto);
							}

						}

						rs3.close();
						st.close();
						con.close();
						sesion.setAttribute("productos", producto_total);
						sesion.setAttribute("carro", carrito);
					}

					req.getRequestDispatcher("pago_correcto.jsp").forward(req, resp);
				} else {

					req.getRequestDispatcher("error.jsp").forward(req, resp);
				}

			} catch (SQLException e) {

				System.out.println("Error al Insertar " + e.getMessage());
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			} // complete
			catch (NamingException e) {
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/abrir_chat.html") == 0) {

			try {
				InitialContext ic = new InitialContext();
				ConnectionFactory cf = (ConnectionFactory) ic.lookup("jms/practica");
				Destination d = (Destination) ic.lookup("jms/queuepractica");
				ReadJMS readJMS = new ReadJMS(cf, d);
				List<Mensaje> contenidos = readJMS.read();

				req.setAttribute("mensaje", contenidos);

				req.setAttribute("receptor", req.getParameter("referenciaE"));

			} catch (NamingException e) {
				e.printStackTrace();
			}

			req.getRequestDispatcher("chat_unico.jsp").forward(req, resp);
		} else if (path.compareTo("/confirmar_compra.html") == 0) {
			String referencia = req.getParameter("referencia");
			String comprador = req.getParameter("comprador");
			String vendedor = req.getParameter("vendedor");
			float precio = Float.parseFloat(req.getParameter("precio"));
			String fecha = req.getParameter("fecha");
			String tarjeta = req.getParameter("tarjeta");
			String direccion = req.getParameter("direccion");
			int index = -1;
			for (int i = 0; i < conf.size(); i++) {
				Comp c = conf.get(i);
				if(c.getReferencia().compareTo(referencia)==0) {
					index = i;
				}
			}
			if(index!=-1) {
				conf.remove(index);
			}
			Comp c = new Comp(referencia, vendedor, comprador, precio, tarjeta, direccion, fecha);
			procesar_compraJDBC(c);
			vaciar_carro(comprador);
			cambiar_estado(referencia);
			req.getRequestDispatcher("pago_aceptado.jsp").forward(req, resp);
		} else if (path.compareTo("/cancelar_compra.html") == 0) {
			String referencia = req.getParameter("referencia");
			String comprador = req.getParameter("comprador");
			int index = -1;
			for (int i = 0; i < conf.size(); i++) {
				Comp c = conf.get(i);
				if(c.getReferencia().compareTo(referencia)==0) {
					index = i;
				}
			}
			if(index!=-1) {
				conf.remove(index);
			}
			vaciar_carro(comprador);
			
			req.getRequestDispatcher("index.jsp").forward(req, resp);
		}

	}

	public void procesar_compraJDBC(Comp compra) {

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

	public ArrayList<Carro> obtener_carro(String user) {
		ArrayList<Carro> carrito = new ArrayList<Carro>();
		try {
			Context ctx = new InitialContext();

			DataSource ds = (DataSource) ctx.lookup("jdbc/practica");

			Connection con = ds.getConnection();

			Statement st = con.createStatement();
			con = ds.getConnection();
			st = con.createStatement();

			ResultSet rs2 = st.executeQuery("SELECT * FROM carro where usuario like  '" + user + "'");

			while (rs2.next()) {
				Carro _carro = new Carro();
				_carro.setReferencia(rs2.getInt(1));
				_carro.setUser(rs2.getString(2));
				_carro.setPrecio(rs2.getFloat(3));
				_carro.setTitulo(rs2.getString(4));
				Blob bytesImagen = rs2.getBlob(5);
				byte[] imgData = bytesImagen.getBytes(1, (int) bytesImagen.length());
				_carro.setImagen(imgData);
				carrito.add(_carro);
			}

		} catch (SQLException e) {

		} // complete
		catch (NamingException e) {

		}
		return carrito;
	}

	public void cambiar_estado(String referencia) {

		try {
			Context ctx = new InitialContext();

			DataSource ds = (DataSource) ctx.lookup("jdbc/practica");

			Connection con = ds.getConnection();

			String[] parts = referencia.split("-");
			for (int i = 0; i < parts.length; i++) {
				Statement st = con.createStatement();

				String script = "UPDATE producto SET estado='" + 1 + "' WHERE referencia ='" + parts[i] + "'";
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
