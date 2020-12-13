package model;


import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the compra database table.
 * 
 */
@Entity
@NamedQuery(name="Compra.findAll", query="SELECT c FROM Compra c")
public class Compra implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String referencia;

	private String comprador;

	private String cv2;

	private String direccion;

	private String fecha;

	private String fechatarjeta;

	private float precio;

	private String tarjeta;

	private String vendedor;

	public Compra() {
	}

	public String getReferencia() {
		return this.referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getComprador() {
		return this.comprador;
	}

	public void setComprador(String comprador) {
		this.comprador = comprador;
	}

	public String getCv2() {
		return this.cv2;
	}

	public void setCv2(String cv2) {
		this.cv2 = cv2;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getFecha() {
		return this.fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getFechatarjeta() {
		return this.fechatarjeta;
	}

	public void setFechatarjeta(String fechatarjeta) {
		this.fechatarjeta = fechatarjeta;
	}

	public float getPrecio() {
		return this.precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	public String getTarjeta() {
		return this.tarjeta;
	}

	public void setTarjeta(String tarjeta) {
		this.tarjeta = tarjeta;
	}

	public String getVendedor() {
		return this.vendedor;
	}

	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}

	public Compra(String referencia, String comprador, String cv2, String direccion, String fecha, String fechatarjeta,
			float precio, String tarjeta, String vendedor) {
		super();
		this.referencia = referencia;
		this.comprador = comprador;
		this.cv2 = cv2;
		this.direccion = direccion;
		this.fecha = fecha;
		this.fechatarjeta = fechatarjeta;
		this.precio = precio;
		this.tarjeta = tarjeta;
		this.vendedor = vendedor;
	}
	

}