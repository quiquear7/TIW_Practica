

public class Usuario {

  // El atributo nombre del usuario
  private String nombre;

  // El atributo apellido del usuario
  private String apellido;

//El atributo direccion del usuario
 private String direccion;

  //El atributo email del usuario
  private String email;


//El atributo contraseña del usuario
 private String contraseña;

//El atributo rol del usuario
  private String rol;

  // Crea el nuevo Usuario
  public Usuario() {
  }

  public Usuario(String nombre,String apellido,String direccion, String email, String contraseña, String rol) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.direccion = direccion;
    this.email = email;
    this.contraseña = contraseña;
    this.rol = rol;
  }

  /** Getter for property nombre
   *  @return Value of property nombre.
   */
  public String getNombre() {
    return nombre;
  }


  /** Setter for property nombre
   *  @param nombre New value of property nombre.
   */
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  /** Getter for property apellido.
   * @return Value of property apellido.
   */
  public String getApellido() {
    return apellido;
  }

  /** Setter for property apellido.
   * @param apellido New value of property apellido.
   */
  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  /** Getter for property direccion.
   * @return Value of property direccion.
   */
  public String getDireccion() {
    return direccion;
  }

  /** Setter for property direccion.
   * @param direccion New value of property direccion.
   */
  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  /** Getter for property email.
   * @return Value of property email.
   */
  public String getEmail() {
    return email;
  }

  /** Setter for property email.
   * @param email New value of property email.
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /** Getter for property contraseña.
   * @return Value of property contraseña.
   */
  public String getContraseña() {
    return contraseña;
  }

  /** Setter for property contraseña.
   * @param contraseña New value of property contraseña.
   */
  public void setContraseña(String contraseña) {
    this.contraseña = contraseña;
  }

  public String getRol() {
	    return rol;
  }

  public void setRol(String rol) {
		this.rol = rol;
  }


}
