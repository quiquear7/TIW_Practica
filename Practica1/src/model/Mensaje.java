package model;

import java.io.Serializable;

public class Mensaje implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String emisor;
	private String receptor;
	private String mensaje;
	public String getEmisor() {
		return emisor;
	}
	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}
	public String getReceptor() {
		return receptor;
	}
	public void setReceptor(String receptor) {
		this.receptor = receptor;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public Mensaje() {
		super();
	}
	public Mensaje(String emisor, String receptor, String mensaje) {
		super();
		this.emisor = emisor;
		this.receptor = receptor;
		this.mensaje = mensaje;
	}
	
}
