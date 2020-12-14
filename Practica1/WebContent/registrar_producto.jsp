<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="model.Usuario"%>
<%@page import="model.Producto"%>
<%@page import="model.Carro"%>
<%@page import="model.Compra"%>
<%@page import="model.Mensaje"%>
<%@page import="java.sql.ResultSet" import="javax.naming.InitialContext"
	import="javax.naming.Context" import=" java.io.OutputStream"
	import="java.sql.Statement" import=" java.util.Base64"
	import="javax.sql.DataSource" import="java.sql.SQLException"
	import=" java.sql.Connection" import="java.sql.ResultSet"
	import="java.sql.Statement"%>
<%@page import="java.util.*"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->

<title>Oyarzabal</title>

<!-- Google font -->
<link href="https://fonts.googleapis.com/css?family=Hind:400,700"
	rel="stylesheet">

<!-- Bootstrap -->
<link type="text/css" rel="stylesheet" href="css/bootstrap.min.css" />

<!-- Slick -->
<link type="text/css" rel="stylesheet" href="css/slick.css" />
<link type="text/css" rel="stylesheet" href="css/slick-theme.css" />

<!-- nouislider -->
<link type="text/css" rel="stylesheet" href="css/nouislider.min.css" />

<!-- Font Awesome Icon -->
<link rel="stylesheet" href="css/font-awesome.min.css">

<!-- Custom stlylesheet -->
<link type="text/css" rel="stylesheet" href="css/style.css" />

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
		  <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
		  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
		<![endif]-->

</head>

