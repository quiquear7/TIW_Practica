<%@page import="Usuario"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

    	<%@page import="Usuario"%>
    <%@page import="java.sql.ResultSet"
      import="javax.naming.Context"
      import="javax.naming.InitialContext"
      import="javax.sql.DataSource"
      import="java.sql.SQLException"
      import="java.sql.Statement"%>
    <%@page import="java.util.*"%>

    <%@page import="java.util.ArrayList"%>

<!DOCTYPE html>
<html lang="en">

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->

	<title>Mobile shop</title>

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

	<!-- Manual creation styles -->
	<link type="text/css" rel="stylesheet" href="css/manual.css" />

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


				</div>
				<div class="pull-right">
					<ul class="header-btns">
            <!-- Account -->
            <li class="header-account dropdown default-dropdown">
              <div class="dropdown-toggle" role="button" data-toggle="dropdown" aria-expanded="true">
                <div class="header-btns-icon">
                  <i class="fa fa-user-o"></i>
                </div>
                <strong class="text-uppercase">Mi Cuenta <i class="fa fa-caret-down"></i></strong>
              </div>
              <% 	Object objlog=session.getAttribute("log");
                String login=(String) objlog;
                if(login=="false"){ %>
                <a href="login.html" class="text-uppercase">Login</a>|<a href="registro.html" class="text-uppercase">Registro</a>
                <% }
                else{
                Object objuser=session.getAttribute("user");
                Usuario currUser=(Usuario) objuser; %>
                <a class="text-uppercase">Bievenido,<%= currUser.getName() %></a>
                <% }%>
                <ul class="custom-menu">
                <% if(login=="false"){ %>
                <li><a href="login.html"><i class="fa fa-unlock-alt"></i> Login</a></li>
                <li><a href="registro.html"><i class="fa fa-user-plus"></i> Crear una Cuenta</a></li>
                <% }
                else{
                  Object objuser=session.getAttribute("user");
                    Usuario currUser=(Usuario) objuser;
                    if(currUser1.getRole().compareTo("Cliente")==0){%>
                    <li><a href="profile.html"><i class="fa fa-user-o"></i> Mi Cuenta</a></li>
                    <li><a href="view-wishlist.html"><i class="fa fa-heart-o"></i> Mi Lista de Deseos</a></li>
                    <li><a href="sendMessage.html"><i class="fa fa-exchange"></i> Enviar un Mensaje</a></li>
                    <li><a href="readMessages.html"><i class="fa fa-exchange"></i> Leer los Mensajes</a></li>
                    <li><a href="myorders.html"><i class="fa fa-check"></i> Mis Pedidos</a></li>
                    <li><a href="logout.html"><i class="fa fa-user-plus"></i> Log Out</a></li>
                      <% }%>
                      <%if(currUser.getRole().compareTo("Vendedor")==0){%>
                    <li><a href="profile.html"><i class="fa fa-user-o"></i> Mi Cuenta</a></li>
                    <li><a href="sendMessage.html"><i class="fa fa-exchange"></i> Enivar un Mensaje</a></li>
                    <li><a href="readMessages.html"><i class="fa fa-exchange"></i> Leer los Mensajes</a></li>
                    <li><a href="registrar-producto.html"><i class="fa fa-user-plus"></i>Añadir productos</a></li>
                    <li><a href="productlist.html"><i class="fa fa-user-plus"></i>Lista de Productos</a></li>
                    <li><a href="logout.html"><i class="fa fa-user-plus"></i> Log Out</a></li>
                    <% }%>
                    <%if(currUser.getRole().compareTo("Administrador")==0){%>
                    <li><a href="profile.html"><i class="fa fa-user-o"></i> Mi Cuenta</a></li>
                    <li><a href="sendMessage.html"><i class="fa fa-exchange"></i> Enivar un Mensaje</a></li>
                    <li><a href="readMessages.html"><i class="fa fa-exchange"></i> Leer los Mensajes</a></li>
                    <li><a href="userlist.html"><i class="fa fa-heart-o"></i>Gestión de Usuarios</a></li>
                    <li><a href="productlist.html"><i class="fa fa-heart-o"></i> Gestión de Productos</a></li>
                    <li><a href="logout.html"><i class="fa fa-user-plus"></i> Log Out</a></li>
                            <% }%>
                  <% }%>
              </ul>
            </li>
            <!-- /Account -->

            <!-- Cart -->
            <%
            if(login!="false"){
            Usuario aux = (Usuario) session.getAttribute("user");
            if (aux!=null){
              if (aux.getRole().equals("Cliente")){


            %>
            <li class="header-cart dropdown default-dropdown">
              <a class="dropdown-toggle" data-toggle="dropdown" aria-expanded="true">
                <div class="header-btns-icon">
                  <i class="fa fa-shopping-cart"></i>
                </div>
                <strong class="text-uppercase">Mi Carrito</strong>
                <br>
              </a>
              <div class="custom-menu">
                <div id="shopping-cart">

                  <div class="shopping-cart-btns">
                    <ul>
                    <li><a href="view-cart.html"><i class="fa fa-exchange"></i> Ver mi Carrito</a></li>
                    </ul>
                  </div>
                </div>
              </div>
            </li>
            <%
            }
            }
          }
            %>
            <!-- /Cart -->
						<!-- Mobile nav toggle-->
						<li class="nav-toggle">
							<button class="nav-toggle-btn main-btn icon-btn"><i class="fa fa-bars"></i></button>
						</li>
						<!-- / Mobile nav toggle -->
					</ul>
				</div>
			</div>
			<!-- header -->
		</div>
		<!-- container -->
	</header>
	<!-- /HEADER -->

	<!-- NAVIGATION -->
	<div id="navigation">
		<!-- container -->
		<div class="container">
			<div id="responsive-nav">
				<!-- category nav -->
				<div class="category-nav">
					<span class="category-header">Categorías <i class="fa fa-list"></i></span>

				</div>
				<!-- /category nav -->

				<!-- menu nav -->
				<div class="menu-nav">
					<span class="menu-header">Menu <i class="fa fa-bars"></i></span>
					<ul class="menu-list">
					<!-- <li><a href="listarusuarios">administrar usuario</a></li> -->

          <li class="dropdown mega-dropdown">
            <a class="dropdown-toggle" href="#" data-toggle="dropdown" aria-expanded="true">Todos los Productos <i class="fa fa-caret-down"></i></a>
            <div class="custom-menu">
              <div class="row">
                <div class="col-md-4">
                  <ul class="list-links">
                    <li>
                      <h3 class="list-links-title">Productos</h3></li>
                      <%
                      Context mycon= new InitialContext();
                      DataSource olo =  (DataSource) mycon.lookup("CTWDS");
                      java.sql.Connection con= olo.getConnection();
                      Statement st = con.createStatement();
                      String queryCart = "SELECT * from product";
                      ResultSet rs=st.executeQuery(queryCart);
                        while (rs.next()){
                            %>
                            <form method="POST" action="display-product.html">
                              <li><input style="display:none" name="currentProduct" value="<%= rs.getString("name") %>"></input></li>
                              <li><input style="background: none!important; border: none; padding: 0!important; font-family: arial, sans-serif; color: #069; text-decoration: underline; cursor: pointer;" type="submit" value="<%= rs.getString("name") %>"></input></li>
                            </form>
                            <%
                        }
                        st.close();
                        con.close();
                        rs.close();
                      %>
                  </ul>
                  <hr class="hidden-md hidden-lg">
                </div>


              </div>
            </div>
          </li>

          <div class="header-search" style="float:right">
            <form method="POST" action="search.html">
              <input style="border: 2px solid black" type="text" name="keyword" placeholder="Introduzca palabra clave para buscar">

              <input type="submit" style="display:none "value="">

            </form>
          </div>


        </li>


        </ul>
      </div>
      <!-- menu nav -->
    </div>
  </div>
  <!-- /container -->
