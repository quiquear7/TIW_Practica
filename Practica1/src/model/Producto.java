package model;

public class Producto {
	private int referencia;

	  // El atributo apellido del usuario
	 private String titulo;

	//El atributo direccion del usuario
	 private String descripcion;

	  //El atributo email del usuario
	  private String categoria;

	  private byte[] imagen;
	//El atributo contraseña del usuario
	 private Float precio;

	//El atributo rol del usuario
	  private String user;

	  private Boolean estado;
	  // Crea el nuevo Usuario
	  public Producto() {
	  }

	  public Producto(int referencia,String titulo,String descripcion, String categoria, byte[] imagen, Float precio, String user, Boolean estado) {
	    this.referencia = referencia;
	    this.titulo = titulo;
	    this.descripcion = descripcion;
	    this.categoria = categoria;
	    this.imagen = imagen;
	    this.precio = precio;
	    this.user = user;
	    this.estado = estado;
	  }

	public int getReferencia() {
		return referencia;
	}

	public void setReferencia(int referencia) {
		this.referencia = referencia;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public byte[] getImagen() {
		return imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}

	public Float getPrecio() {
		return precio;
	}

	public void setPrecio(Float precio) {
		this.precio = precio;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
}
