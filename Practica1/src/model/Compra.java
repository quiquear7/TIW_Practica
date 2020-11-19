package model;

import java.io.Serializable;

public class Compra  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int referencia;
	private String comprador;
	private String vendedor;


	//El atributo contraseña del usuario
	 private Float precio;

	//El atributo rol del usuario
	  private String direccion;

	  private String fecha;
	  private int referencia_compra;
	  private String titulo;
	  private byte[] imagen;
	public int getReferencia() {
		return referencia;
	}
	public void setReferencia(int referencia) {
		this.referencia = referencia;
	}
	public String getComprador() {
		return comprador;
	}
	public void setComprador(String comprador) {
		this.comprador = comprador;
	}
	public String getVendedor() {
		return vendedor;
	}
	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}
	public Float getPrecio() {
		return precio;
	}
	public void setPrecio(Float precio) {
		this.precio = precio;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public int getReferencia_compra() {
		return referencia_compra;
	}
	public void setReferencia_compra(int referencia_compra) {
		this.referencia_compra = referencia_compra;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public  byte[] getImagen() {
		return imagen;
	}
	public void setImagen( byte[] imagen) {
		this.imagen = imagen;
	}


}