</div>
<!-- /NAVIGATION -->

<!-- BREADCRUMB -->
<div id="breadcrumb">
  <div class="container">
    <ul class="breadcrumb">
      <li><a href="#">Inicio</a></li>
      <li class="active">Perfil</li>
    </ul>
  </div>
</div>
<!-- /BREADCRUMB -->

	<!-- section -->
	<div class="section">
		<!-- container -->
		<div class="container">
			<!-- row -->
			<div class="row">
			</div>
			<!-- /row -->
		</div>
		<!-- /container -->
	</div>
	<!-- /section -->

	<!-- REGISTER SECTION -->
	<form method="POST" action="modify-profile.html">
		<% 	Object objuser=session.getAttribute("user");
		Usuario currUser=(Usuario) objuser; %>
		Nombre:  <input class="form-wt" type="text" name="nombre" value="<%=currUser.getNombre() %>"><br>
		Apellido:  <input class="form-wt" type="text" name="Apellido" value="<%=currUser.getApellido() %>"><br>
		Direccion:  <input class="form-wt" type="text" name="Direccion" value="<%=currUser.getDireccion() %>"><br>
		Email:  <input class="form-wt" type="email" name="email" value="<%=currUser.getEmail() %>"><br>
		Contraseña:  <input class="form-wt" type="password" name="Contraseña" value="<%=currUser.getContraseña() %>">
		<br></br>
		<input type="submit" value="Submit">
	</form>
	<br><br><br>
	<form method="POST" action="borrar-perfil.html">
		<input type="submit" value="Borrar Pefil">
	</form>

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

  						<p>askdaskdlj</p>

  						<!-- footer social -->
  						<ul class="footer-social">
  							<li><a href="#"><i class="fa fa-facebook"></i></a></li>
  							<li><a href="#"><i class="fa fa-twitter"></i></a></li>
  							<li><a href="#"><i class="fa fa-instagram"></i></a></li>
  							<li><a href="#"><i class="fa fa-google-plus"></i></a></li>
  							<li><a href="#"><i class="fa fa-pinterest"></i></a></li>
  						</ul>
  						<!-- /footer social -->
  					</div>
  				</div>
  				<!-- /footer widget -->

          <!-- footer widget -->
          <div class="col-md-3 col-sm-6 col-xs-6">
            <div class="footer">
              <h3 class="footer-header">Mi Cuenta</h3>
                <% 	Object objlog1=session.getAttribute("log");
                  String login1=(String) objlog1;%>
                <ul class="list-links">
                  <% if(login1=="false"){ %>
                  <li><a href="login.html"><i class="fa fa-unlock-alt"></i> Login</a></li>
                  <li><a href="registro.html"><i class="fa fa-user-plus"></i> Crear una Cuenta</a></li>
                  <%}else{
                    Object objuser1=session.getAttribute("user");
                      Usuario currUser1=(Usuario) objuser1;
                      if(currUser1.getRole().compareTo("Cliente")==0){%>
                      <li><a href="profile.html"><i class="fa fa-user-o"></i> Mi Cuenta</a></li>
                      <li><a href="view-wishlist.html"><i class="fa fa-heart-o"></i> Mi Lista de Deseos</a></li>
                      <li><a href="sendMessage.html"><i class="fa fa-exchange"></i> Enviar un Mensaje</a></li>
                      <li><a href="readMessages.html"><i class="fa fa-exchange"></i> Leer los Mensajes</a></li>
                      <li><a href="myorders.html"><i class="fa fa-check"></i> Mis Pedidos</a></li>
                      <li><a href="logout.html"><i class="fa fa-user-plus"></i> Log Out</a></li>
                        <% }%>
                        <%if(currUser.getRole().compareTo("Vendedor")==0){%>
                      <li><a href="profile.html"><i class="fa fa-user-o"></i> Mi Cuenta</a></li>
                      <li><a href="sendMessage.html"><i class="fa fa-exchange"></i> Enivar un Mensaje</a></li>
                      <li><a href="readMessages.html"><i class="fa fa-exchange"></i> Leer los Mensajes</a></li>
                      <li><a href="registrar-producto.html"><i class="fa fa-user-plus"></i>Añadir productos</a></li>
                      <li><a href="productlist.html"><i class="fa fa-user-plus"></i>Lista de Productos</a></li>
                      <li><a href="logout.html"><i class="fa fa-user-plus"></i> Log Out</a></li>
                      <% }%>
                      <%if(currUser.getRole().compareTo("Administrador")==0){%>
                      <li><a href="profile.html"><i class="fa fa-user-o"></i> Mi Cuenta</a></li>
                      <li><a href="sendMessage.html"><i class="fa fa-exchange"></i> Enivar un Mensaje</a></li>
                      <li><a href="readMessages.html"><i class="fa fa-exchange"></i> Leer los Mensajes</a></li>
                      <li><a href="userlist.html"><i class="fa fa-heart-o"></i>Gestión de Usuarios</a></li>
                      <li><a href="productlist.html"><i class="fa fa-heart-o"></i> Gestión de Productos</a></li>
                      <li><a href="logout.html"><i class="fa fa-user-plus"></i> Log Out</a></li>
                              <% }%>
                    <% }%>


                </ul>
            </div>
          </div>
          <!-- /footer widget -->

          <div class="clearfix visible-sm visible-xs"></div>

          <!-- footer widget -->
          <div class="col-md-3 col-sm-6 col-xs-6">
            <div class="footer">
              <h3 class="footer-header">Servicio al Clinte</h3>
              <ul class="list-links">
                <li><a href="#">Sobre Nosotros</a></li>
                <li><a href="#">Envío y Devoluciones</a></li>
                <li><a href="#">Guía de Envío</a></li>
                <li><a href="#">FAQ</a></li>
              </ul>
            </div>
          </div>
          <!-- /footer widget -->

          <!-- footer subscribe -->
          <div class="col-md-3 col-sm-6 col-xs-6">
            <div class="footer">
              <h3 class="footer-header">Mantente Conectado</h3>
              <p>Suscríbase a nuestra revista para descuentos y promociones</p>
              <form>
                <div class="form-group">
                  <input class="input" placeholder="Introduzca direccion de email">
                </div>
                <button class="primary-btn">Suscríbase a nuestra revista</button>
              </form>
            </div>
          </div>
          <!-- /footer subscribe -->
        </div>
        <!-- /row -->
        <hr>
        <!-- row -->
        <div class="row">
          <div class="col-md-8 col-md-offset-2 text-center">
            <!-- footer copyright -->

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
