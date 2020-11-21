package model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the compra database table.
 * 
 */

public class Comp implements Serializable {
	private static final long serialVersionUID = 1L;

	private String referencia;

	private String direccion;

	private String fecha;

	private float precio;

	private String tarjeta;

	private String vendedor;

	private String comprador;

	public Comp(String referencia, String vendedor,String comprador, float precio, String tarjeta,String direccion,String fecha) {
		super();
		this.referencia = referencia;
		this.direccion = direccion;
		this.fecha = fecha;
		this.precio = precio;
		this.tarjeta = tarjeta;
		this.vendedor = vendedor;
		this.comprador = comprador;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
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

	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	public String getTarjeta() {
		return tarjeta;
	}

	public void setTarjeta(String tarjeta) {
		this.tarjeta = tarjeta;
	}

	public String getVendedor() {
		return vendedor;
	}

	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}

	public String getComprador() {
		return comprador;
	}

	public void setComprador(String comprador) {
		this.comprador = comprador;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}