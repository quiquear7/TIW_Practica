package model;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;

public class Producto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int referencia;

	  // El atributo apellido del usuario
	 private String nombre;

	//El atributo direccion del usuario
	 private String descripcion;

	  //El atributo email del usuario
	  private String categoria;

	  private byte[] imagen;
	//El atributo contraseña del usuario
	 private Float precio;

	//El atributo rol del usuario
	  private String vendedor;

	  private Boolean estado;
	  // Crea el nuevo Usuario
	  public Producto() {
	  }

	  public Producto(int referencia,String titulo,String descripcion, String categoria, byte[] imagen, Float precio, String user, Boolean estado) {
	    this.referencia = referencia;
	    this.vendedor = titulo;
	    this.descripcion = descripcion;
	    this.categoria = categoria;
	    this.imagen = imagen;
	    this.precio = precio;
	    this.nombre = user;
	    this.estado = estado;
	  }

	public int getReferencia() {
		return referencia;
	}

	public void setReferencia(int referencia) {
		this.referencia = referencia;
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

	public void setImagen(byte[] inputStream) {
		this.imagen = inputStream;
	}

	public Float getPrecio() {
		return precio;
	}

	public void setPrecio(Float precio) {
		this.precio = precio;
	}


	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public String getVendedor() {
		return vendedor;
	}

	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
