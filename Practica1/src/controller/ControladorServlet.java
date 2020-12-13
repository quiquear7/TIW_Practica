package controller;

import java.util.List;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.el.parser.ParseException;

import model.Chat;
import model.Compra;
import model.Producto;
import model.Respuesta;
import model.Usuario;

/**
 * Servlet implementation class ControladorServlet
 */
@WebServlet("/ControladorServlet")
@MultipartConfig
public class ControladorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	ServletContext miServletContex = null;

	String strAutor;
	HttpSession sesion;

	public void init(ServletConfig config) throws ServletException {

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

			if (res.getCop() == 200) {
				sesion.setAttribute("productos", res.getLista());
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			} else {

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

				List<Compra> compra = webResource.request().accept("application/json")
						.get(new GenericType<List<Compra>>() {
						});
				ArrayList<Producto> compras = new ArrayList<Producto>();
				for (int i = 0; i < compra.size(); i++) {
					Compra c = compra.get(i);
					String[] parts = c.getReferencia().split("-");

					for (int x = 0; x < parts.length; x++) {

						Client client2 = ClientBuilder.newClient();

						WebTarget webResource2 = client2.target("http://localhost:12503").path("producto")
								.queryParam("referencia", parts[x]);
						Response r2 = webResource2.request().accept("application/json").get();
						int cop2 = r2.getStatus();

						if (cop2 == 200) {
							Producto producto = webResource2.request().accept("application/json")
									.get(new GenericType<Producto>() {
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
				List<Producto> producto = webResource.request().accept("application/json")
						.get(new GenericType<List<Producto>>() {
						});
				req.setAttribute("producto", producto);
				req.getRequestDispatcher("cuenta-productos.jsp").forward(req, resp);

			} else {
				req.getRequestDispatcher("registrar_producto-incorrecto.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/busqueda-avanzada.html") == 0)

		{
			req.getRequestDispatcher("busqueda-avanzada.jsp").forward(req, resp);
		} else if (path.compareTo("/mensajes.html") == 0) {
			Respuesta res = leerMensaje();

			if (res.getCop() == 200) {
				@SuppressWarnings("unchecked")
				List<Chat> mensaje = (List<Chat>) res.getLista();
				req.setAttribute("mensaje", mensaje);

				req.getRequestDispatcher("mensajes.jsp").forward(req, resp);

			} else {
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/conf_compras.html") == 0) {

			/*
			 * List<Comp> contenidos = rc.read(); for (int i = 0; i < contenidos.size();
			 * i++) { Comp c = contenidos.get(i); conf.add(c); }
			 * req.setAttribute("confirmaciones", conf);
			 * 
			 * req.setAttribute("receptor", req.getParameter("referenciaE"));
			 * 
			 * 
			 * req.getRequestDispatcher("confirmaciones.jsp").forward(req, resp);
			 */

		}
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

		} else if (path.compareTo("/eliminar-usuario.html") == 0)

		{

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
			byte array[] = new byte[(int) imagen.getSize()];
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
			byte array[] = new byte[(int) imagen.getSize()];
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
			WebTarget webResource = client.target("http://localhost:12503").path("producto").queryParam("referencia",
					req.getParameter("referenciaM"));
			Response r = webResource.request().accept("application/json").get();
			int cop = r.getStatus();

			if (cop == 200) {
				Producto producto = webResource.request().accept("application/json").get(new GenericType<Producto>() {
				});
				List<Producto> productoList = new ArrayList<Producto>();
				productoList.add(producto);
				req.setAttribute("producto_info", productoList);
				req.getRequestDispatcher("producto.jsp").forward(req, resp);
			} else {
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/producto_index.html") == 0) {

			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target("http://localhost:12503").path("producto").queryParam("referencia",
					req.getParameter("referenciaM"));
			Response r = webResource.request().accept("application/json").get();
			int cop = r.getStatus();
			Respuesta res = new Respuesta();
			res.setCop(cop);
			if (cop == 200) {
				Producto producto = webResource.request().accept("application/json").get(new GenericType<Producto>() {
				});
				List<Producto> productoList = new ArrayList<Producto>();
				productoList.add(producto);
				req.setAttribute("producto_part", productoList);
				req.getRequestDispatcher("producto-index.jsp").forward(req, resp);
			} else {
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/agregar_carro.html") == 0) {

			@SuppressWarnings("unchecked")
			List<Producto> carro = (List<Producto>) sesion.getAttribute("carro");
			if (carro == null) {
				carro = new ArrayList<Producto>();
			}
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target("http://localhost:12503").path("producto").queryParam("referencia",
					req.getParameter("referenciaE"));
			Response r = webResource.request().accept("application/json").get();
			int cop = r.getStatus();
			Respuesta res = new Respuesta();
			res.setCop(cop);
			if (cop == 200) {
				Producto producto = webResource.request().accept("application/json").get(new GenericType<Producto>() {
				});

				int existe = 0;
				for (int i = 0; i < carro.size(); i++) {
					Producto p = carro.get(i);
					if (p.getReferencia() == producto.getReferencia()) {
						existe = 1;
					}
				}
				if (existe == 0)
					carro.add(producto);

				sesion.setAttribute("carro", carro);

				req.getRequestDispatcher("index.jsp").forward(req, resp);
			} else {
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/eliminar_carro.html") == 0) {
			@SuppressWarnings("unchecked")
			List<Producto> carro = (List<Producto>) sesion.getAttribute("carro");
			for (int i = 0; i < carro.size(); i++) {
				Producto p = carro.get(i);
				if (p.getReferencia() == Integer.parseInt(req.getParameter("referenciaC")))
					carro.remove(i);
			}
			sesion.setAttribute("carro", carro);
			req.getRequestDispatcher("index.jsp").forward(req, resp);
		} else if (path.compareTo("/busqueda.html") == 0) {

			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target("http://localhost:12503").path("productosb").queryParam("busqueda",
					req.getParameter("name"));
			Response r = webResource.request().accept("application/json").get();
			int cop = r.getStatus();
			Respuesta res = new Respuesta();
			res.setCop(cop);
			if (cop == 200) {
				List<Producto> productos = webResource.request().accept("application/json")
						.get(new GenericType<List<Producto>>() {
						});
				req.setAttribute("productos-busqueda", productos);
				req.getRequestDispatcher("res-busqueda.jsp").forward(req, resp);
			} else {
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/buscar_producto.html") == 0) {
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target("http://localhost:12503").path("productosba")
					.queryParam("nombre", req.getParameter("nombreProd"))
					.queryParam("categoria", req.getParameter("categoriaProd"))
					.queryParam("precio", req.getParameter("precioProd"));
			Response r = webResource.request().accept("application/json").get();
			int cop = r.getStatus();
			Respuesta res = new Respuesta();
			res.setCop(cop);
			if (cop == 200) {
				List<Producto> productos = webResource.request().accept("application/json")
						.get(new GenericType<List<Producto>>() {
						});
				req.setAttribute("productos-busqueda", productos);
				req.getRequestDispatcher("res-busqueda.jsp").forward(req, resp);
			} else {
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			}
		} else if (path.compareTo("/usuarios_admin.html") == 0) {

			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target("http://localhost:12502").path("usuarios");
			Response r = webResource.request().accept("application/json").get();
			int cop = r.getStatus();
			if (cop == 200) {
				List<Usuario> us = webResource.request().accept("application/json")
						.get(new GenericType<List<Usuario>>() {
						});
				req.setAttribute("usuario-admin", us);

				req.getRequestDispatcher("usuarios_admin.jsp").forward(req, resp);
			} else {
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
			Chat conver = new Chat();
			Usuario user = (Usuario) sesion.getAttribute("usuario");
			conver.setEmisor(user.getEmail());
			conver.setReceptor(req.getParameter("referenciaE"));
			conver.setMensaje(req.getParameter("mensaje"));

			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target("http://localhost:12505").path("chat");
			Response r = webResource.request().accept("application/json")
					.post(Entity.entity(conver, MediaType.APPLICATION_JSON));
			int cop = r.getStatus();

			if (cop == 201) {

				Client client2 = ClientBuilder.newClient();
				WebTarget webResource2 = client2.target("http://localhost:12505").path("chat").queryParam("usuario",
						user.getEmail());
				Response r2 = webResource2.request().accept("application/json").get();
				int cop2 = r2.getStatus();

				if (cop2 == 200) {
					List<Chat> mens = webResource2.request().accept("application/json")
							.get(new GenericType<List<Chat>>() {
							});
					req.setAttribute("mensaje", mens);
					req.setAttribute("receptor", req.getParameter("referenciaE"));
					req.getRequestDispatcher("chat_unico.jsp").forward(req, resp);

				} else {
					req.getRequestDispatcher("error.jsp").forward(req, resp);
				}

			} else {
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/pagar.html") == 0) {

			@SuppressWarnings("unchecked")
			List<Producto> carro = (List<Producto>) sesion.getAttribute("carro");
			if (carro == null) {
				req.getRequestDispatcher("carro_vacio.jsp").forward(req, resp);
			} else {
				req.setAttribute("producto", carro);
				req.getRequestDispatcher("compra.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/pago.html") == 0) {

			int error = 0;

			ArrayList<String> usuarios = new ArrayList<String>();
			ArrayList<Integer> ref = new ArrayList<Integer>();

			Usuario user = (Usuario) sesion.getAttribute("usuario");

			@SuppressWarnings("unchecked")
			List<Producto> carro = (List<Producto>) sesion.getAttribute("carro");
			System.out.println("Obtenemos lista usuarios");
			for (int i = 0; i < carro.size(); i++) {
				Producto p = carro.get(i);
				if (p.getEstado() == 0) {
					if (usuarios.contains(p.getUsuario().getEmail()) == false) {
						usuarios.add(p.getUsuario().getEmail());
					}
				}

			}

			req.setAttribute("producto", carro);
			for (int i = 0; i < usuarios.size(); i++) {
				String referencia = "";
				String vendedor = "";
				String comprador = "";
				Float total = (float) 0;
				String direccion = "";
				String tarjeta = "";
				String cv2 = "";
				String fechatarjeta = "";
				String fecha = "";
				String vend = usuarios.get(i);
				for (int x = 0; x < carro.size(); x++) {
					Producto p = carro.get(x);

					if (vend.compareTo(p.getUsuario().getEmail()) == 0) {
						referencia += p.getReferencia() + "-";
						vendedor = p.getUsuario().getEmail();
						comprador = user.getEmail();
						total += p.getPrecio();
						tarjeta = req.getParameter("tarjeta");
						cv2 = req.getParameter("cv2");
						fechatarjeta = req.getParameter("fecha");
						direccion = req.getParameter("direccion");
						Date objDate = new Date();
						fecha = objDate.toString();
						ref.add(p.getReferencia());
					}
					System.out.println("Referencia: " + referencia);

				}
				Compra compra = new Compra(referencia, comprador, cv2, direccion, fecha, fechatarjeta, total, tarjeta,
						vendedor);
				Client client = ClientBuilder.newClient();
				WebTarget webResource = client.target("http://localhost:12506").path("banco");
				Response r = webResource.request().accept("application/json")
						.post(Entity.entity(compra, MediaType.APPLICATION_JSON));
				int cop = r.getStatus();

				if (cop == 200) {
					for (int j = 0; j < ref.size(); j++) {
						Client client2 = ClientBuilder.newClient();
						WebTarget webResource2 = client2.target("http://localhost:12503").path("producto")
								.queryParam("referencia", ref.get(j));
						Response r2 = webResource2.request().accept("application/json").get();
						int cop2 = r2.getStatus();

						if (cop2 == 200) {
							Producto producto = webResource2.request().accept("application/json")
									.get(new GenericType<Producto>() {
									});
							producto.setEstado((byte) 1);
							Client client3 = ClientBuilder.newClient();
							WebTarget webResource3 = client3.target("http://localhost:12503").path("producto");
							Response r3 = webResource3.request().accept("application/json")
									.put(Entity.entity(producto, MediaType.APPLICATION_JSON));
							if (r3.getStatus() != 200) {
								error = 1;
							}
						} else {
							error = 1;
						}

					}

				} else {
					error = 1;
				}

			}
			Respuesta res = productoTotal();

			if (res.getCop() == 200) {

				req.setAttribute("productos", res.getLista());
				ArrayList<Producto> carro_vacio = new ArrayList<Producto>();
				sesion.setAttribute("carro", carro_vacio);

			} else {
				error = 1;
			}

			if (error == 1) {
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			} else {
				req.getRequestDispatcher("pago_aceptado.jsp").forward(req, resp);
			}

		} else if (path.compareTo("/abrir_chat.html") == 0) {

			Respuesta res = leerMensaje();

			if (res.getCop() == 200) {
				@SuppressWarnings("unchecked")
				List<Chat> mensaje = (List<Chat>) res.getLista();
				req.setAttribute("mensaje", mensaje);

				req.setAttribute("receptor", req.getParameter("referenciaE"));
				req.getRequestDispatcher("chat_unico.jsp").forward(req, resp);
			} else {
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}

		} /*
			 * else if (path.compareTo("/confirmar_compra.html") == 0) { String referencia =
			 * req.getParameter("referencia"); String comprador =
			 * req.getParameter("comprador"); String vendedor =
			 * req.getParameter("vendedor"); float precio =
			 * Float.parseFloat(req.getParameter("precio")); String fecha =
			 * req.getParameter("fecha"); String tarjeta = req.getParameter("tarjeta");
			 * String direccion = req.getParameter("direccion"); int index = -1; for (int i
			 * = 0; i < conf.size(); i++) { Comp c = conf.get(i); if
			 * (c.getReferencia().compareTo(referencia) == 0) { index = i; } } if (index !=
			 * -1) { conf.remove(index); } Comp c = new Comp(referencia, vendedor,
			 * comprador, precio, tarjeta, direccion, fecha);
			 * 
			 * procesar_compraJDBC(c); vaciar_carro(comprador); cambiar_estado(referencia);
			 * req.getRequestDispatcher("pago_aceptado.jsp").forward(req, resp); } else if
			 * (path.compareTo("/cancelar_compra.html") == 0)
			 * 
			 * { String referencia = req.getParameter("referencia"); String comprador =
			 * req.getParameter("comprador"); int index = -1; for (int i = 0; i <
			 * conf.size(); i++) { Comp c = conf.get(i); if
			 * (c.getReferencia().compareTo(referencia) == 0) { index = i; } } if (index !=
			 * -1) { conf.remove(index); } vaciar_carro(comprador);
			 * 
			 * req.getRequestDispatcher("index.jsp").forward(req, resp); }
			 */

	}

	public Respuesta productoTotal() {
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

	public Respuesta leerMensaje() {
		Client client = ClientBuilder.newClient();
		Usuario user = (Usuario) sesion.getAttribute("usuario");
		WebTarget webResource = client.target("http://localhost:12505").path("chat").queryParam("usuario",
				user.getEmail());
		Response r = webResource.request().accept("application/json").get();
		int cop = r.getStatus();
		Respuesta res = new Respuesta();
		res.setCop(cop);
		if (cop == 200) {
			res.setLista(webResource.request().accept("application/json").get(new GenericType<List<Chat>>() {
			}));
		}
		return res;
	}

}
