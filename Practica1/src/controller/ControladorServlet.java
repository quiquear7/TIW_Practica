package controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Hashtable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;


import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;

import model.Carro;
import model.Compra;
import model.Producto;
import model.Usuario;
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
	HttpSession sesion;

	public void init() {

		/*ServletContext miServletContex = getServletContext();

		// Cogemos el par�metro de inicializaci�n
		miServletContex.setAttribute("autor", miServletContex.getInitParameter("autor"));*/
		

	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		
		sesion = req.getSession(true);
		sesion.setAttribute("sesion_iniciada", login);
		String path= req.getServletPath();
		
		
		if(path.compareTo("/login.html")==0) {
			req.getRequestDispatcher("login.jsp").forward(req, resp);
		}
		else if(path.compareTo("/registro.html")==0) {
			req.getRequestDispatcher("registro.jsp").forward(req, resp);
		}
		else if(path.compareTo("/index.html")==0) {
			System.out.println("Index");
			try {
				Context ctx = new InitialContext();
				System.out.println("iniciamos context");
				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");
				System.out.println("ds");
				Connection con = ds.getConnection();
				ArrayList<Producto> producto_total = new ArrayList<Producto>();
				if (con != null) {
					
					System.out.println("CONEXION CORRECTA");
					Statement st = con.createStatement();
					String script= "SELECT * FROM producto";
					ResultSet rs = st.executeQuery(script);
					while(rs.next()) {
						System.out.println("Recorremos");
						Producto _producto= new Producto();
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
					
					sesion.setAttribute("productos", producto_total);
					rs.close();
					st.close();
					con.close();
					System.out.println("Connection close");
					req.getRequestDispatcher("index.jsp").forward(req, resp);
				}
				else {
					System.out.println("CONEXION iNCORRECTA");
					req.getRequestDispatcher("error.jsp").forward(req, resp);
				}
				
			} 
			catch (SQLException e) {
				
				System.out.println("Error al Insertar "+e.getMessage());
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}// complete
			catch (NamingException e) {
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}
			
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
			try {
				Context ctx = new InitialContext();
				System.out.println("iniciamos context");
				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");
				System.out.println("ds");
				Connection con = ds.getConnection();
				ArrayList<Compra> compras = new ArrayList<Compra>();
				if (con != null) {
					
					System.out.println("CONEXION CORRECTA");
					Statement st = con.createStatement();
					Usuario user = (Usuario) sesion.getAttribute("usuario");
					String script= "SELECT * FROM compra where comprador like  '"+user.getEmail()+"'";
					ResultSet rs = st.executeQuery(script);
				
					while(rs.next()) {
						System.out.println("Recorremos");
						Compra _producto= new Compra();
						_producto.setReferencia(rs.getInt(1));
						_producto.setComprador(rs.getString(2));
						_producto.setVendedor(rs.getString(3));
						_producto.setPrecio(rs.getFloat(4));
						_producto.setDireccion(rs.getString(5));
						_producto.setFecha(rs.getString(6));
						_producto.setReferencia_compra(rs.getInt(7)); 
						_producto.setTitulo(rs.getString(8));
						Blob bytesImagen = rs.getBlob(9);
						 byte[] imgData = bytesImagen.getBytes(1, (int) bytesImagen.length());
						_producto.setImagen(imgData);
						compras.add(_producto);
					}
					

					sesion.setAttribute("compras", compras);
					rs.close();
					st.close();
					con.close();
					System.out.println("Connection close");
					req.getRequestDispatcher("compras_realizadas.jsp").forward(req, resp);
				}
				else {
					System.out.println("CONEXION iNCORRECTA");
					req.getRequestDispatcher("error.jsp").forward(req, resp);
				}
				
			} 
			catch (SQLException e) {
				
				System.out.println("Error al Insertar "+e.getMessage());
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}// complete
			catch (NamingException e) {
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}
			
		}
		else if(path.compareTo("/add_producto.html")==0) {
			req.getRequestDispatcher("registrar_producto.jsp").forward(req, resp);
		}
		else if(path.compareTo("/cuenta-productos.html")==0) {
			try {
				Context ctx = new InitialContext();
				System.out.println("iniciamos context");
				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");
				System.out.println("ds");
				Connection con = ds.getConnection();
				if (con != null) {
					
					System.out.println("CONEXION CORRECTA");
					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery("Select * from producto");
				
					Object user = (Object) sesion.getAttribute("usuario");
					Usuario usu = (Usuario) user;
					String us = usu.getEmail();
					ArrayList<Producto> productoList = new ArrayList<Producto>();
					
					while(rs.next()) {
						if(rs.getString(7).compareTo(us)==0) {
							Producto _producto= new Producto();
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
					sesion.setAttribute("producto", productoList);
					rs.close();
					st.close();
					con.close();
					System.out.println("Connection close");
					req.getRequestDispatcher("cuenta-productos.jsp").forward(req, resp);
				}
				else {
					System.out.println("CONEXION iNCORRECTA");
					req.getRequestDispatcher("registrar_producto-incorrecto.jsp").forward(req, resp);
				}
				
			} 
			catch (SQLException e) {
				
				System.out.println("Error al Insertar "+e.getMessage());
				req.getRequestDispatcher("registrar_producto-incorrecto.jsp").forward(req, resp);
			}// complete
			catch (NamingException e) {
				req.getRequestDispatcher("registrar_producto-incorrecto.jsp").forward(req, resp);
			}
			
		}
		else if(path.compareTo("/busqueda-avanzada.html")==0) {
			req.getRequestDispatcher("busqueda-avanzada.jsp").forward(req, resp);
		}
		
		
		
		
		
		
		
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		
		sesion = req.getSession();
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
					String user ="";
					while(rs.next()) {
						System.out.println(rs.getString(1));
						System.out.println(rs.getString(5));
						if((rs.getString(1).compareTo(req.getParameter("email"))== 0  && rs.getString(5).compareTo(req.getParameter("contrasenia"))==0 )) {
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
					
					System.out.println("Connection close");
					System.out.println("User"+usuario_existe);
					if(usuario_existe==1) {
						login = true;
						sesion.setAttribute("sesion_iniciada", login);
						sesion.setAttribute("usuario", _usuario);
						st = con.createStatement();
						
						ResultSet rs2 = st.executeQuery("SELECT * FROM carro where usuario like  '"+user+"'");
						
						Carro _carro = new Carro();
						ArrayList<Carro> carrito = new ArrayList<Carro>();
						
						while(rs2.next()) {
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
						
						for (int x = 0; x < carrito.size(); x++) {
							Carro car = carrito.get(x);
							System.out.println(car.getPrecio());
						}
						System.out.println("Connection close");
						
						
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
					if((req.getParameter("rol").compareTo("Admin")==0 &&req.getParameter("contraAdmin").compareTo("2020")==0)||(req.getParameter("rol").compareTo("Admin")!=0 )){
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
					Usuario user = (Usuario) sesion.getAttribute("usuario");
					if(user.getEmail().compareTo(req.getParameter("email"))==0) {
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
							req.getRequestDispatcher("eliminar-usuario-correcto.jsp").forward(req, resp);
						}
						else {
							req.getRequestDispatcher("eliminar-usuario-incorrecto.jsp").forward(req, resp);
						}
						if(user.getRol().compareTo("Vendedor")==0) {
							st = con.createStatement();
							script= "DELETE FROM producto where email like '"+req.getParameter("email")+"'";
							
							correcto = st.executeUpdate(script);
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
		else if(path.compareTo("/agregar_producto.html")==0) {
			System.out.println("entramos al path");
			int referencia = 0;
			try {
				Context ctx = new InitialContext();
				System.out.println("iniciamos context");
				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");
				System.out.println("ds");
				Connection con = ds.getConnection();
				if (con != null) {
					System.out.println("CONEXION CORRECTA");
					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery("Select referencia from producto");
				
					while(rs.next()) {
						referencia =  rs.getInt(1);
					}
					
					rs.close();
					st.close();
					
					st = con.createStatement();
					String script= "insert into producto (referencia,nombre,descripcion,categoria,imagen,precio,vendedor, estado) values (?,?,?,?,?,?,?,?)";
					PreparedStatement ps = con.prepareStatement(script);
					ps.setInt(1, referencia +1 );
					ps.setString(2, req.getParameter("nombreProd"));
					ps.setString(3, req.getParameter("descripcionProd"));
					ps.setString(4, req.getParameter("categoriaProd"));
					FileInputStream fis = new FileInputStream(req.getParameter("fotoproducto"));
					ps.setBlob(5,fis);
					ps.setString(6, req.getParameter("precioProd"));
					Usuario usu = (Usuario) sesion.getAttribute("usuario");
					ps.setString(7, usu.getEmail());
					ps.setBoolean(8, false);

					int correcto = ps.executeUpdate();
					ps.close();
					st.close();
					con.close();
					System.out.println("Connection close");
					if(correcto==1) req.getRequestDispatcher("registrar_producto-correctamente.jsp").forward(req, resp);
					else {
						req.getRequestDispatcher("registrar_podructo-incorrecto.jsp").forward(req, resp);
					}
					
				}
				else {
					System.out.println("CONEXION iNCORRECTA");
					req.getRequestDispatcher("registrar_podructo-incorrecto.jsp").forward(req, resp);
				}
				
			} 
			catch (SQLException e) {
				
				System.out.println("Error al Insertar "+e.getMessage());
				req.getRequestDispatcher("registrar_podructo-incorrecto.jsp").forward(req, resp);
			}// complete
			catch (NamingException e) {
				req.getRequestDispatcher("registrar_podructo-incorrecto.jsp").forward(req, resp);
			}
			
	
		}
		else if(path.compareTo("/eliminar-producto.html")==0) {
			try {
				Context ctx = new InitialContext();
				System.out.println("iniciamos context");
				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");
				System.out.println("ds");
				Connection con = ds.getConnection();
				if (con != null) {
					System.out.println("CONEXION CORRECTA");
					Statement st = con.createStatement();
					Usuario user = (Usuario) sesion.getAttribute("usuario");
					String script= "DELETE FROM producto where referencia like '"+req.getParameter("referenciaE")+"' and vendedor like '"+user.getEmail()+"'";
					
					int correcto = st.executeUpdate(script);
					st.close();
					con.close();
					System.out.println("Connection close");
					if(correcto==1) {

						req.getRequestDispatcher("producto-eliminar-correctamente.jsp").forward(req, resp);
					}
					else {
						req.getRequestDispatcher("producto-eliminar-incorrectamente.jsp").forward(req, resp);
					}
					
				}
				else {
					System.out.println("CONEXION INCORRECTA");
					req.getRequestDispatcher("producto-eliminar-incorrectamente.jsp").forward(req, resp);
				}
				
			} 
			catch (SQLException e) {
				
				System.out.println("Error al Insertar "+e.getMessage());
				req.getRequestDispatcher("producto-eliminar-incorrectamente.jsp").forward(req, resp);
			}// complete
			catch (NamingException e) {
				req.getRequestDispatcher("producto-eliminar-incorrectamente.jsp").forward(req, resp);
			}
		}
		else if(path.compareTo("/modificar-producto.html")==0) {
			try {
				Context ctx = new InitialContext();
				System.out.println("iniciamos context");
				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");
				System.out.println("ds");
				Connection con = ds.getConnection();
				
				
				if (con != null) {
					System.out.println("CONEXION CORRECTA");
					Statement st = con.createStatement();
					
					String script= "UPDATE producto SET nombre = ?, descripcion=?, categoria=?, imagen=?, precio=? WHERE referencia = ? and vendedor=?";
					PreparedStatement ps = con.prepareStatement(script);
					
					
					ps.setString(1, req.getParameter("nombreProd"));
					ps.setString(2, req.getParameter("descripcionProd"));
					ps.setString(3, req.getParameter("categoriaProd"));
					FileInputStream fis = new FileInputStream(req.getParameter("fotoproducto"));
					ps.setBlob(4,fis);
					ps.setString(5, req.getParameter("precioProd"));
					Usuario user = (Usuario) sesion.getAttribute("usuario");
					ps.setInt(6,Integer.parseInt( req.getParameter("referenciaProd")));
					ps.setString(7,user.getEmail());
					

					int correcto = ps.executeUpdate();
					ps.close();
					st.close();
					con.close();
					System.out.println("Connection close");
					if(correcto==1) {
						req.getRequestDispatcher("modificar_producto-correcto.jsp").forward(req, resp);
					}
					else {
						req.getRequestDispatcher("modificar_producto-incorrecto.jsp").forward(req, resp);
					}
					
				}
				else {
					System.out.println("CONEXION INCORRECTA");
					req.getRequestDispatcher("modificar_producto-incorrecto.jsp").forward(req, resp);
				}
				
			} 
			catch (SQLException e) {
				
				System.out.println("Error al Insertar "+e.getMessage());
				req.getRequestDispatcher("modificar_producto-incorrecto.jsp").forward(req, resp);
			}// complete
			catch (NamingException e) {
				req.getRequestDispatcher("modificar_producto-incorrecto.jsp").forward(req, resp);
			}
			
		}
		else if(path.compareTo("/producto.html")==0) {
			try {
				Context ctx = new InitialContext();
				System.out.println("iniciamos context");
				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");
				System.out.println("ds");
				Connection con = ds.getConnection();
				if (con != null) {
					
					System.out.println("CONEXION CORRECTA");
					Statement st = con.createStatement();
					String script= "SELECT * FROM producto where referencia like '"+req.getParameter("referenciaM")+"' ";
					ResultSet rs = st.executeQuery(script);
				
					Usuario usu = (Usuario) sesion.getAttribute("usuario");
					String us = usu.getEmail();
					ArrayList<Producto> productoList = new ArrayList<Producto>();
					
					while(rs.next()) {
						if(rs.getString(7).compareTo(us)==0) {
							Producto _producto= new Producto();
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
					sesion.setAttribute("producto_info", productoList);
					rs.close();
					st.close();
					con.close();
					System.out.println("Connection close");
					req.getRequestDispatcher("producto.jsp").forward(req, resp);
				}
				else {
					System.out.println("CONEXION iNCORRECTA");
					req.getRequestDispatcher("error.jsp").forward(req, resp);
				}
				
			} 
			catch (SQLException e) {
				
				System.out.println("Error al Insertar "+e.getMessage());
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}// complete
			catch (NamingException e) {
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}
			
		}
		else if(path.compareTo("/producto_index.html")==0) {
			try {
				Context ctx = new InitialContext();
				System.out.println("iniciamos context");
				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");
				System.out.println("ds");
				Connection con = ds.getConnection();
				if (con != null) {
					
					System.out.println("CONEXION CORRECTA");
					Statement st = con.createStatement();
					String script= "SELECT * FROM producto where referencia like '"+req.getParameter("referenciaM")+"' ";
					ResultSet rs = st.executeQuery(script);
				
					
					ArrayList<Producto> productoList = new ArrayList<Producto>();
					
					while(rs.next()) {
						Producto _producto= new Producto();
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
					sesion.setAttribute("producto_part", productoList);
					rs.close();
					st.close();
					con.close();
					System.out.println("Connection close");
					req.getRequestDispatcher("producto-index.jsp").forward(req, resp);
				}
				else {
					System.out.println("CONEXION iNCORRECTA");
					req.getRequestDispatcher("error.jsp").forward(req, resp);
				}
				
			} 
			catch (SQLException e) {
				
				System.out.println("Error al Insertar "+e.getMessage());
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}// complete
			catch (NamingException e) {
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}
			
		}
		else if(path.compareTo("/agregar_carro.html")==0) {

			try {
				Context ctx = new InitialContext();
				
				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");
				
				Connection con = ds.getConnection();
				Producto _producto= new Producto();
				if (con != null) {
					System.out.println("CONEXION CORRECTA");
					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery("SELECT * FROM producto where referencia like '"+req.getParameter("referenciaE")+"' ");
					
					
					while(rs.next()) {
						
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
					
					rs.close();	
					st.close();
					
					st = con.createStatement();
					Usuario us = (Usuario) sesion.getAttribute("usuario");
					
					String script= "insert into carro (referencia,usuario,precio,titulo,imagen) values (?,?,?,?,?)";
					PreparedStatement ps = con.prepareStatement(script);
					
					ps.setInt(1,Integer.parseInt(req.getParameter("referenciaE")) );
					ps.setString(2,us.getEmail());
					ps.setFloat(3, _producto.getPrecio());
					ps.setString(4, _producto.getTitulo());
					Blob blob = new SerialBlob(_producto.getImagen());
					ps.setBlob(5,blob);


					
					ps.executeUpdate();
					ps.close();
					st.close();
					
					st = con.createStatement();
					System.out.println("Usuario: "+us.getEmail());
					ResultSet rs2 = st.executeQuery("SELECT * FROM carro where usuario like  '"+us.getEmail()+"'");

					ArrayList<Carro> carrito = new ArrayList<Carro>();
					
					while(rs2.next()) {
						
						System.out.println("titulo:"+rs2.getString(4));
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
					
					for (int x = 0; x < carrito.size(); x++) {
						Carro car = carrito.get(x);
						System.out.println(car.getPrecio());
					}
					System.out.println("Connection close");
					req.getRequestDispatcher("index.jsp").forward(req, resp);
					
				}
				else {
					System.out.println("CONEXION iNCORRECTA");
					req.getRequestDispatcher("index.jsp").forward(req, resp);
				}
				
			} 
			catch (SQLException e) {
				
				System.out.println("Error al Insertar "+e.getMessage());
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			}// complete
			catch (NamingException e) {
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			}
		}
		else if(path.compareTo("/eliminar_carro.html")==0) {

			try {
				Context ctx = new InitialContext();
				System.out.println("iniciamos context");
				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");
				System.out.println("ds");
				Connection con = ds.getConnection();
				if (con != null) {
					System.out.println("CONEXION CORRECTA");
					Statement st = con.createStatement();
					Usuario us = (Usuario) sesion.getAttribute("usuario");
					String script= "DELETE FROM carro where referencia like '"+req.getParameter("referenciaC")+"' and usuario like '"+us.getEmail()+"'";
					
					st.executeUpdate(script);
					
					
					st.close();
					
					st = con.createStatement();
					System.out.println("Usuario: "+us.getEmail());
					ResultSet rs2 = st.executeQuery("SELECT * FROM carro where usuario like  '"+us.getEmail()+"'");
					
					Carro _carro = new Carro();
					ArrayList<Carro> carrito = new ArrayList<Carro>();
					
					while(rs2.next()) {
						
						
						
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
					
					for (int x = 0; x < carrito.size(); x++) {
						Carro car = carrito.get(x);
						System.out.println(car.getPrecio());
					}
					System.out.println("Connection close");
					req.getRequestDispatcher("index.jsp").forward(req, resp);
					
				}
				else {
					System.out.println("CONEXION iNCORRECTA");
					req.getRequestDispatcher("index.jsp").forward(req, resp);
				}
				
			} 
			catch (SQLException e) {
				
				System.out.println("Error al Insertar "+e.getMessage());
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			}// complete
			catch (NamingException e) {
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			}
		}
		else if(path.compareTo("/busqueda.html")==0) {

			try {
				Context ctx = new InitialContext();
				System.out.println("iniciamos context");
				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");
				System.out.println("ds");
				Connection con = ds.getConnection();
				ArrayList<Producto> producto_total = new ArrayList<Producto>();
				if (con != null) {
					System.out.println("CONEXION CORRECTA");
					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery("SELECT * FROM producto where nombre like '%"+req.getParameter("name")+"%' or descripcion like '%"+req.getParameter("name")+"%'");
					
					
					while(rs.next()) {
						Producto _producto= new Producto();
						_producto.setReferencia(rs.getInt(1));
						_producto.setTitulo(rs.getString(2));
						_producto.setDescripcion(rs.getString(3));
						_producto.setCategoria(rs.getString(4));
						//_producto.setImagen(rs.getString(5));
						_producto.setPrecio(rs.getFloat(6));
						_producto.setUser(rs.getString(7));
						_producto.setEstado(rs.getBoolean(8));
						producto_total.add(_producto);
					}
					
					sesion.setAttribute("productos-busqueda", producto_total);
					
					rs.close();	
					st.close();
					con.close();
					
					req.getRequestDispatcher("res-busqueda.jsp").forward(req, resp);
					
				}
				else {
					System.out.println("CONEXION iNCORRECTA");
					req.getRequestDispatcher("index.jsp").forward(req, resp);
				}
				
			} 
			catch (SQLException e) {
				
				System.out.println("Error al Insertar "+e.getMessage());
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			}// complete
			catch (NamingException e) {
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			}
		}
		else if(path.compareTo("/buscar_producto.html")==0) {

			try {
				Context ctx = new InitialContext();
				System.out.println("iniciamos context");
				DataSource ds = (DataSource) ctx.lookup("jdbc/practica");
				System.out.println("ds");
				Connection con = ds.getConnection();
				ArrayList<Producto> producto_total = new ArrayList<Producto>();
				if (con != null) {
					System.out.println("CONEXION CORRECTA");
					Statement st = con.createStatement();
					String name ="";
					String descripcion = "";
					String categoria = "";
					String vendedor = "";
					String precio = "";
					int cont = 0;
					if(req.getParameter("nombreProd").compareTo("")!=0) {
						name = "nombre like '%"+req.getParameter("nombreProd")+"%'";
						cont++;
					}
					if(req.getParameter("descripcionProd").compareTo("")!=0) {
						System.out.println(req.getParameter("descripcionProd"));
						if (cont > 0) descripcion = " and descripcion like '%"+req.getParameter("descripcionProd")+"%'";
						else descripcion = " descripcion like '%"+req.getParameter("descripcionProd")+"%'";
						cont++;
						System.out.println(descripcion);
					}
					if(req.getParameter("precioProd").compareTo("")!=0) {
						if (cont > 0) precio = " and precio like '%"+req.getParameter("precioProd")+"%'";
						else precio = " precio like '%"+req.getParameter("precioProd")+"%'";
						cont++;
					}
					if(req.getParameter("vendedorProd").compareTo("")!=0) {
						if (cont > 0) vendedor = " and vendedor like '%"+req.getParameter("vendedorProd")+"%'";
						else vendedor = " vendedor like '%"+req.getParameter("vendedorProd")+"%'";
						cont++;
					}
					if(req.getParameter("categoriaProd").compareTo("")!=0) {
						if (cont > 0) categoria = " and categoria like '%"+req.getParameter("categoriaProd")+"%'";
						else categoria = " categoria like '%"+req.getParameter("categoriaProd")+"%'";
						cont++;
					}
					System.out.println(descripcion);
					System.out.println("SELECT * FROM producto where "+name+""+descripcion+""+precio+""+categoria);
					ResultSet rs = st.executeQuery("SELECT * FROM producto where "+name+""+descripcion+""+precio+""+vendedor+""+categoria);
					
					
					while(rs.next()) {
						Producto _producto= new Producto();
						_producto.setReferencia(rs.getInt(1));
						_producto.setTitulo(rs.getString(2));
						_producto.setDescripcion(rs.getString(3));
						_producto.setCategoria(rs.getString(4));
						//_producto.setImagen(rs.getString(5));
						_producto.setPrecio(rs.getFloat(6));
						_producto.setUser(rs.getString(7));
						_producto.setEstado(rs.getBoolean(8));
						producto_total.add(_producto);
					}
					
					sesion.setAttribute("productos-busqueda", producto_total);
					
					rs.close();	
					st.close();
					con.close();
					
					req.getRequestDispatcher("res-busqueda.jsp").forward(req, resp);
					
				}
				else {
					System.out.println("CONEXION iNCORRECTA");
					req.getRequestDispatcher("index.jsp").forward(req, resp);
				}
				
			} 
			catch (SQLException e) {
				
				System.out.println("Error al Insertar "+e.getMessage());
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			}// complete
			catch (NamingException e) {
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			}
		}
		
		
		
		
		
	}

}
