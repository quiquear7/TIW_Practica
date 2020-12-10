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
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Carro;
import model.Comp;
import model.Compra;
import model.Mensaje;
import model.Producto;
import model.Respuesta;
import model.Usuario;
import sun.misc.IOUtils;


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
		String path = req.getServletPath();

		if (path.compareTo("/login.html") == 0) {
			req.getRequestDispatcher("login.jsp").forward(req, resp);
		} else if (path.compareTo("/registro.html") == 0) {
			req.getRequestDispatcher("registro.jsp").forward(req, resp);
		} else if (path.compareTo("/index.html") == 0) {
			
			Respuesta res = productoTotal();

			if (res.getCop() == 200) {
				
				sesion.setAttribute("productos", res.getLista());
				Boolean ini = (Boolean) sesion.getAttribute("sesion_iniciada");

				if (ini == null) {
					sesion.setAttribute("sesion_iniciada", false);
				}
				req.getRequestDispatcher("index.jsp").forward(req, resp);

			} else {
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/modificar_usuario.html") == 0) {
			req.getRequestDispatcher("modificar_usuario.jsp").forward(req, resp);
		} else if (path.compareTo("/modificar_usuario_admin.html") == 0) {
			req.setAttribute("usuario", req.getParameter("referenciaM"));
			req.getRequestDispatcher("modificar_usuario_admin.jsp").forward(req, resp);
		} else if (path.compareTo("/cerrar_sesion.html") == 0) {
			Usuario _usuario = new Usuario();

			sesion.setAttribute("sesion_iniciada", false);
			sesion.setAttribute("usuario", _usuario);
			
			Respuesta res = productoTotal();
			
			if(res.getCop()==200) {
				sesion.setAttribute("productos", res.getLista());
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			}	
			else {

				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/cuenta.html") == 0) {
			req.getRequestDispatcher("cuenta.jsp").forward(req, resp);
		} else if (path.compareTo("/compras_realizadas.html") == 0) {
			
			Client client = ClientBuilder.newClient();
			Usuario user = (Usuario) sesion.getAttribute("usuario");
			WebTarget webResource = client.target("http://localhost:12504").path("compras").queryParam("comprador",
					user.getEmail());
			Response r = webResource.request().accept("application/json").get();
			int cop = r.getStatus();

			if (cop == 200) {
				
				List<Compra> compra = webResource.request().accept("application/json").get(new GenericType<List<Compra>>() {
				});
				ArrayList<Producto> compras = new ArrayList<Producto>();
				for (int i = 0; i < compra.size(); i++) {
					Compra c = compra.get(i);
					String[] parts = c.getReferencia().split("-");

					for (int x = 0; x < parts.length; x++) {

						Client client2 = ClientBuilder.newClient();
						
						WebTarget webResource2 = client2.target("http://localhost:12503").path("producto").queryParam("referencia",parts[x]);
						Response r2 = webResource2.request().accept("application/json").get();
						int cop2 = r2.getStatus();

						if (cop2 == 200) {
							Producto producto = webResource2.request().accept("application/json").get(new GenericType<Producto>() {
							});
							compras.add(producto);
						}
					}
				}
				req.setAttribute("compras", compras);
				req.getRequestDispatcher("compras_realizadas.jsp").forward(req, resp);
			} else {
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}
		} else if (path.compareTo("/add_producto.html") == 0) {
			req.getRequestDispatcher("registrar_producto.jsp").forward(req, resp);
		} else if (path.compareTo("/cuenta-productos.html") == 0) {
			
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target("http://localhost:12503").path("produc").queryParam("vendedor",
					req.getParameter("referenciaM"));
			Response r = webResource.request().accept("application/json").get();
			int cop = r.getStatus();

			if (cop == 200) {
				List<Producto> producto = webResource.request().accept("application/json").get(new GenericType<List<Producto>>() {
				});
				req.setAttribute("producto", producto);
				req.getRequestDispatcher("cuenta-productos.jsp").forward(req, resp);

			} else {
				req.getRequestDispatcher("registrar_producto-incorrecto.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/busqueda-avanzada.html") == 0) {
			req.getRequestDispatcher("busqueda-avanzada.jsp").forward(req, resp);
		} /*else if (path.compareTo("/mensajes.html") == 0) {

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
			System.out.println("llamamos a mensajes");
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
*/
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		sesion = req.getSession();
		String path = req.getServletPath();

		if (path.compareTo("/analizar-login.html") == 0) {
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target("http://localhost:12502").path("usuario").queryParam("email",
					req.getParameter("email"));
			Response r = webResource.request().accept("application/json").get();
			int cop = r.getStatus();

			if (cop == 200) {
				Usuario user = webResource.request().accept("application/json").get(new GenericType<Usuario>() {
				});

				if (user.getContrasenia().compareTo(req.getParameter("contrasenia")) == 0) {
					sesion.setAttribute("sesion_iniciada", true);
					sesion.setAttribute("usuario", user);
					req.getRequestDispatcher("index.jsp").forward(req, resp);
				} else {
					req.getRequestDispatcher("login-incorrecto.jsp").forward(req, resp);
				}

			} else {
				req.getRequestDispatcher("login-incorrecto.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/analizar-registro.html") == 0) {

			Usuario user = new Usuario();
			user.setEmail(req.getParameter("email"));
			user.setNombre(req.getParameter("nombre"));
			user.setApellido(req.getParameter("apellido"));
			user.setDireccion(req.getParameter("direccion"));
			user.setContrasenia(req.getParameter("contrasenia"));
			user.setRol(req.getParameter("rol"));

			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target("http://localhost:12502").path("usuario");
			Response r = webResource.request().accept("application/json")
					.post(Entity.entity(user, MediaType.APPLICATION_JSON));
			int cop = r.getStatus();

			if (cop == 201) {

				req.getRequestDispatcher("registro-correcto.jsp").forward(req, resp);
			} else {
				req.getRequestDispatcher("registro-incorrecto.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/modificar_usuario-correcto.html") == 0) {

			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target("http://localhost:12502").path("usuario").queryParam("email",
					req.getParameter("email"));
			Response r = webResource.request().accept("application/json").get();
			int cop = r.getStatus();

			if (cop == 200) {
				Usuario u = webResource.request().accept("application/json").get(new GenericType<Usuario>() {
				});
				Usuario user = new Usuario();
				user.setEmail(req.getParameter("email"));
				user.setNombre(req.getParameter("nombre"));
				user.setApellido(req.getParameter("apellido"));
				user.setDireccion(req.getParameter("direccion"));
				user.setContrasenia(req.getParameter("contrasenia"));
				user.setRol(u.getRol());
				Client client2 = ClientBuilder.newClient();
				WebTarget webResource2 = client2.target("http://localhost:12502").path("usuario");
				Response r2 = webResource2.request().accept("application/json")
						.put(Entity.entity(user, MediaType.APPLICATION_JSON));
				int cop2 = r2.getStatus();

				if (cop2 == 200) {
					req.getRequestDispatcher("modificar_usuario-correcto.jsp").forward(req, resp);
				} else {
					req.getRequestDispatcher("modificar-usuario-incorrecto.jsp").forward(req, resp);
				}
			

			} else {
				req.getRequestDispatcher("modificar-usuario-incorrecto.jsp").forward(req, resp);
			}
				

		} else if (path.compareTo("/eliminar-usuario.html") == 0) {

			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target("http://localhost:12502").path("usuario").queryParam("email",
					req.getParameter("email"));
			Response r = webResource.request().accept("application/json").delete();
			int cop = r.getStatus();
			if (cop == 200) {
				Usuario _usuario = new Usuario();
				_usuario.setRol("");
				_usuario.setEmail("");
				sesion.setAttribute("sesion_iniciada", false);
				sesion.setAttribute("usuario", _usuario);
				req.getRequestDispatcher("eliminar-usuario-correcto.jsp").forward(req, resp);
			} else {
				req.getRequestDispatcher("eliminar-usuario-incorrecto.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/agregar_producto.html") == 0) {

			
			Producto producto = new Producto();
			producto.setNombre(req.getParameter("nombreProd"));
			producto.setDescripcion(req.getParameter("descripcionProd"));
			producto.setCategoria(req.getParameter("categoriaProd"));
			Part imagen = req.getPart("fotoproducto");
			byte array[] = new byte[(int)imagen.getSize()];
			imagen.getInputStream().read(array);
			producto.setImagen(array);
			producto.setPrecio(Float.parseFloat(req.getParameter("precioProd")));
			Usuario usu = (Usuario) sesion.getAttribute("usuario");
			producto.setUsuario(usu);
			producto.setEstado((byte) 0);
			

			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target("http://localhost:12503").path("producto");
			Response r = webResource.request().accept("application/json")
					.post(Entity.entity(producto, MediaType.APPLICATION_JSON));
			int cop = r.getStatus();

			if (cop == 201) {
				req.getRequestDispatcher("registrar_producto-correctamente.jsp").forward(req, resp);
			} else {
				req.getRequestDispatcher("registrar_producto-incorrecto.jsp").forward(req, resp);
			}
		} else if (path.compareTo("/eliminar-producto.html") == 0) {
			
			
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target("http://localhost:12503").path("producto").queryParam("referencia",
					req.getParameter("referenciaE"));
			Response r = webResource.request().accept("application/json").delete();
			int cop = r.getStatus();
			if (cop == 200) {
				req.getRequestDispatcher("producto-eliminar-correctamente.jsp").forward(req, resp);
			} else {
				req.getRequestDispatcher("producto-eliminar-incorrectamente.jsp").forward(req, resp);
			}
		} else if (path.compareTo("/modificar-producto.html") == 0) {
			
			Producto producto = new Producto();
			producto.setReferencia(Integer.parseInt(req.getParameter("referenciaProd")));
			producto.setNombre(req.getParameter("nombreProd"));
			producto.setDescripcion(req.getParameter("descripcionProd"));
			producto.setCategoria(req.getParameter("categoriaProd"));
			Part imagen = req.getPart("fotoproducto");
			byte array[] = new byte[(int)imagen.getSize()];
			imagen.getInputStream().read(array);
			producto.setImagen(array);
			producto.setPrecio(Float.parseFloat(req.getParameter("precioProd")));
			Usuario usu = (Usuario) sesion.getAttribute("usuario");
			producto.setUsuario(usu);
			producto.setEstado((byte) 0);
			
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target("http://localhost:12503").path("producto");
			Response r = webResource.request().accept("application/json")
					.put(Entity.entity(producto, MediaType.APPLICATION_JSON));
			int cop = r.getStatus();

			if (cop == 200) {
				req.getRequestDispatcher("modificar_producto-correcto.jsp").forward(req, resp);
			} else {
				req.getRequestDispatcher("modificar_producto-incorrecto.jsp").forward(req, resp);
			}
		} else if (path.compareTo("/producto.html") == 0) {
			
			
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target("http://localhost:12503").path("producto").queryParam("referencia", req.getParameter("referenciaM"));
			Response r = webResource.request().accept("application/json").get();
			int cop = r.getStatus();

			if (cop == 200) {
				Producto producto = webResource.request().accept("application/json").get(new GenericType<Producto>() {
				});
				List<Producto> productoList = new ArrayList<Producto>();
				productoList.add(producto);
				req.setAttribute("producto_info", productoList);
				req.getRequestDispatcher("producto.jsp").forward(req, resp);
			}
			else {
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}
			
		} else if (path.compareTo("/producto_index.html") == 0) {
			
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target("http://localhost:12503").path("producto").queryParam("referencia", req.getParameter("referenciaM"));
			Response r = webResource.request().accept("application/json").get();
			int cop = r.getStatus();
			Respuesta res = new Respuesta();
			res.setCop(cop);
			if (cop == 200) {
				Producto producto =  webResource.request().accept("application/json").get(new GenericType<Producto>() {
				});
				List<Producto> productoList = new ArrayList<Producto>();
				productoList.add(producto);
				req.setAttribute("producto_part", productoList);
				req.getRequestDispatcher("producto-index.jsp").forward(req, resp);
			}
			else {
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/agregar_carro.html") == 0) {
			
			@SuppressWarnings("unchecked")
			List<Producto> carro = (List<Producto>) sesion.getAttribute("carro");
			if(carro == null) {
				carro = new ArrayList<Producto>();
			}
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target("http://localhost:12503").path("producto").queryParam("referencia", req.getParameter("referenciaE"));
			Response r = webResource.request().accept("application/json").get();
			int cop = r.getStatus();
			Respuesta res = new Respuesta();
			res.setCop(cop);
			if (cop == 200) {
				Producto producto =  webResource.request().accept("application/json").get(new GenericType<Producto>() {
				});
				
				
				int existe = 0;
				for (int i = 0; i < carro.size(); i++) {
					Producto p = carro.get(i);
					if(p.getReferencia()==producto.getReferencia()) {
						existe = 1;
					}
				}
				if(existe == 0) carro.add(producto);
					
				sesion.setAttribute("carro", carro);
				
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			}
			else {
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/eliminar_carro.html") == 0) {
			@SuppressWarnings("unchecked")
			List<Producto> carro = (List<Producto>) sesion.getAttribute("carro");
			for (int i = 0; i < carro.size(); i++) {
				Producto p = carro.get(i);
				if(p.getReferencia()==Integer.parseInt(req.getParameter("referenciaC"))) carro.remove(i);
			}
			sesion.setAttribute("carro", carro);
			req.getRequestDispatcher("index.jsp").forward(req, resp);
		} else if (path.compareTo("/busqueda.html") == 0) {
			
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target("http://localhost:12503").path("productosb").queryParam("busqueda", req.getParameter("name"));
			Response r = webResource.request().accept("application/json").get();
			int cop = r.getStatus();
			Respuesta res = new Respuesta();
			res.setCop(cop);
			if (cop == 200) {
				List<Producto> productos =  webResource.request().accept("application/json").get(new GenericType<List<Producto>>() {
				});
				req.setAttribute("productos-busqueda", productos);
				req.getRequestDispatcher("res-busqueda.jsp").forward(req, resp);
			}
			else {
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/buscar_producto.html") == 0) {
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target("http://localhost:12503").path("productosba").queryParam("nombre", req.getParameter("nombreProd")).queryParam("categoria", req.getParameter("categoriaProd")).queryParam("precio", req.getParameter("precioProd"));
			Response r = webResource.request().accept("application/json").get();
			int cop = r.getStatus();
			Respuesta res = new Respuesta();
			res.setCop(cop);
			if (cop == 200) {
				List<Producto> productos =  webResource.request().accept("application/json").get(new GenericType<List<Producto>>() {
				});
				req.setAttribute("productos-busqueda", productos);
				req.getRequestDispatcher("res-busqueda.jsp").forward(req, resp);
			}
			else {
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			}
		} else if (path.compareTo("/usuarios_admin.html") == 0) {
			
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target("http://localhost:12502").path("usuarios");
			Response r = webResource.request().accept("application/json").get();
			int cop = r.getStatus();
			if (cop == 200) {
				List<Usuario> us = webResource.request().accept("application/json").get(new GenericType<List<Usuario>>() {
				});
				req.setAttribute("usuario-admin", us);

				req.getRequestDispatcher("usuarios_admin.jsp").forward(req, resp);
			}
			else {
				req.getRequestDispatcher("login-incorrecto.jsp").forward(req, resp);
			}
			

		} else if (path.compareTo("/productos_admin.html") == 0) {
			Respuesta res = productoTotal();

			if (res.getCop() == 200) {
	
				req.setAttribute("producto", res.getLista());
				

				req.getRequestDispatcher("cuenta-productos.jsp").forward(req, resp);

			} else {
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/eliminar-usuario-admin.html") == 0)

		{
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target("http://localhost:12502").path("usuario").queryParam("email",
					req.getParameter("referenciaM"));
			Response r = webResource.request().accept("application/json").delete();
			int cop = r.getStatus();
			if (cop == 200) {
				req.getRequestDispatcher("eliminar-usuario-correcto.jsp").forward(req, resp);
			} else {
				req.getRequestDispatcher("eliminar-usuario-incorrecto.jsp").forward(req, resp);
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
								_producto.setNombre(rs2.getString(2));
								_producto.setDescripcion(rs2.getString(3));
								_producto.setCategoria(rs2.getString(4));
								Blob bytesImagen = rs2.getBlob(5);
								byte[] imgData = bytesImagen.getBytes(1, (int) bytesImagen.length());
								//_producto.setImagen(imgData);
								_producto.setPrecio(rs2.getFloat(6));
								//_producto.setVendedor(rs2.getString(7));
								//_producto.setEstado(rs2.getBoolean(8));
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
								_producto.setNombre(rs2.getString(2));
								_producto.setDescripcion(rs2.getString(3));
								_producto.setCategoria(rs2.getString(4));
								Blob bytesImagen = rs2.getBlob(5);
								byte[] imgData = bytesImagen.getBytes(1, (int) bytesImagen.length());
								//_producto.setImagen(imgData);
								_producto.setPrecio(rs2.getFloat(6));
								//_producto.setVendedor(rs2.getString(7));
								if (usuarios.contains(rs2.getString(7)) == false) {
									usuarios.add(rs2.getString(7));
								}
								//_producto.setEstado(rs2.getBoolean(8));
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
							/*if (vend.compareTo(p.getUser()) == 0) {
								referencia += p.getReferencia() + "-";
								vendedor = p.getUser();
								comprador = user.getEmail();
								total += p.getPrecio();
								tarjeta = req.getParameter("tarjeta");
								direccion = req.getParameter("direccion");
								Date objDate = new Date();
								fecha = objDate.toString();
							}*/
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
							//_carro.setVendedor(rs2.getString(2));
							_carro.setPrecio(rs2.getFloat(3));
							//_carro.setNombre(rs2.getString(4));
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
								_producto.setNombre(rs3.getString(2));

								_producto.setDescripcion(rs3.getString(3));
								_producto.setCategoria(rs3.getString(4));
								Blob bytesImagen = rs3.getBlob(5);
								byte[] imgData = bytesImagen.getBytes(1, (int) bytesImagen.length());
								//_producto.setImagen(imgData);
								_producto.setPrecio(rs3.getFloat(6));
								//_producto.setVendedor(rs3.getString(7));
								//_producto.setEstado(rs3.getBoolean(8));
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
				if (c.getReferencia().compareTo(referencia) == 0) {
					index = i;
				}
			}
			if (index != -1) {
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
				if (c.getReferencia().compareTo(referencia) == 0) {
					index = i;
				}
			}
			if (index != -1) {
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
				//_carro.setVendedor(rs2.getString(2));
				_carro.setPrecio(rs2.getFloat(3));
				//_carro.setNombre(rs2.getString(4));
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
	
	public Respuesta productoTotal(){
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:12503").path("productos");
		Response r = webResource.request().accept("application/json").get();
		int cop = r.getStatus();
		Respuesta res = new Respuesta();
		res.setCop(cop);
		if (cop == 200) {
			res.setLista(webResource.request().accept("application/json").get(new GenericType<List<Producto>>() {
			}));
		}
		return res;
	}

}
