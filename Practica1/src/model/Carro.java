package model;

import java.io.Serializable;

public class Carro implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int referencia;

	 private String titulo;
	
	//El atributo contraseña del usuario
	 private Float precio;

	//El atributo rol del usuario
	  private String user;
	  private byte[] imagen;

	  public Carro() {
	  }

	  public Carro(int referencia,Float precio, String user,String titulo,byte[] imagen) {
	    this.referencia = referencia;
	    this.precio = precio;
	    this.user = user;
	    this.titulo = titulo;
	    this.imagen=imagen;
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

	public byte[] getImagen() {
		return imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}

	
	  
}
