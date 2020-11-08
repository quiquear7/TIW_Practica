<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="servlet.Usuario"%>
<%@page import="java.sql.ResultSet"
        import="javax.naming.InitialContext"
        import="javax.naming.Context"
        import="java.sql.Statement"
        import="javax.sql.DataSource"
        import="java.sql.SQLException"%>
<%@page import="java.util.*"%>
<%@page import="java.util.ArrayList"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->

	<title>Oyarzabal</title>

	<!-- Google font -->
	<link href="https://fonts.googleapis.com/css?family=Hind:400,700" rel="stylesheet">

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
						<a class="logo" href="index.html">
							<img src="./img/logo.png" alt="">
						</a>
					</div>
					<!-- /Logo -->

					<!-- Search -->
				
				</div>
				<div class="pull-right">
					<ul class="header-btns">
						<!-- Account -->
						<li class="header-account dropdown default-dropdown">
							<div class="dropdown-toggle" role="button" data-toggle="dropdown" aria-expanded="true">
								<div class="header-btns-icon">
									<i class="fa fa-user-o"></i>
								</div>
								<% 
							Object log = (Object) session.getAttribute("sesion_iniciada");
							Boolean login = (Boolean) log;
							System.out.println(login);
							Object user = (Object) session.getAttribute("usuario");
							Usuario usu = (Usuario) user;
							
							if(login == false || usu.getEmail() == null){%>
								<strong class="text-uppercase">Mi Cuenta <i class="fa fa-caret-down"></i></strong>
								<%}else{%>
									<strong class="text-uppercase"><%=usu.getEmail()%> <i class="fa fa-caret-down"></i></strong>
								<%}%>
							</div>
							
							
							<ul class="custom-menu">
							<% if(login == false){%>
								<li><a href="login.html"><i class="fa fa-unlock-alt"></i>Login</a></li>
								<li><a href="registro.html"><i class="fa fa-user-plus"></i> Crear Cuenta</a></li>
							<%} else{ %>
								<li><a href="cuenta.html"><i class="fa fa-user-o"></i> Mi Cuenta</a></li>
								<li><a href="modificar_usuario.html"><i class="fa fa-unlock-alt"></i>Modificar Usuario</a></li>
								<li><a href="cerrar_sesion.html"><i class="fa fa-user-plus"></i> Cerrar Sesion</a></li>
							<%}%>
						

						</ul>
						<li class="header-account dropdown default-dropdown">
							
							
							 
						
						
						<%if(login == true && usu.getRol().compareTo("Vendedor")==0 ){%>
					
							<strong><a href="add_producto.html">Nuevo Producto</a></strong>
						
							<%}%>
							
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
						<h2 class="title">Registro</h2>
						<div class="pull-right">
							<div class="product-slick-dots-1 custom-dots"></div>
						</div>
					</div>
				</div>
				<!-- /section-title -->

				
				<!-- Produc Slick -->
				<div class="col-md-9 col-sm-6 col-xs-6">
					
					<form  action="analizar-registro.html" action="ControladorServlet" method="post">
						<label for="name">Nombre:</label><br>
						<input class="form-wt" type="text" name="nombre" value="" required><br>
						<label for="apellido">Apellido:</label><br>
						<input class="form-wt" type="text" name="apellido" value="" required><br>
						<label for="email">Email:</label><br>    
						<input class="form-wt" type="email" name="email" value="" required><br>
						<label for="direccion">Dirección:</label><br>
						<input class="form-wt" type="text" name="direccion" value="" required><br>
						<label for="contrasenia">Contraseña:</label><br>
						<input class="form-wt" type="password" name="contrasenia" value="" required>
						<h4>Tipo de Usuario:</h4>
						<select name="rol" required>
							<option value="Cliente">Cliente</option>
							<option value="Vendedor">Vendedor</option>
							<option value="Admin">Administrador</option>
						</select>
						<br></br>
					<input type="submit" value="Aceptar">
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