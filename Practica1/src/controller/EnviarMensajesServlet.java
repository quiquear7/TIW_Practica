package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Queue;
import javax.jms.Connection;
import javax.jms.Session;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import model.Usuario;


/**
 * Implementacion del Servlet de Enviar Mensajes
 */
@WebServlet({ "/EnviarMensajesServlet","/enviarMensajes.html" })
public class EnviarMensajesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource
	ConnectionFactory connectF; //Definimos la ConnectionFactory
	@Resource(mappedName="connectF")
	Queue cola;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EnviarMensajesServlet() {
        super();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
					try {
						//llamamos al metodo doPost
						doPost(req, res);
					} catch (Exception e) {
						System.out.println("Error en el doGet: " + e);
					}

				}

    
    public void doPost(HttpServletRequest req, HttpServletResponse res)	throws ServletException,IOException {
    					try {

    						// Creamos una conexion
    						 Connection conexion = connectF.createConnection();

    						// Creamos la sesion
    						 //javax.jms.QueueSession QSes = null; //... COMPLETE ....

    						 Session sesion = conexion.createSession(false, Session.AUTO_ACKNOWLEDGE);

    						// Asignamos la cola a la sesion para crear a la persona que envia los mensajes
    						 javax.jms.QueueSender Qsen =  null;
    						 MessageProducer messageProd = sesion.createProducer(cola);

    							// Creamos un mensaje de texto
                  // Creamos la sesion de un cliente y sacamos su rol ya que solo los vendedores pueden enviar mensajes
    						 javax.jms.TextMessage men = null;
    						 TextMessage textMessage = sesion.createTextMessage();
    						 HttpSession miSesion = req.getSession(true);
    						 Usuario aux = (Usuario) ((ServletRequest) sesion).getAttribute("usuario");
    						 String rol = aux.getRol();
    						 String mensaje, emailRec;
    						 String pagina="index.jsp"; //Definimos la pagina inicial
    						 int respuesta= (int) miSesion.getAttribute("respuesta");
    					if(respuesta==0){
    						 if(rol.equals("Vendedor")){ //Si el rol del cliente es el de vendedor, sacamos su campo de respuesta
    								 mensaje=req.getParameter("mensaje");

                    //Si el tipo de usuario es vendedor e iniciar el mensaje, se debe añadir una variable de
    		 						// Ejecuta la consulta para sacar todos los usuarios en comun

    		 						try {
    			 							ResultSet rs=null;
    			 							String consulta="";
    			 							String servernom = "localhost";
    			 						 Context mycon= new InitialContext();
    			 						 DataSource olo =  (DataSource) mycon.lookup("CTWDS");
    			 						 java.sql.Connection con= olo.getConnection();

    			 						 if (con==null){
    			 							 System.out.println("--->ERROR DE CONEXION CON EL SERVIDOR:" + servernom);
    			 						 }
    			 						 else {
    			 							 // Crea un Statement
    										 Statement st= con.createStatement();
    			 							 // Ejecuta la consulta "select * from users"
    			 							 rs=st.executeQuery("SELECT * from usuarios WHERE role='Cliente'");
    			 						 }

    				 					 if(rs.next()==false){ //Si encontramos un error al ejecutar la consulta, nos redireccionara a la pagina de error al enviar
    				 						 pagina="enviar-error.jsp";
    				 					 }

    				 						else{
    											rs.beforeFirst();
    				 							while(rs.next()){
    				 								String emailUsuarioActual = rs.getString("email");
                            //Sacamos el mensaje de los parametros y lo asignamos a una de las colas
    												 textMessage.setText(mensaje);
    												 //creamos los campos para los usuarios que envian y reciben -> los usuarios que reciban mensajes seran un por cada iteracion del bucle.
    												 textMessage.setStringProperty("emisor", aux.getEmail());
    												 textMessage.setStringProperty("receptor", emailUsuarioActual);

    												// Enviamos el mensaje a traves del emisor
    												messageProd.send(textMessage);

    				 							}

    				 						}
    		 					 }
    		 					 catch(Exception e) {
    		 					 }

    						}
  // Si el rol es distinto al del vendedores

          else{
  							 mensaje=req.getParameter("mensaje");
  							 emailRec=req.getParameter("email");

  	             // verifica si el email del receptor existe
  							 try {

  	 							 ResultSet rs=null;
  	 	 						 String consulta="";

  								String servernom = "localhost";
  								Context mycon= new InitialContext();
  								DataSource olo =  (DataSource) mycon.lookup("CTWDS");
  								java.sql.Connection con= olo.getConnection();


  								if (con==null){
                    System.out.println("--->ERROR DE CONEXION CON EL SERVIDOR:" + servernom);
  								}
  								else {

                    // Crea un Statement
                    Statement st= con.createStatement();
                    // Ejecuta la consulta "select * from users"
  									consulta="SELECT * from usuarios WHERE email="+"'"+emailRec+"'";
  									rs=st.executeQuery(consulta);
  								}

  								if(rs.next()==false){ //Si no existe el email
  									pagina="enviar-error.jsp";
  								}
                  //verifica si el usuario puede enviar dicho  mensaje al receptor

  								else{
  									String rolRec=rs.getString("rol");
  									if(rol.equals("Cliente")){// Si el enisor es un "Cliente" solo puede enviar mensajes a un vendedor
  										if(rolRec.equals("Vendedor")){
                        //Obtenemos el mensaje del parametro y lo asignamos a uno de la cola
  											 textMessage.setText(mensaje);
  											 //creamos las propiedades emisor y receptor
  											 textMessage.setStringProperty("emisor", aux.getEmail());
  											 textMessage.setStringProperty("receptor",emailRec);
  											// Enviamos el mensaje a traves del emisor
  											messageProd.send(textMessage);
  										}
  										else{ //si no se puede enviar
  											pagina="enviar-error.jsp";
  										}
  									}
  									else{//el administrador no puede enviar mensajes a otro administrador
  										if(!rolRec.equals("Administrador")){
  											  //Obtenemos el mensaje del parametro y lo asignamos a uno de la cola
  											 textMessage.setText(mensaje);
                         //creamos las propiedades emisor y receptor
                        textMessage.setStringProperty("emisor", aux.getEmail());
                        textMessage.setStringProperty("receptor",emailRec);
                       // Enviamos el mensaje a traves del emisor
                       messageProd.send(textMessage);
                     }
                     else{ //si no se puede enviar
                       pagina="enviar-error.jsp";
  										}

  									}


  								}
  							}
  							catch(Exception e) {
  								System.out.println("<FONT color=\"#ff0000\">"+e.getMessage()+"</FONT><BR>");
  							}


  					}
  				}
  				else{//la respuesta no tiene restricciones
  					mensaje=req.getParameter("mensaje");
  					emailRec=req.getParameter("email");
            //Obtenemos el mensaje del parametro y lo asignamos a uno de la cola
            textMessage.setText(mensaje);
            //creamos las propiedades emisor y receptor
            textMessage.setStringProperty("emisor", aux.getEmail());
            textMessage.setStringProperty("receptor",emailRec);
            // Enviamos el mensaje a traves del emisor
            messageProd.send(textMessage);

  				}

  					//si la respuesta es valida


  					req.getRequestDispatcher(pagina).forward(req, res);
  						// Cerramos el productor de mensajes
  						messageProd.close();
  						// Cerramos la sesion
  						sesion.close();
  						// Cerramos la conexion
  						conexion.close();



  					} catch (Exception e) {
  						System.out.println("Error en el doPost: " + e);
  						


  			}


  	}

}