<body>
	<!-- HEADER -->
	<header>


		<!-- header -->
		<div id="header">
			<div class="container">
				<div class="pull-left">
					<!-- Logo -->
					<div class="header-logo">
						<a class="logo" href="index.html"> <img src="./img/logo.png"
							alt="">
						</a>
					</div>
					<!-- /Logo -->

					<%
					Boolean login = (Boolean) session.getAttribute("sesion_iniciada");

					Usuario usu = (Usuario) session.getAttribute("usuario");
					
					if(login==null){
						login = false;
					}
					if(usu==null){
						login = false;
					}
					if (login == true) {
						if(usu.getRol().compareTo("Cliente") == 0){
					%>
					<!-- Search -->
					<div class="header-search">
						<form action="busqueda.html" action="ControladorServlet"
							method="post">
							<input class="input search-input" name="name" type="text"
								placeholder="Busqueda">
							<button class="search-btn">
								<i class="fa fa-search"></i>
							</button>
						</form>
					</div>
					<a href="busqueda-avanzada.html">Busqueda Avanzada</a>

					<%
					}}
					%>
					<!-- /Search -->
				</div>
				<div class="pull-right">
					<ul class="header-btns">
						<!-- Account -->
						<li class="header-account dropdown default-dropdown">
							<div class="dropdown-toggle" role="button" data-toggle="dropdown"
								aria-expanded="true">
								<div class="header-btns-icon">
									<i class="fa fa-user-o"></i>
								</div>
								<%
									if (login == false) {
								%>
								<strong class="text-uppercase">Mi Cuenta <i
									class="fa fa-caret-down"></i></strong>
								<%
									} else {
								%>
								<strong class="text-uppercase"><%=usu.getEmail()%> <i
									class="fa fa-caret-down"></i></strong>
								<%
									}
								%>
							</div>


							<ul class="custom-menu">
								<%
									if (login == false) {
								%>
								<li><a href="login.html"><i class="fa fa-unlock-alt"></i>Login</a></li>
								<li><a href="registro.html"><i class="fa fa-user-plus"></i>
										Crear Cuenta</a></li>
								<%
									} else {
								%>
								<li><a href="cuenta.html"><i class="fa fa-user-o"></i>
										Mi Cuenta</a></li>
								<li><a href="modificar_usuario.html"><i
										class="fa fa-unlock-alt"></i>Modificar Usuario</a></li>
								<li><a href="cerrar_sesion.html"><i
										class="fa fa-user-plus"></i> Cerrar Sesion</a></li>
								<%
									}
								%>


							</ul>
						</li>
						<!-- /Account -->

						<!-- Cart -->
						<%
							if (login == true ) {
								if(usu.getRol().compareTo("Cliente") == 0){
							ArrayList<Carro> c = (ArrayList<Carro>) session.getAttribute("carro");
							float total = 0;

							if (c != null) {
						%>

						<li class="header-cart dropdown default-dropdown"><a
							class="dropdown-toggle" data-toggle="dropdown"
							aria-expanded="true">
								<div class="header-btns-icon">
									<i class="fa fa-shopping-cart"></i>
								</div> <strong class="text-uppercase">Carro</strong> <br>

						</a>
							<div class="custom-menu">
								<div id="shopping-cart">
									<div class="shopping-cart-list">


										<%
											for (int x = 0; x < c.size(); x++) {

											Carro carrito = c.get(x);

											byte[] photo = carrito.getImagen();
											String bphoto = Base64.getEncoder().encodeToString(photo);
											total += carrito.getPrecio();
										%>
										<div class="product product-widget">
											<div class="product-thumb">
												<img alt=""
													style="max-width: 70%; width: auto; height: auto;"
													src="data:image/png;base64,<%=bphoto%>" />
											</div>
											<div class="product-body">
												<h3 class="product-price"><%=carrito.getPrecio()%>$
												</h3>
												<h2 class="product-name"><%=carrito.getTitulo()%></h2>
												<form action="producto_index.html"
													action="ControladorServlet" method="post">
													<input class="form-wt" type="hidden" name="referenciaM"
														value=<%=carrito.getReferencia()%> required> <input
														type="submit" class="prod_btn" value="Mas Info">
												</form>
											</div>


											<form action="eliminar_carro.html"
												action="ControladorServlet" method="post">
												<input class="form-wt" type="hidden" name="referenciaC"
													value=<%=carrito.getReferencia()%> required> <input
													type="submit" class="cancel-btn" value="X">
											</form>

										</div>
										<%
											}
										%>
									</div>
									<div class="shopping-cart-btns">
										<span>Total a Pagar: <%=total%> $
										</span> <br></br>
										<form action="pagar.html" action="ControladorServlet"
											method="post">
											<input class="form-wt" type="hidden" name="referenciaC"
												value=required> <input type="submit" class="primary-btn add-to-cart" value="Pagar">
										</form>
									</div>
								</div>
							</div></li>
						<!-- /Cart -->

						<%
							}
						}}
						%>
						<li class="header-account dropdown default-dropdown">
							<%
								if (login == true) {
									if (usu.getRol().compareTo("Vendedor") == 0) {
							%> <strong><a href="add_producto.html">Nuevo
									Producto</a></strong>
									<%
 							}}
 							%> <%
 							if (login == true) {
 							%><strong><a href="mensajes.html">Mensajes</a></strong> <%
 							}
 							%>

						</li>


					</ul>
				</div>
			</div>
			<!-- header -->
		</div>
		<!-- container -->
	</header>
	<!-- /HEADER -->


	

	<!-- section -->
	<div class="section">
		<!-- container -->
		<div class="container">
			<!-- row -->
			<div class="row">
				<!-- section-title -->
				<div class="col-md-12">
					<div class="section-title">
						<h2 class="title">Añadir Nuevo Producto</h2>
						<div class="pull-right">
							<div class="product-slick-dots-1 custom-dots"></div>
						</div>
					</div>
				</div>
				<!-- /section-title -->

				
				<!-- Produc Slick -->
				<div class="col-md-9 col-sm-6 col-xs-6">
					
					<form  action="agregar_producto.html" action="ControladorServlet" method="post" enctype="multipart/form-data">
						<label for="name">Titulo del Producto:</label><br>
						<input class="form-wt" type="text" name="nombreProd" value="" required><br>
						<label for="apellido">Descripción</label><br>
						<input class="form-wt" type="text" name="descripcionProd" value="" required><br>
						<label for="email">Precio:</label><br>    
						<input class="form-wt" type="text" name="precioProd" value="" required><br>
						<label for="direccion">Imagen:</label><br>
						<input id="fotoproducto" class="form-wt" name="fotoproducto" type="file"><br />
						<h4>Categoría:</h4>
						<select name="categoriaProd" required>
							<option value="Ropa">Ropa</option>
							<option value="Electronica">Electrónica</option>
							<option value="Hogar">Hogar</option>
							<option value="Deporte">Deporte</option>
							<option value="Cultura">Cultura</option>
							
						</select>
						<br></br>
					<input type="submit" class="primary-btn add-to-cart" value="Aceptar">
					</form>	

				</div>
				<!-- /Product Slick -->
			</div>
			<!-- /row -->

			
		</div>
		<!-- /container -->
	</div>
	<!-- /section -->

	<!-- section -->
	

	<!-- FOOTER -->
	<footer id="footer" class="section section-grey">
		<!-- container -->
		<div class="container">
			<!-- row -->
			<div class="row">
				<!-- footer widget -->
				<div class="col-md-3 col-sm-6 col-xs-6">
					<div class="footer">
						<!-- footer logo -->
						<div class="footer-logo">
							<a class="logo" href="#">
		            <img src="./img/logo.png" alt="">
		          </a>
						</div>
						<!-- /footer logo -->

						<p>Somos los mejores</p>

						<!-- footer social -->
						
						<!-- /footer social -->
					</div>
				</div>
			

				<div class="clearfix visible-sm visible-xs"></div>

				<!-- footer widget -->
				<div class="col-md-3 col-sm-6 col-xs-6">
					<div class="footer">
						<h3 class="footer-header">Servicio</h3>
						<ul class="list-links">
							<li><a href="#">About Us</a></li>
						</ul>
					</div>
				</div>
				<!-- /footer widget -->

				<!-- footer subscribe -->
				<!-- /footer subscribe -->
			</div>
			<!-- /row -->
			<hr>
			<!-- row -->
			<div class="row">
				<div class="col-md-8 col-md-offset-2 text-center">
					<!-- footer copyright -->
					<div class="footer-copyright">
						<!-- Link back to Colorlib can't be removed. Template is licensed under CC BY 3.0. -->
						Copyright &copy;<script>document.write(new Date().getFullYear());</script> All rights reserved | This template is made with <i class="fa fa-heart-o" aria-hidden="true"></i> by <a href="https://colorlib.com" target="_blank">Colorlib</a>
						<!-- Link back to Colorlib can't be removed. Template is licensed under CC BY 3.0. -->
					</div>
					<!-- /footer copyright -->
				</div>
			</div>
			<!-- /row -->
		</div>
		<!-- /container -->
	</footer>
	<!-- /FOOTER -->

	<!-- jQuery Plugins -->
	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/slick.min.js"></script>
	<script src="js/nouislider.min.js"></script>
	<script src="js/jquery.zoom.min.js"></script>
	<script src="js/main.js"></script>

</body>
</html>